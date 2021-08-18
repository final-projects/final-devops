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

package org.ifinalframework.devops.core.function;

import org.ifinalframework.devops.core.model.Parameter;
import org.ifinalframework.javadoc.model.ClassDoc;
import org.ifinalframework.javadoc.model.MethodDoc;
import org.ifinalframework.javadoc.model.ParameterDoc;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MethodConvertFunction.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class MethodConvertFunction implements
        BiFunction<Class<?>, Method, org.ifinalframework.devops.core.model.Method> {

    @Override
    public org.ifinalframework.devops.core.model.Method apply(final Class<?> clazz, final Method targetMethod) {

        final Class<?> declaringClass = targetMethod.getDeclaringClass();

        final ClassDoc doc = ClassDoc.load(declaringClass);

        final Optional<MethodDoc> methodDoc = Optional.ofNullable(doc).map(it -> it.getMethod(targetMethod));

        final Map<String, ParameterDoc> parameterDocs = methodDoc.map(
                        it -> it.getParameterDocs().stream().collect(Collectors.toMap(ParameterDoc::getName, Function.identity())))
                .orElse(
                        Collections.emptyMap());

        final List<Parameter> parameters = Arrays.stream(targetMethod.getParameters())
                .map(it -> Parameter.builder()
                        .name(it.getName())
                        .type(it.getType())
                        .summary(
                                Optional.ofNullable(parameterDocs.get(it.getName())).map(ParameterDoc::getSummary)
                                        .orElse(it.getName())
                        ).build())
                .collect(Collectors.toList());

        return org.ifinalframework.devops.core.model.Method.builder()
                .clazz(clazz)
                .name(targetMethod.getName())
                .summary(methodDoc.map(MethodDoc::getSummary).orElse(targetMethod.getName()))
                .parameters(parameters)
                .signature(targetMethod.toGenericString())
                .build();
    }

}
