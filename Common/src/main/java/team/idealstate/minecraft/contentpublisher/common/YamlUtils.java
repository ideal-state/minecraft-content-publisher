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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.rpc.api.AssertUtils;

import java.io.File;
import java.io.IOException;

/**
 * <p>YamlUtils</p>
 *
 * <p>创建于 2024/2/7 3:46</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class YamlUtils {

    private static final Logger logger = LogManager.getLogger(YamlUtils.class);
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = YAMLMapper.builder()
                .addModules(
                        new ParameterNamesModule(),
                        new Jdk8Module(),
                        new JavaTimeModule()
                ).build();
    }

    public static <T> T toBean(@NotNull String yamlStr, @NotNull Class<T> beanType) throws JsonProcessingException {
        AssertUtils.notBlank(yamlStr, "待转换的 yaml 字符串仅包含无效的内容");
        AssertUtils.notNull(beanType, "待转换目标 bean 的类型不能为 null");
        return OBJECT_MAPPER.readValue(yamlStr, beanType);
    }

    public static <T> T toBean(@NotNull File file, @NotNull Class<T> beanType) throws JsonProcessingException {
        AssertUtils.notNull(file, "待转换的文件不允许为 null");
        AssertUtils.notNull(beanType, "待转换目标 bean 的类型不能为 null");
        try {
            return OBJECT_MAPPER.readValue(file, beanType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull String toYaml(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }
}
