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

package org.apache.shenyu.common.enums;

import org.apache.shenyu.common.constant.RpcConstants;
import org.apache.shenyu.common.exception.ShenyuException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RpcTypeEnum.
 */
public enum RpcTypeEnum {

    /**
     * Http rpc type enum.
     */
    HTTP(RpcConstants.HTTP, true),

    /**
     * Dubbo rpc type enum.
     */
    DUBBO(RpcConstants.DUBBO, true),

    /**
     * Sofa rpc type enum.
     */
    SOFA(RpcConstants.SOFA, true),

    /**
     * Tars rpc type enum.
     */
    TARS(RpcConstants.TARS, true),

    /**
     * Web socket rpc type enum.
     */
    WEB_SOCKET(RpcConstants.WEB_SOCKET, true),

    /**
     * springCloud rpc type enum.
     */
    SPRING_CLOUD(RpcConstants.SPRING_CLOUD, true),

    /**
     * motan.
     */
    MOTAN(RpcConstants.MOTAN, true),

    /**
     * grpc.
     */
    GRPC(RpcConstants.GRPC, true);

    private final String name;

    private final Boolean support;

    /**
     * all args constructor.
     *
     * @param name    name
     * @param support support
     */
    RpcTypeEnum(final String name, final Boolean support) {
        this.name = name;
        this.support = support;
    }

    /**
     * get name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * get support.
     *
     * @return support
     */
    public Boolean getSupport() {
        return support;
    }

    /**
     * acquire operator supports.
     *
     * @return operator support.
     */
    public static List<RpcTypeEnum> acquireSupports() {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support).collect(Collectors.toList());
    }

    /**
     * acquire operator support URI RPC type.
     *
     * @return operator support.
     */
    public static List<RpcTypeEnum> acquireSupportURIs() {
        return Arrays.asList(RpcTypeEnum.GRPC, RpcTypeEnum.HTTP, RpcTypeEnum.TARS, RpcTypeEnum.SPRING_CLOUD, RpcTypeEnum.DUBBO);
    }

    /**
     * acquire operator support Metadata RPC type.
     *
     * @return operator support.
     */
    public static List<RpcTypeEnum> acquireSupportMetadatas() {
        return Arrays.asList(RpcTypeEnum.HTTP, RpcTypeEnum.DUBBO, RpcTypeEnum.GRPC, RpcTypeEnum.SPRING_CLOUD, RpcTypeEnum.SOFA, RpcTypeEnum.TARS, RpcTypeEnum.MOTAN);
    }

    /**
     * acquireByName.
     *
     * @param name this is rpc type
     * @return RpcTypeEnum rpc type enum
     */
    public static RpcTypeEnum acquireByName(final String name) {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support && e.name.equals(name)).findFirst()
                .orElseThrow(() -> new ShenyuException(String.format(" this rpc type can not support %s", name)));
    }
}
