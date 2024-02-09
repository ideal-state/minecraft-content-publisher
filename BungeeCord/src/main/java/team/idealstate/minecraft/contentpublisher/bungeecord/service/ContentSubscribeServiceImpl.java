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

package team.idealstate.minecraft.contentpublisher.bungeecord.service;

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.api.AssertUtils;
import team.idealstate.minecraft.contentpublisher.common.AssetUtils;
import team.idealstate.minecraft.contentpublisher.common.service.ContentSubscribeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * <p>ContentSubscribeServiceImpl</p>
 *
 * <p>创建于 2024/2/9 17:42</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentSubscribeServiceImpl implements ContentSubscribeService {

    @Override
    public byte @NotNull [] subscribe(@NotNull String id, @NotNull String path) {
        AssertUtils.notBlank(id, "无效的内容标识");
        AssertUtils.notBlank(path, "无效的内容路径");
        try {
            return Files.readAllBytes(new File(AssetUtils.dataDirectory(), id + "/" + path).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
