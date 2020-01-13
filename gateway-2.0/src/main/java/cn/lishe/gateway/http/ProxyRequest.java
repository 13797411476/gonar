package cn.lishe.gateway.http;

import cn.lishe.gateway.enums.ResultCode;
import cn.lishe.gateway.response.CommonResponseConfig;
import cn.lishe.gateway.response.RespDTO;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


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
            //other method not support
            result = new RespDTO();
            result.setCode(ResultCode.http_method_not_support.getCode());
            result.setMsg(ResultCode.http_method_not_support.getMsg());
            result.setContent(CommonResponseConfig.UNSURPPORT_METHOD.getBytes());
        }
        return result;
    }

}
