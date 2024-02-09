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

package team.idealstate.minecraft.contentpublisher.common.service;

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.impl.service.annotation.Implementation;
import team.idealstate.hyper.rpc.impl.service.annotation.RemoteService;

/**
 * <p>ContentSubscribeService</p>
 *
 * <p>创建于 2024/2/9 17:41</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@RemoteService
@Implementation("team.idealstate.minecraft.contentpublisher.bungeecord.service.ContentSubscribeServiceImpl")
public interface ContentSubscribeService {

    byte @NotNull [] subscribe(@NotNull String id, @NotNull String path);
}
