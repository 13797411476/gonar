package com.lishe.gateway.core;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class GatewayEndPoint {

    @RequestMapping("/**")
    public Mono<Object> endpoint(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String value = request.getPath().value();

        System.out.println(value);

        HttpMethod method = request.getMethod();

        HttpHeaders headers = request.getHeaders();

        System.out.println(headers);

        Mono<MultiValueMap<String, Part>> multipartData = serverWebExchange.getMultipartData();
        System.out.println(multipartData);
        MediaType contentType = headers.getContentType();
        if (MediaType.APPLICATION_JSON.equals(contentType)) {
            Flux<DataBuffer> body = request.getBody();
            AtomicReference<String> bodyRef = new AtomicReference<>();
            body.subscribe(buffer -> {
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                DataBufferUtils.release(buffer);
                bodyRef.set(charBuffer.toString());
            });
            //获取request body
            String bodyStr = bodyRef.get();
            System.out.println(bodyStr);
        }

        return Mono.just("aaaaa");
    }

}
