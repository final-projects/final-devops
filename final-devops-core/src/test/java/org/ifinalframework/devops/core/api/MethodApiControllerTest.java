package org.ifinalframework.devops.core.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.ifinalframework.json.Json;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

/**
 * CoreDebugApiControllerTest.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootTest
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
