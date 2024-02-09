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

package team.idealstate.minecraft.contentpublisher.spigot.service;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.api.AssertUtils;
import team.idealstate.minecraft.contentpublisher.common.service.ContentPublishService;
import team.idealstate.minecraft.contentpublisher.spigot.bukkit.event.ContentPublishEvent;

/**
 * <p>ContentServiceImpl</p>
 *
 * <p>创建于 2024/2/9 15:58</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentPublishServiceImpl implements ContentPublishService {

    @Override
    public void publish(@NotNull String id, @NotNull String path, byte @NotNull [] content) {
        AssertUtils.notBlank(id, "无效的内容标识");
        AssertUtils.notBlank(path, "无效的内容路径");
        AssertUtils.notNull(content, "无效的内容");
        Bukkit.getPluginManager().callEvent(new ContentPublishEvent(id, path, content));
    }
}
