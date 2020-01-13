package cn.lishe.gateway.http;

import cn.lishe.gateway.response.CommonResponseConfig;
import cn.lishe.gateway.response.RespDTO;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author YeJin
 * @date 2019/11/12 16:11
 */
@Component
public class ProxyRequest {

    @Resource
    private ProxyGetRequest proxyGetRequest;
    @Resource
    private  ProxyPostRequest proxyPostRequest;

    public RespDTO request(String realUrl, FullHttpRequest request) {
        RespDTO result;
        HttpMethod method = request.method();
        if (method.equals(HttpMethod.GET)) {
            result = proxyGetRequest.doGet( realUrl,  request);
        } else if (method.equals(HttpMethod.POST)) {
            result = proxyPostRequest.doPost( realUrl,  request);
        } else {
            //other method,unsurpport
            result = new RespDTO();
            //error
            result.setStatus(2);
            result.setContent(CommonResponseConfig.UNSURPPORT_METHOD.getBytes());
        }
        return result;
    }

}
