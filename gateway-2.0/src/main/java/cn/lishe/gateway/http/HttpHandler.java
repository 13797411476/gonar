package cn.lishe.gateway.http;

import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.entity.Router;
import org.springframework.stereotype.Component;

/**
 * @author YeJin
 * @date 2020/1/9 17:42
 */
@Component
public class HttpHandler {


    public void doHandler(GatewayContext gatewayContext) {
        String uri = gatewayContext.getRequest().uri().toLowerCase();

    }

}
