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

package team.idealstate.minecraft.contentpublisher.spigot.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.minecraft.contentpublisher.spigot.ContentPublisher;
import team.idealstate.minecraft.contentpublisher.spigot.bukkit.event.ContentSubscribeEvent;

/**
 * <p>ContentSubscribeListener</p>
 *
 * <p>创建于 2024/2/9 21:36</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentSubscribeListener implements Listener {

    private final ContentPublisher contentPublisher;

    public ContentSubscribeListener(@NotNull ContentPublisher contentPublisher) {
        AssertUtils.notNull(contentPublisher, "插件实例不允许为 null");
        this.contentPublisher = contentPublisher;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(ContentSubscribeEvent event) {
        byte[] content = contentPublisher.subscribe(event.getId(), event.getPath());
        event.setContent(content);
        event.setCancelled(true);
    }
}
