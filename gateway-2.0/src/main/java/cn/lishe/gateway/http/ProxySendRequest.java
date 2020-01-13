package cn.lishe.gateway.http;

import cn.lishe.gateway.response.RespDTO;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YeJin
 * @date 2019/11/12 16:19
 */
@Component
public class ProxySendRequest {

    private static RequestConfig requestConfig;

    static {
        requestConfig = RequestConfig.custom()
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(3000)
                //与服务器连接超时时间：httpclient会创建一个异步线程用以创建socket连接，此处设置该socket的连接超时时间
                .setConnectTimeout(3000)
                //socket读数据超时时间：从服务器获取响应数据的超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false)
                .build();
    }


    RespDTO execute(HttpRequestBase req) {
        return retry(req, 1);
    }

    private RespDTO retry(HttpRequestBase req, int retryCount) {
        RespDTO respDTO = new RespDTO();
        long timeStart = System.currentTimeMillis();

        CloseableHttpClient build = HttpClientBuilder.create()
                .setMaxConnTotal(50)
                .setDefaultRequestConfig(requestConfig)
                .build();
        try {
            CloseableHttpResponse execute = build.execute(req);
            int statusCode = execute.getStatusLine().getStatusCode();
            long useTime = System.currentTimeMillis() - timeStart;

            if (statusCode < 399) {
                HttpEntity entity = execute.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
                Map<String, String> headersMap = new HashMap<>();
                HeaderIterator headerIterator = execute.headerIterator();
                while (headerIterator.hasNext()) {
                    Header header = (Header) headerIterator.next();
                    headersMap.put(header.getName(), header.getValue());
                }

                respDTO.setCode(statusCode);
                respDTO.setStatus(0);
                respDTO.setContent(bytes);
                respDTO.setHeaders(headersMap);


            } else {
                respDTO.setCode(statusCode);
                respDTO.setStatus(statusCode);
            }

            respDTO.setUseTime(useTime);
        } catch (IOException e) {
            if (retryCount < 3) {
                respDTO = retry(req, ++retryCount);
            } else {
                respDTO.setStatus(1);
                long useTime = System.currentTimeMillis() - timeStart;
                respDTO.setUseTime(useTime);
            }
        }

        return respDTO;
    }


}
