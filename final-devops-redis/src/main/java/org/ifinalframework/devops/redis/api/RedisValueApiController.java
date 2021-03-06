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

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * RedisValueApiController.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/redis")
public class RedisValueApiController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/get")
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @GetMapping({"/mget", "/multiGet"})
    public List<String> get(List<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    @PostMapping("/set")
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

}
