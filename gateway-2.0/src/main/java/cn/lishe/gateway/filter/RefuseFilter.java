package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;

public class RefuseFilter implements GatewayRouterFilter {
    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {

    }

    @Override
    public int order() {
        return 20;
    }
}
