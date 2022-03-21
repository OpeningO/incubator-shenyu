/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.plugin.cache.base.redis;

import org.apache.shenyu.common.enums.RedisModeEnum;
import org.apache.shenyu.plugin.cache.base.config.CacheConfig;
import org.apache.shenyu.plugin.cache.base.enums.CacheEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RedisCacheTest
 */
@ExtendWith(MockitoExtension.class)
public class RedisCacheTest {

    private RedisCache shenyuCacheRedisTemplate;

    @Before
    public void prepare() {
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setMode(RedisModeEnum.STANDALONE.getName());
        cacheConfig.setUrl("shenyu-redis:6379");
        cacheConfig.setCacheType(CacheEnum.REDIS.getName());
        final RedisConnectionFactory redisConnectionFactory = new RedisConnectionFactory(cacheConfig);
        shenyuCacheRedisTemplate = new RedisCache(redisConnectionFactory.getLettuceConnectionFactory());
    }

    @Test
    public void testRedisCache() {
        final String testKey = "testRedisCache";
        assertEquals(Boolean.FALSE, shenyuCacheRedisTemplate.isExist(testKey));
        boolean flag = shenyuCacheRedisTemplate.cacheData(testKey, testKey.getBytes(StandardCharsets.UTF_8), 1000);
        assertEquals(Boolean.TRUE, flag);
        assertEquals(Boolean.TRUE, shenyuCacheRedisTemplate.isExist(testKey));
        final byte[] value = shenyuCacheRedisTemplate.getData(testKey);
        assert null != value;
        assertEquals(testKey, new String(value, StandardCharsets.UTF_8));

    }
}
