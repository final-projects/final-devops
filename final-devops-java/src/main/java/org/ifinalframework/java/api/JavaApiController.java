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

import org.ifinalframework.java.Decompiler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * JavaApiController.
 *
 * @author likly
 * @version 1.2.2
 * @since 1.2.2
 */
@RestController
@RequestMapping("/devops/java")
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
}
