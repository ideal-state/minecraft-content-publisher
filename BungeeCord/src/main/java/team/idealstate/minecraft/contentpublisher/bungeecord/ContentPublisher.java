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

import net.md_5.bungee.api.plugin.Plugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.api.service.WatchdogListener;
import team.idealstate.hyper.rpc.api.service.exception.UnregisteredServiceException;
import team.idealstate.minecraft.contentpublisher.bungeecord.event.ContentPublisherStartEvent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>ContentPublisher</p>
 *
 * <p>创建于 2024/2/7 3:31</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ContentPublisher extends Plugin {
    private static final Logger logger = LogManager.getLogger(ContentPublisher.class);
    private final WatchdogListener watchdogListener = new WatchdogListener() {
        @Override
        public void after(When when) {
            if (When.STARTUP.equals(when)) {
                getProxy().getPluginManager().callEvent(new ContentPublisherStartEvent());
            }
        }

        @Override
        public void unnaturalDeath() {
            logger.error("服务启动失败，即将关闭服务器");
            getProxy().stop("服务启动失败，即将关闭服务器");
        }
    };
    private final Lock lock = new ReentrantLock();
    private volatile ContentPublisherCommand contentPublisherCommand = null;
    private volatile ServiceHelper serviceHelper = null;

    @Override
    public void onEnable() {
        lock.lock();
        try {
            if (serviceHelper == null) {
                this.serviceHelper = new ServiceHelper(getClass().getClassLoader(), watchdogListener);
            }
            serviceHelper.startup();
            if (contentPublisherCommand == null) {
                this.contentPublisherCommand = new ContentPublisherCommand(this);
            }
            getProxy().getPluginManager().registerCommand(this, contentPublisherCommand);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onDisable() {
        lock.lock();
        try {
            getProxy().getPluginManager().unregisterCommand(contentPublisherCommand);
            if (serviceHelper != null) {
                serviceHelper.shutdown();
            }
        } finally {
            lock.unlock();
        }
    }

    void reload() {
        lock.lock();
        try {
            if (serviceHelper != null) {
                serviceHelper.reload(getClass().getClassLoader());
            } else {
                throw new IllegalStateException("服务不可用");
            }
        } finally {
            lock.unlock();
        }
    }

    void restart() {
        lock.lock();
        try {
            if (serviceHelper != null) {
                serviceHelper.restart();
            } else {
                throw new IllegalStateException("服务不可用");
            }
        } finally {
            lock.unlock();
        }
    }

    void publish(@NotNull String id, @NotNull String path) {
        lock.lock();
        try {
            if (serviceHelper != null) {
                serviceHelper.publish(id, path);
            } else {
                throw new IllegalStateException("服务不可用");
            }
        } catch (UnregisteredServiceException e) {
            logger.catching(e);
        } finally {
            lock.unlock();
        }
    }
}
