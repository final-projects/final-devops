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

package org.ifinalframework.devops.core.api;

import lombok.Setter;
import org.ifinalframework.context.exception.NotFoundException;
import org.ifinalframework.devops.core.function.MethodConvertFunction;
import org.ifinalframework.json.Json;
import org.ifinalframework.web.annotation.bind.RequestJsonParam;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 方法控制器.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/methods")
public class MethodApiController implements ApplicationContextAware {

    private static final MethodConvertFunction METHOD_CONVERT_FUNCTION = new MethodConvertFunction();

    @Setter
    private ApplicationContext applicationContext;

    /**
     * 方法查询
     *
     * @param clazz  类的全名称
     * @param method 方法名
     * @param types  参数类型列表
     * @return
     */
    @GetMapping
    public List<org.ifinalframework.devops.core.model.Method> findMethod(
            @RequestParam(value = "class") Class<?> clazz,
            @RequestParam(value = "method", required = false) String method,
            @RequestParam(value = "types", required = false) Class<?>[] types) {

        if (Objects.isNull(method)) {
            // return all methods
            return Arrays.stream(clazz.getMethods())
                    .filter(it -> !Object.class.equals(it.getDeclaringClass()))
                    .map(it -> METHOD_CONVERT_FUNCTION.apply(clazz, it))
                    .collect(Collectors.toList());

        } else {
            // find the methods of name
            final Method targetMethod = ReflectionUtils.findMethod(clazz, method, types);

            if (Objects.isNull(targetMethod)) {
                throw new NotFoundException("Not found method of {} in class {}", method, clazz);
            }

            return Collections.singletonList(METHOD_CONVERT_FUNCTION.apply(clazz, targetMethod));
        }

    }

    /**
     * 方法调试
     *
     * @param name   Spring Bean Name
     * @param clazz  Spring Bean clazz
     * @param method method name
     * @param types  method parameter types
     * @param args   method parameter args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping("/debug")
    public Object debug(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "class", required = false) Class<?> clazz,
            @RequestParam(value = "method") String method,
            @RequestJsonParam(value = "types", required = false) Class<?>[] types,
            @RequestJsonParam(value = "args", required = false) String[] args)
            throws InvocationTargetException, IllegalAccessException {

        try {

            if (Objects.isNull(name) && Objects.isNull(clazz)) {
                throw new IllegalArgumentException("required last one parameter of 'name' and 'class'");
            }

            // 1. find bean by name or class
            final Object bean =
                    Objects.nonNull(name) ? applicationContext.getBean(name) : applicationContext.getBean(clazz);

            // 2. if not define clazz then get from bean
            if (clazz == null) {
                clazz = AopUtils.getTargetClass(bean);
            }

            // 3. find method
            final Method methodTarget = ReflectionUtils.findMethod(clazz, method, types);

            ReflectionUtils.makeAccessible(Objects.requireNonNull(methodTarget));

            // 4. invoke the method
            if (Objects.isNull(args)) {
                return methodTarget.invoke(bean);
            } else {
                // 5. convert args
                final Type[] parameterTypes = methodTarget.getGenericParameterTypes();
                final AtomicInteger i = new AtomicInteger();

                final Object[] values = Arrays.stream(args)
                        .map(it -> Json.toObject(it, parameterTypes[i.getAndIncrement()]))
                        .toArray();

                return methodTarget.invoke(bean, values);
            }
        } finally {
            ReflectionUtils.clearCache();
        }
    }

}
