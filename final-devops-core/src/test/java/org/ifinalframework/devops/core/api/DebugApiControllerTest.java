package org.ifinalframework.devops.core.api;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.ifinalframework.devops.core.domain.Parameter;
import org.ifinalframework.json.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
class DebugApiControllerTest {

    @Resource
    private MockMvc mvc;

    @Test
    void debug() throws Exception {

        List<Parameter> args = new ArrayList<>();
        final Parameter parameter = new Parameter();
        parameter.setClazz(String.class);
        parameter.setValue("world");
        args.add(parameter);

        MvcResult mvcResult = mvc.perform(
            MockMvcRequestBuilders.get("/devops/debug")
                .param("clazz", HelloController.class.getCanonicalName())
                .param("method", "hello")
                .param("args", Json.toJson(args))
                .accept(MediaType.APPLICATION_JSON)
        ) // 断言返回结果是json
            .andReturn();// 得到返回结果

        MockHttpServletResponse response = mvcResult.getResponse();
        //拿到请求返回码
        int status = response.getStatus();
        //拿到结果
        String contentAsString = response.getContentAsString();

        System.err.println(status);
        System.err.println(contentAsString);

    }

}
