package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.enums.FuseEnum;
import cn.lishe.gateway.enums.ResultCode;
import cn.lishe.gateway.response.RespDTO;
import org.springframework.stereotype.Component;

@Component
public class RefuseFilter implements GatewayRouterFilter {
    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {
        Router router = context.getRouter();
        if (router.getFuse().equals(FuseEnum.normal)) {
            chain.filter(context);
        } else {
            RespDTO respDTO = new RespDTO();
            respDTO.setCode(ResultCode.fusing.getCode());
            respDTO.setMsg(ResultCode.fusing.getMsg());
            context.setRespDTO(respDTO);
        }
    }

    @Override
    public int order() {
        return 20;
    }
}
