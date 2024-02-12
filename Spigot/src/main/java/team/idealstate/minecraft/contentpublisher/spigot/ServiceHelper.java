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

package team.idealstate.minecraft.contentpublisher.spigot;

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.api.service.WatchdogListener;
import team.idealstate.hyper.rpc.api.service.exception.UnregisteredServiceException;
import team.idealstate.minecraft.contentpublisher.common.AbstractServiceHelper;
import team.idealstate.minecraft.contentpublisher.common.service.ContentSubscribeService;

/**
 * <p>ServiceHelper</p>
 *
 * <p>创建于 2024/2/9 16:18</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class ServiceHelper extends AbstractServiceHelper {

    ServiceHelper(@NotNull ClassLoader classLoader, WatchdogListener watchdogListener) {
        super(KeyType.PUBLIC, StarterType.CLIENT, classLoader, watchdogListener);
    }

    public byte @NotNull [] subscribe(@NotNull String id, @NotNull String path) throws UnregisteredServiceException {
        lock.lock();
        try {
            if (isActive()) {
                ContentSubscribeService contentSubscribeService = serviceManager.get(ContentSubscribeService.class);
                return contentSubscribeService.subscribe(id, path);
            }
            throw new IllegalStateException("服务未启动");
        } finally {
            lock.unlock();
        }
    }
}
