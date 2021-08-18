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

package org.ifinalframework.devops.api.model;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

/**
 * RequestPattern.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class RequestPattern implements Comparable<RequestPattern>, Serializable {

    /**
     * @see RequestMethod#name()
     */
    @Nullable
    private String name;

    private String pattern;

    private RequestMethod method;

    @Override
    public int compareTo(final RequestPattern o) {

        final int compare = pattern.compareTo(o.getPattern());

        if (compare == 0) {
            return method.compareTo(o.getMethod());
        }

        return compare;
    }

}
