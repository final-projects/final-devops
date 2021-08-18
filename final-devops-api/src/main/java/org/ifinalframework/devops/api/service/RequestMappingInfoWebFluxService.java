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

package org.ifinalframework.devops.api.service;

import org.ifinalframework.devops.api.model.RequestPattern;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import java.util.List;

/**
 * RequestMapingInfoWebFluxService.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class RequestMappingInfoWebFluxService implements RequestMappingInfoService {

    private final List<RequestMappingInfoHandlerMapping> requestMappingInfoHandlerMappings;

    public RequestMappingInfoWebFluxService(
            final List<RequestMappingInfoHandlerMapping> requestMappingInfoHandlerMappings) {
        this.requestMappingInfoHandlerMappings = requestMappingInfoHandlerMappings;
    }

    @Override
    public List<RequestPattern> requestPatterns() {
        return null;
    }

}
