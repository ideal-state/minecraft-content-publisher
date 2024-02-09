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

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.api.AssertUtils;

/**
 * <p>ContentPublisher</p>
 *
 * <p>创建于 2024/2/7 6:06</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class ContentPublisherCommand extends Command {

    private final ContentPublisher contentPublisher;

    ContentPublisherCommand(@NotNull ContentPublisher contentPublisher) {
        super("ContentPublisher-Bungeecord", "bungeecord.command.end");
        AssertUtils.notNull(contentPublisher, "插件实例不允许为 null");
        this.contentPublisher = contentPublisher;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "reload":
                    contentPublisher.reload();
                    break;
                case "restart":
                    contentPublisher.restart();
                    break;
            }
        }
        if (args.length == 3) {
            if ("publish".equals(args[0])) {
                contentPublisher.publish(args[1], args[2]);
            }
        }
    }
}
