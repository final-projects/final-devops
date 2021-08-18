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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * RequestMappingInfoMvcService.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
public class RequestMappingInfoMvcService implements RequestMappingInfoService, InitializingBean {

    private static final Set<RequestMethod> DEFAULT_REQUEST_METHODS = Arrays.stream(RequestMethod.values()).collect(
            Collectors.toSet());

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final List<RequestPattern> requestPatterns = new LinkedList<>();

    public RequestMappingInfoMvcService(
            final RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public List<RequestPattern> requestPatterns() {
        return requestPatterns;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (final Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping
                .getHandlerMethods().entrySet()) {

            final RequestMappingInfo requestMappingInfo = entry.getKey();
            final HandlerMethod handlerMethod = entry.getValue();

            if (handlerMethod.hasMethodAnnotation(ResponseBody.class) || AnnotatedElementUtils
                    .isAnnotated(handlerMethod.getMethod().getDeclaringClass(), ResponseBody.class)) {

                final Set<String> patterns = requestMappingInfo.getPatternValues();
                final Set<RequestMethod> methods = getRequestMethods(requestMappingInfo);

                for (final String pattern : patterns) {
                    for (final RequestMethod method : methods) {

                        final RequestPattern requestPattern = new RequestPattern();

                        requestPattern.setName(Optional.ofNullable(requestMappingInfo.getName())
                                .orElse(handlerMethod.getMethod().getName()));

                        requestPattern.setPattern(pattern);
                        requestPattern.setMethod(method);

                        requestPatterns.add(requestPattern);
                    }

                }
            }

        }

        Collections.sort(requestPatterns);
    }

    private Set<RequestMethod> getRequestMethods(RequestMappingInfo requestMappingInfo) {
        if (CollectionUtils.isEmpty(requestMappingInfo.getMethodsCondition().getMethods())) {
            return DEFAULT_REQUEST_METHODS;
        }

        return requestMappingInfo.getMethodsCondition().getMethods();
    }

}
