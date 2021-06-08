package org.ifinalframework.devops.core.api;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.ifinalframework.devops.core.domain.Parameter;
import org.ifinalframework.json.Json;
import org.ifinalframework.web.annotation.bind.RequestJsonParam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Setter;

/**
 * CoreDebugApiController.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/devops/debug")
public class DebugApiController implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @RequestMapping()
    public Object debug(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "class", required = false) Class<?> clazz,
        @RequestParam(value = "method") String methodName,
        @RequestJsonParam(required = false) List<Parameter> args)
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
            final Method method = CollectionUtils.isEmpty(args)
                ? ReflectionUtils.findMethod(clazz, methodName)
                : ReflectionUtils
                    .findMethod(clazz, methodName, args.stream().map(Parameter::getClazz).toArray(Class[]::new));

            ReflectionUtils.makeAccessible(Objects.requireNonNull(method));

            // 4. invoke the method
            if (CollectionUtils.isEmpty(args)) {
                return method.invoke(bean);
            } else {

                final Type[] parameterTypes = method.getGenericParameterTypes();
                final AtomicInteger i = new AtomicInteger();

                final Object[] values = args.stream()
                    .map(it -> Json.toObject(it.getValue(), parameterTypes[i.getAndIncrement()]))
                    .toArray();

                return method.invoke(bean, values);
            }
        } finally {
            ReflectionUtils.clearCache();
        }
    }

}
