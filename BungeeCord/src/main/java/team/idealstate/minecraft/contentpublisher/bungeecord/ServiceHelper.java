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

package team.idealstate.minecraft.contentpublisher.bungeecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.JarUtils;
import team.idealstate.hyper.common.RsaUtils;
import team.idealstate.hyper.common.jackson.YamlUtils;
import team.idealstate.hyper.rpc.api.future.Future;
import team.idealstate.hyper.rpc.api.service.ServiceManager;
import team.idealstate.hyper.rpc.api.service.Watchdog;
import team.idealstate.hyper.rpc.impl.netty.ServerStarter;
import team.idealstate.hyper.rpc.impl.service.StdServiceManager;
import team.idealstate.minecraft.contentpublisher.common.AssetUtils;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>ServiceHelper</p>
 *
 * <p>创建于 2024/2/9 16:18</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class ServiceHelper {

    private static final Logger logger = LogManager.getLogger(ServiceHelper.class);
    private final Lock lock = new ReentrantLock();
    private volatile SocketAddress address;
    private volatile int nThreads;
    private volatile int timeout;
    private volatile int maximumRetry;
    private volatile Key key;
    private volatile ClassLoader classLoader;
    private volatile ServiceManager serviceManager;
    private volatile Watchdog watchdog;

    ServiceHelper(@NotNull ClassLoader classLoader) {
        load(classLoader);
    }

    private void load(@NotNull ClassLoader classLoader) {
        AssertUtils.notNull(classLoader, "无效的类加载器");
        File file = JarUtils.copy(AssetUtils.asset("/service.yml"), AssetUtils.dataDirectory());
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
        file = JarUtils.copy(AssetUtils.asset("/private"), AssetUtils.dataDirectory());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] data = new byte[fileInputStream.available()];
            //noinspection ResultOfMethodCallIgnored
            fileInputStream.read(data);
            this.key = RsaUtils.generatePrivateKey(RsaUtils.importKey(data));
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
                ServerStarter starter = new ServerStarter(address, key, serviceManager, nThreads);
                ((StdServiceManager) serviceManager).setServiceInvoker(starter);
                ((StdServiceManager) serviceManager).setTimeout(timeout);
                serviceManager.register(ContentPublishService.class);
                serviceManager.register(ContentSubscribeService.class);
                this.watchdog = new Watchdog(starter, maximumRetry);
                watchdog.startup();
            }
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            if (serviceManager != null && watchdog != null) {
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

    public void publish(@NotNull String id, @NotNull String path) {
        lock.lock();
        try {
            startup();
            ContentSubscribeService contentSubscribeService = serviceManager.get(ContentSubscribeService.class);
            byte[] content = contentSubscribeService.subscribe(id, path);
            ContentPublishService contentPublishService = serviceManager.get(ContentPublishService.class);
            contentPublishService.publish(id, path, content);
        } catch (Throwable e) {
            logger.catching(e);
        } finally {
            lock.unlock();
        }
    }
}
