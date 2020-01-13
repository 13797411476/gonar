package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.enums.ReduceEnum;
import cn.lishe.gateway.enums.ResultCode;
import cn.lishe.gateway.response.RespDTO;
import org.springframework.stereotype.Component;

@Component
public class ReduceFilter implements GatewayRouterFilter{

    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {
        Router router = context.getRouter();
        if(router.getReduce().equals(ReduceEnum.normal)) {
            chain.filter(context);
        }else {
            RespDTO respDTO = new RespDTO();
            respDTO.setCode(ResultCode.reducing.getCode());
            respDTO.setMsg(ResultCode.reducing.getMsg());
            context.setRespDTO(respDTO);
        }
    }

    @Override
    public int order() {
        return 30;
    }
}
