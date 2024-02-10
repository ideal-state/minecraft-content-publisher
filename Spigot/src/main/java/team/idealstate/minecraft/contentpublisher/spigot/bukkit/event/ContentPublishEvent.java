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
import team.idealstate.hyper.common.AssertUtils;

/**
 * <p>ContentPublishEvent</p>
 *
 * <p>
 * 该事件是异步的
 * </p>
 *
 * <p>创建于 2024/2/9 18:29</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentPublishEvent extends CancellableEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String id;
    private final String path;
    private final byte[] content;

    public ContentPublishEvent(@NotNull String id, @NotNull String path, byte @NotNull [] content) {
        super(true);
        AssertUtils.notBlank(id, "无效的内容标识");
        AssertUtils.notBlank(path, "无效的内容路径");
        AssertUtils.notNull(content, "无效的内容");
        this.id = id;
        this.path = path;
        this.content = content;
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

    public byte[] getContent() {
        byte[] copied = new byte[content.length];
        System.arraycopy(content, 0, copied, 0, content.length);
        return copied;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
