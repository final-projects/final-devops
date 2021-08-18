/*
 * Copyright 2020-2021 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ifinalframework.devops.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * Method.
 *
 * @author likly
 * @version 1.0.0
 * @see java.lang.reflect.Method
 * @since 1.0.0
 */
@Getter
@Builder
public class Method implements Serializable {

    /**
     * 方法声明的类
     */
    @NonNull
    @JsonProperty("class")
    private final Class<?> clazz;

    /**
     * 方法名称
     */
    @NonNull
    private final String name;

    /**
     * 方法概要
     */
    @Nullable
    private final String summary;

    /**
     * 参数列表
     */
    @NonNull
    private final List<Parameter> parameters;

    /**
     * 方法签名
     */
    @NonNull
    private final String signature;

}
