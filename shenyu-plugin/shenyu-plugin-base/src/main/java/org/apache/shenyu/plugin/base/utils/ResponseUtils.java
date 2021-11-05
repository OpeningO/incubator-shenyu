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

package org.apache.shenyu.plugin.base.utils;

import org.apache.shenyu.plugin.base.support.CachedBodyOutputMessage;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * ResponseUtils.
 */
public final class ResponseUtils {

    private static final String CHUNKED = "chunked";

    /**
     * create CachedBodyOutputMessage.
     *
     * @param exchange ServerWebExchange
     * @return CachedBodyOutputMessage.
     */
    public static CachedBodyOutputMessage newCachedBodyOutputMessage(final ServerWebExchange exchange) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        return new CachedBodyOutputMessage(exchange, headers);
    }

    /**
     * build client response with current response data.
     *
     * @param response current response
     * @param body current response body
     * @return the client respone
     */
    public static ClientResponse buildClientResponse(final ServerHttpResponse response,
                                                     final Publisher<? extends DataBuffer> body) {
        ClientResponse.Builder builder = ClientResponse.create(Objects.requireNonNull(response.getStatusCode()),
                ServerCodecConfigurer.create().getReaders());
        return builder.headers(headers -> headers.putAll(response.getHeaders())).body(Flux.from(body)).build();
    }

    /**
     * fix the body message.
     *
     * @param response current response
     * @param outputMessage cache message
     * @return fixed body message
     */
    public static Mono<DataBuffer> fixBodyMessage(final ServerHttpResponse response,
                                                  final CachedBodyOutputMessage outputMessage) {
        fixHeaders(response.getHeaders());
        return DataBufferUtils.join(outputMessage.getBody());
    }

    /**
     * release source.
     * @param outputMessage CachedBodyOutputMessage
     * @param throwable Throwable
     * @return Mono.
     */
    public static Mono<Void> release(final CachedBodyOutputMessage outputMessage, final Throwable throwable) {
        if (outputMessage.getCache()) {
            return outputMessage.getBody().map(DataBufferUtils::release).then(Mono.error(throwable));
        }
        return Mono.error(throwable);
    }

    /**
     * ChunkedHeader.
     *
     * @param headers headers.
     * @return chunked headers
     */
    public static HttpHeaders chunkedHeader(final HttpHeaders headers) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(headers);
        fixHeaders(httpHeaders);
        return httpHeaders;
    }

    /**
     * fix headers.
     *
     * @param httpHeaders the headers
     */
    private static void fixHeaders(final HttpHeaders httpHeaders) {
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, CHUNKED);
    }
}
