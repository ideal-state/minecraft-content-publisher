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

package team.idealstate.minecraft.contentpublisher.spigot.bukkit.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.idealstate.hyper.common.AssertUtils;

/**
 * <p>ContentSubscribeEvent</p>
 *
 * <p>
 * 该事件是同步的
 * </p>
 *
 * <p>创建于 2024/2/9 21:30</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentSubscribeEvent extends CancellableEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String id;
    private final String path;
    private byte[] content;

    public ContentSubscribeEvent(@NotNull String id, @NotNull String path) {
        super(false);
        AssertUtils.notBlank(id, "无效的内容标识");
        AssertUtils.notBlank(path, "无效的内容路径");
        this.id = id;
        this.path = path;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public byte @Nullable [] getContent() {
        return content;
    }

    public void setContent(byte @NotNull [] content) {
        AssertUtils.notNull(content, "无效的内容");
        this.content = content;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
