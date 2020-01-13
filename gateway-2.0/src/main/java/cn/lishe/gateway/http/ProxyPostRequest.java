package cn.lishe.gateway.http;

import cn.lishe.gateway.conf.HeaderConfig;
import cn.lishe.gateway.response.RespDTO;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author YeJin
 * @date 2019/11/12 16:15
 */
@Component
public class ProxyPostRequest {

    private static Logger lg = LoggerFactory.getLogger(ProxyPostRequest.class);

    @Resource
    private PostFormDataDecoder postFormDataDecoder;
    @Resource
    private ProxySendRequest proxySendRequest;

    public RespDTO doPost(String realUrl, FullHttpRequest request) {
        HttpHeaders headers = request.headers();

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
        decoder.offer(request);
        List<InterfaceHttpData> bodyHttpDatas = decoder.getBodyHttpDatas();
        if (!bodyHttpDatas.isEmpty()) {
            //Form data
            Map<String, Map> result = postFormDataDecoder.recvData(bodyHttpDatas);

            return proxyPostForm(realUrl, result.get("attributeMap"), result.get("fileMap"), headers);
        } else {
            //Raw data
            if (request.content().isReadable()) {
                try {
                    byte[] reqBytes = new byte[request.content().readableBytes()];
                    request.content().readBytes(reqBytes);
                    return proxyPostRaw(realUrl, reqBytes, headers);
                } catch (Exception e) {
                    lg.error("post 封装数据出现异常", e);
                    return RespDTO.error("系统繁忙101");
                }
            }
        }
        return proxyPostRaw(realUrl, headers);
    }

    private RespDTO proxyPostRaw(String realUrl, HttpHeaders headers) {
        HttpPost httpPost = new HttpPost(realUrl);
        return sendPost(httpPost, headers);
    }

    private RespDTO proxyPostRaw(String realUrl, byte[] rawData, HttpHeaders headers) {
        HttpPost httpPost = new HttpPost(realUrl);
        HttpEntity httpEntity = new ByteArrayEntity(rawData);
        httpPost.setEntity(httpEntity);
        return sendPost(httpPost, headers);
    }


    private RespDTO proxyPostForm(String realUrl, Map<String, String> attributeMap, Map<String, FileUpload> fileMap, HttpHeaders headers) {
        HttpPost httpPost = new HttpPost(realUrl);


        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(StandardCharsets.UTF_8);
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);
        //加上此行代码解决返回中文乱码问题
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (Map.Entry<String, FileUpload> uploadEntry : fileMap.entrySet()) {
            FileUpload fileUpload = uploadEntry.getValue();
            try {
                builder.addBinaryBody(uploadEntry.getKey(), fileUpload.get(), ContentType.parse(fileUpload.getContentType()), fileUpload.getFilename());
            } catch (IOException e) {
                lg.error("文件上传转发出现了IOException", e);
            }
        }

        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue());
        }

        HttpEntity build = builder.build();
        httpPost.setEntity(build);

        return sendPost(httpPost, headers);
    }

    private RespDTO sendPost(HttpPost httpPost, HttpHeaders headers) {
        headers.forEach(header -> {
            if (!HeaderConfig.ignoreHeaders.contains(header.getKey())) {
                httpPost.addHeader(header.getKey(), header.getValue());
            }
        });
        return proxySendRequest.execute(httpPost);
    }
}
