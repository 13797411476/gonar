package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;

/**
 * @author YeJin
 * @date 2020/1/9 16:27
 */
public interface GatewayRouterFilter {

    void filter(GatewayContext context, GatewayRouterFilterChain chain);

    int order();

}
