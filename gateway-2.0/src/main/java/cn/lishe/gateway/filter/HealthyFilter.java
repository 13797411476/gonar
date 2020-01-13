package cn.lishe.gateway.filter;

import cn.hutool.core.util.RandomUtil;
import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.enums.ReduceEnum;
import cn.lishe.gateway.enums.ResultCode;
import cn.lishe.gateway.response.RespDTO;
import org.springframework.stereotype.Component;

@Component
public class HealthyFilter implements GatewayRouterFilter {


    /**
     * 当某个接口被降级之后, 每个10分钟会在自动恢复一点健康值
     */
    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {

        Router router = RequestCache.getRouter(context.getRequest().uri().toLowerCase());

        Integer healthy = router.getHealthy();
        if (healthy == 0) {
            Long aLong = RequestCache.reduingMap.get(context.getRequest().uri().toLowerCase());
            long now = System.currentTimeMillis();
            if (aLong == null) {
                aLong = now;
            }

            if (now - aLong >= 600000) {
                healthy += 20;
                RequestCache.reduingMap.remove(context.getRequest().uri().toLowerCase());
                router.setHealthy(healthy);
                router.setReduce(ReduceEnum.normal);
            }
        }

        chain.filter(context);
    }

    @Override
    public int order() {
        return 10;
    }
}
