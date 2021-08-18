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

import org.ifinalframework.devops.redis.model.RedisKey;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ScanOptions.ScanOptionsBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * RedisKeyApiController.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/redis")
public class RedisKeyApiController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/keys")
    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /**
     * 删除 Redis Key
     *
     * @param key
     * @return
     */
    @DeleteMapping
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 扫描 Redis Key
     *
     * @param pattern
     * @param count
     * @return
     */
    @GetMapping("/scan")
    @SuppressWarnings("unchecked")
    public List<RedisKey> scan(String pattern, @RequestParam(required = false) Integer count) {

        final ScanOptionsBuilder builder = ScanOptions.scanOptions();

        if (Objects.nonNull(pattern)) {
            builder.match(pattern);
        }
        if (Objects.nonNull(count)) {
            builder.count(count);
        }

        ScanOptions options = builder.build();
        Cursor<String> cursor = (Cursor<String>) stringRedisTemplate.executeWithStickyConnection(
                redisConnection -> new ConvertingCursor<>(redisConnection.scan(options),
                        stringRedisTemplate.getKeySerializer()::deserialize));

        List<RedisKey> result = new LinkedList<>();
        cursor.forEachRemaining(key -> result.add(new RedisKey(key, stringRedisTemplate.type(key))));
        return result;

    }

}
