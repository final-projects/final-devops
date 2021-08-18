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

import org.ifinalframework.devops.core.TestApplication;
import org.ifinalframework.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * CoreDebugApiControllerTest.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
class MethodApiControllerTest {

    @Resource
    private MockMvc mvc;

    @Test
    void debug() throws Exception {

        List<String> args = Arrays.asList("world");

        MvcResult mvcResult = mvc.perform(
                        get("/devops/methods/debug")
                                .param("class", HelloController.class.getCanonicalName())
                                .param("method", "hello")
                                .param("args", Json.toJson(args))
                                .accept(MediaType.APPLICATION_JSON)
                ) // 断言返回结果是json
                .andDo(print())
                .andReturn();// 得到返回结果

    }

}
