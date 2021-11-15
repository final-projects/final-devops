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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.Setter;
import org.ifinalframework.devops.core.model.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * LoggerApiController.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/logger")
public class LoggerApiController implements BeanFactoryAware, InitializingBean {

    private static final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    @Setter
    private BeanFactory beanFactory;

    private AutoConfigurationPackagesFilter autoConfigurationPackagesFilter;

    @GetMapping()
    public List<Logger> level() {
        return context.getLoggerList()
                .stream()
                .filter(it -> autoConfigurationPackagesFilter.test(it.getName()))
                .map(it -> new Logger(it.getName(), it.getEffectiveLevel().toString()))
                .collect(Collectors.toList());

    }

    /**
     * 设置日志级别
     *
     * @param name  名称
     * @param level 级别
     */
    @PostMapping
    public void level(String name, Level level) {
        context.getLogger(name).setLevel(level);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        autoConfigurationPackagesFilter = new AutoConfigurationPackagesFilter(
                AutoConfigurationPackages.get(beanFactory));
    }

    private static final class AutoConfigurationPackagesFilter implements Predicate<String> {

        private final List<String> packages;

        public AutoConfigurationPackagesFilter(final List<String> packages) {
            this.packages = packages;
        }

        @Override
        public boolean test(final String s) {

            return packages.stream().anyMatch(s::startsWith);

        }

    }

}
