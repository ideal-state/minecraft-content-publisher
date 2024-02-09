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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.api.AssertUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>ContentPublisherCommand</p>
 *
 * <p>创建于 2024/2/7 6:06</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class ContentPublisherCommand implements TabExecutor {

    private static final List<String> ARGS = Arrays.asList("reload", "restart");
    private final ContentPublisher contentPublisher;

    ContentPublisherCommand(@NotNull ContentPublisher contentPublisher) {
        AssertUtils.notNull(contentPublisher, "插件实例不允许为 null");
        this.contentPublisher = contentPublisher;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "reload":
                    contentPublisher.reload();
                    return true;
                case "restart":
                    contentPublisher.restart();
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> collect = ARGS.stream().filter((arg) -> arg.startsWith(args[0])).collect(Collectors.toList());
            if (collect.isEmpty()) {
                return ARGS;
            }
            return collect;
        }
        return null;
    }
}
