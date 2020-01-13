package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.handler.HttpHandler;
import org.springframework.stereotype.Component;

/**
 * @author YeJin
 * @date 2020/1/9 17:52
 */
@Component
public class LastFilter implements GatewayRouterFilter {

    private final HttpHandler httpHandler;

    public LastFilter(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {
        httpHandler.doHandler(context);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
