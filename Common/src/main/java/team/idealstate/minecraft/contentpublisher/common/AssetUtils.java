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

package team.idealstate.minecraft.contentpublisher.common;

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.common.AssertUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * <p>AssetUtils</p>
 *
 * <p>创建于 2024/2/9 16:22</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AssetUtils {

    private static final Map<Class<?>, String> NAME_CACHE = new HashMap<>(16, 0.6F);
    private static final Map<Class<?>, File> FILE_CACHE = new HashMap<>(16, 0.6F);

    @NotNull
    public static String asset(@NotNull String assetName) {
        return asset(AssetUtils.class, assetName);
    }

    @NotNull
    public static String asset(@NotNull Class<?> sourceClass, @NotNull String assetName) {
        AssertUtils.notNull(sourceClass, "无效的来源类型");
        AssertUtils.notBlank(assetName, "无效的资源名称");
        String name = getName(sourceClass);
        return "/assets/" + name + assetName;
    }

    @NotNull
    public static File dataDirectory() {
        return dataDirectory(AssetUtils.class);
    }

    @NotNull
    public static File dataDirectory(@NotNull Class<?> sourceClass) {
        AssertUtils.notNull(sourceClass, "无效的来源类型");
        File file = getFile(sourceClass);
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
        return file;
    }

    private static String getName(Class<?> sourceClass) {
        String name = NAME_CACHE.get(sourceClass);
        if (name == null) {
            URI uri;
            try {
                uri = sourceClass.getProtectionDomain().getCodeSource().getLocation().toURI();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            try (JarFile jarFile = new JarFile(new File(uri))) {
                Manifest manifest = jarFile.getManifest();
                if (manifest != null) {
                    Attributes mainAttributes = manifest.getMainAttributes();
                    if (mainAttributes != null) {
                        name = mainAttributes.getValue("Name");
                        if (name != null) {
                            NAME_CACHE.put(sourceClass, name);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("无效的工件名称");
        }
        return name;
    }

    private static File getFile(Class<?> sourceClass) {
        File file = FILE_CACHE.get(sourceClass);
        if (file == null) {
            file = new File("./plugins/" + getName(sourceClass));
            FILE_CACHE.put(sourceClass, file);
        }
        return file;
    }
}
