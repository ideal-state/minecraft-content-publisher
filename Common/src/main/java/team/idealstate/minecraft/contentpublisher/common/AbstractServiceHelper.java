/*
 *     <one line to give the program's name and a brief idea of what it does.>
 *     Copyright (C) 2024  ideal-state
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package team.idealstate.minecraft.contentpublisher.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.JarUtils;
import team.idealstate.hyper.common.RsaUtils;
import team.idealstate.hyper.common.jackson.YamlUtils;
import team.idealstate.hyper.rpc.api.future.Future;
import team.idealstate.hyper.rpc.api.service.ServiceInvoker;
import team.idealstate.hyper.rpc.api.service.ServiceManager;
import team.idealstate.hyper.rpc.api.service.ServiceStarter;
import team.idealstate.hyper.rpc.api.service.Watchdog;
import team.idealstate.hyper.rpc.impl.netty.ClientStarter;
import team.idealstate.hyper.rpc.impl.netty.ServerStarter;
import team.idealstate.hyper.rpc.impl.service.StdServiceManager;
import team.idealstate.minecraft.contentpublisher.common.model.entity.Service;
import team.idealstate.minecraft.contentpublisher.common.service.ContentPublishService;
import team.idealstate.minecraft.contentpublisher.common.service.ContentSubscribeService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.Key;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>AbstractServiceHelper</p>
 *
 * <p>创建于 2024/2/12 11:26</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractServiceHelper {
    private static final Logger logger = LogManager.getLogger(AbstractServiceHelper.class);
    protected final Lock lock = new ReentrantLock();
    protected final StartupListener startupListener;
    private final KeyType keyType;
    private final StarterType starterType;
    protected volatile ServiceManager serviceManager;
    private volatile SocketAddress address;
    private volatile int nThreads;
    private volatile int timeout;
    private volatile int maximumRetry;
    private volatile Key key;
    private volatile ClassLoader classLoader;
    private volatile Watchdog watchdog;

    protected AbstractServiceHelper(@NotNull KeyType keyType, @NotNull StarterType starterType, @NotNull ClassLoader classLoader, StartupListener startupListener) {
        AssertUtils.notNull(keyType, "无效的密钥类型");
        AssertUtils.notNull(starterType, "无效的服务启动器类型");
        this.keyType = keyType;
        this.starterType = starterType;
        load(classLoader);
        this.startupListener = startupListener;
    }

    private void load(@NotNull ClassLoader classLoader) {
        AssertUtils.notNull(classLoader, "无效的类加载器");
        File dataDirectory = AssetUtils.dataDirectory(getClass());
        File file = JarUtils.copy(getClass(), AssetUtils.asset(getClass(), "/service.yml"), dataDirectory);
        Service service;
        try {
            service = YamlUtils.toBean(file, Service.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.address = new InetSocketAddress(service.getHost(), service.getPort());
        Integer threads = service.getThreads();
        this.nThreads = threads <= 0 ? Runtime.getRuntime().availableProcessors() : threads;
        this.timeout = Math.max(Future.DEFAULT_TIMEOUT, service.getTimeout());
        this.maximumRetry = Math.max(10, service.getTimeout());
        file = JarUtils.copy(getClass(), AssetUtils.asset(getClass(), keyType.getPath()), dataDirectory);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] data = new byte[fileInputStream.available()];
            //noinspection ResultOfMethodCallIgnored
            fileInputStream.read(data);
            switch (keyType) {
                case PUBLIC:
                    this.key = RsaUtils.generatePublicKey(RsaUtils.importKey(data));
                    break;
                case PRIVATE:
                    this.key = RsaUtils.generatePrivateKey(RsaUtils.importKey(data));
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.classLoader = classLoader;
    }

    public void reload(@NotNull ClassLoader classLoader) {
        lock.lock();
        try {
            logger.info("开始载入配置");
            load(classLoader);
            logger.info("已载入配置");
        } finally {
            lock.unlock();
        }
    }

    public void startup() {
        lock.lock();
        try {
            if (serviceManager == null && watchdog == null) {
                this.serviceManager = new StdServiceManager(Executors.newFixedThreadPool(nThreads));
                this.serviceManager.setClassLoader(classLoader);
                ServiceStarter starter = null;
                switch (starterType) {
                    case CLIENT:
                        starter = new ClientStarter(address, key, serviceManager, nThreads);
                        break;
                    case SERVER:
                        starter = new ServerStarter(address, key, serviceManager, nThreads);
                        break;
                }
                AssertUtils.notNull(starter, "无效的服务启动器");
                ((StdServiceManager) serviceManager).setServiceInvoker((ServiceInvoker) starter);
                ((StdServiceManager) serviceManager).setTimeout(timeout);
                serviceManager.register(ContentPublishService.class);
                serviceManager.register(ContentSubscribeService.class);
                this.watchdog = new Watchdog(starter, maximumRetry);
                watchdog.startup();
                if (startupListener != null) {
                    while (watchdog.isAlive()) {
                        if (watchdog.isActive()) {
                            break;
                        }
                        try {
                            TimeUnit.SECONDS.sleep(1L);
                        } catch (InterruptedException e) {
                            logger.catching(e);
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (watchdog.isAlive()) {
                        if (watchdog.isActive()) {
                            startupListener.onSucceed();
                        } else {
                            logger.warn("已退出等待服务启动完成的阻塞");
                            logger.warn("这可能会导致一些问题（仅在即将关闭服务器前执行此操作才能保证服务正常运行）");
                        }
                    } else {
                        startupListener.onFail();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            if (serviceManager != null || watchdog != null) {
                serviceManager.shutdown();
                this.serviceManager = null;
                watchdog.shutdown();
                this.watchdog = null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void restart() {
        lock.lock();
        try {
            shutdown();
            startup();
        } finally {
            lock.unlock();
        }
    }

    public boolean isAlive() {
        lock.lock();
        try {
            if (watchdog == null) {
                return false;
            }
            return watchdog.isAlive();
        } finally {
            lock.unlock();
        }
    }

    public boolean isActive() {
        lock.lock();
        try {
            if (watchdog == null) {
                return false;
            }
            return watchdog.isActive();
        } finally {
            lock.unlock();
        }
    }

    protected enum KeyType {
        PRIVATE("/private"),
        PUBLIC("/public");

        private final String path;

        KeyType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    protected enum StarterType {
        SERVER,
        CLIENT;
    }
}
