package cn.lishe.gateway.filter;

import cn.hutool.core.util.RandomUtil;
import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.enums.ResultCode;
import cn.lishe.gateway.response.RespDTO;
import org.springframework.stereotype.Component;

@Component
public class ReqeustLimitFilter implements GatewayRouterFilter {

    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {
        int randomInt = RandomUtil.randomInt(0, 100);

        Integer healthy = context.getRouter().getHealthy();

        if (healthy >= randomInt) {
            chain.filter(context);
        } else {
            RespDTO respDTO = new RespDTO();
            respDTO.setCode(ResultCode.request_limit.getCode());
            respDTO.setMsg(ResultCode.request_limit.getMsg());
            context.setRespDTO(respDTO);
        }

    }

    @Override
    public int order() {
        return 10;
    }
}
