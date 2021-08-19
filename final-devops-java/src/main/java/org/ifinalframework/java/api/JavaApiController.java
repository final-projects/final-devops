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

package org.ifinalframework.java.api;

import lombok.extern.slf4j.Slf4j;
import org.ifinalframework.java.Decompiler;
import org.ifinalframework.java.Instrumentations;
import org.ifinalframework.java.Redefiner;
import org.ifinalframework.java.compiler.Compiler;
import org.springframework.web.bind.annotation.*;

import java.lang.instrument.Instrumentation;

/**
 * JavaApiController.
 *
 * @author likly
 * @version 1.2.2
 * @since 1.2.2
 */
@Slf4j
@RestController
@RequestMapping("/java")
public class JavaApiController {

    /**
     * return the java decompile content.
     *
     * @param clazz  target class
     * @param method target method, could be {@code null}.
     * @return the java decompile.
     */
    @GetMapping("/jad")
    public String jad(@RequestParam("class") Class<?> clazz, @RequestParam(value = "method", required = false) String method) {
        return Decompiler.decompile(clazz, method);
    }


    @PostMapping("/compile")
    public void compile(@RequestParam("class") Class<?> clazz, String source) {
        logger.info("compile class={},source={}", clazz, source);
        Instrumentation instrumentation = Instrumentations.get();

        ClassLoader classLoader = clazz.getClassLoader();

        Compiler compiler = new Compiler(classLoader);

        String className = clazz.getCanonicalName();
        compiler.addSource(className, source);

        compiler.compile();
    }

    @PostMapping("/replace")
    public void replace(@RequestParam("class") Class<?> clazz, String source) {
        logger.info("replace class={},source={}", clazz, source);
        Redefiner.redefine(clazz, source);
    }
}
