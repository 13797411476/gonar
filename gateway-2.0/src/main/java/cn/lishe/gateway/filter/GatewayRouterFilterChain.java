package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;

import java.util.List;

/**
 * @author YeJin
 * @date 2020/1/9 17:11
 */
public class GatewayRouterFilterChain {

    private List<GatewayRouterFilter> gatewayRouterFilters;

    private int index;

    public GatewayRouterFilterChain(List<GatewayRouterFilter> gatewayRouterFilters) {
        this.gatewayRouterFilters = gatewayRouterFilters;
        this.index = 0;
    }

    public GatewayRouterFilterChain(GatewayRouterFilterChain gatewayRouterFilterChainParent, int index) {
        this.gatewayRouterFilters = gatewayRouterFilterChainParent.getGatewayRouterFilters();
        this.index = index;
    }

    public void filter(GatewayContext gatewayContext) {
        List<GatewayRouterFilter> gatewayRouterFilters = this.gatewayRouterFilters;
        if (this.index < gatewayRouterFilters.size()) {
            GatewayRouterFilter filter = gatewayRouterFilters.get(this.index);
            GatewayRouterFilterChain chain = new GatewayRouterFilterChain(this, this.index + 1);
            filter.filter(gatewayContext, chain);
        }
    }

    public List<GatewayRouterFilter> getGatewayRouterFilters() {
        return gatewayRouterFilters;
    }

    public void setGatewayRouterFilters(List<GatewayRouterFilter> gatewayRouterFilters) {
        this.gatewayRouterFilters = gatewayRouterFilters;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
