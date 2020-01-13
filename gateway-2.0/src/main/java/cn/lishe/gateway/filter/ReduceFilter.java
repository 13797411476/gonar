package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.enums.ReduceEnum;

public class ReduceFilter implements GatewayRouterFilter{

    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {
        Router router = context.getRouter();
        if(router.getReduce().equals(ReduceEnum.normal)) {
            chain.filter(context);
        }else {

        }
    }

    @Override
    public int order() {
        return 10;
    }
}
