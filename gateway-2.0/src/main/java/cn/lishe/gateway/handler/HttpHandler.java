package cn.lishe.gateway.handler;

import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.http.ProxyRequest;
import cn.lishe.gateway.response.RespDTO;
import org.springframework.stereotype.Component;

/**
 * @author YeJin
 * @date 2020/1/9 17:42
 */
@Component
public class HttpHandler {

    private final ProxyRequest proxyRequest;

    public HttpHandler(ProxyRequest proxyRequest) {
        this.proxyRequest = proxyRequest;
    }

    public void doHandler(GatewayContext gatewayContext) {

        RespDTO respDTO = proxyRequest.request(gatewayContext.getRouter().getUrl(), gatewayContext.getRequest());
        gatewayContext.setRespDTO(respDTO);

    }

}
