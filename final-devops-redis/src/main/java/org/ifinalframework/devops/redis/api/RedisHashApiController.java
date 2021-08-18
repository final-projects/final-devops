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

package org.ifinalframework.devops.redis.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * RedisHashApiController.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/redis")
public class RedisHashApiController implements InitializingBean {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private HashOperations<String, String, String> hashOperations;

    @GetMapping({"/hget", "/get"})
    public String get(String key, String field) {
        return hashOperations.get(key, field);
    }

    @PostMapping({"/hset", "/put"})
    public void put(String key, String field, String value) {
        hashOperations.put(key, field, value);
    }

    @GetMapping({"/hgetall", "/entries"})
    public Map<String, String> entries(String key) {
        return hashOperations.entries(key);
    }

    @GetMapping({"hlen", "/size"})
    public Long size(String key) {
        return hashOperations.size(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        hashOperations = stringRedisTemplate.opsForHash();
    }

}
