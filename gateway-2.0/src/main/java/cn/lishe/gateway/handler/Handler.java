package cn.lishe.gateway.handler;

import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.response.RespDTO;

public interface Handler {
    RespDTO handler(GatewayContext gatewayContext);
}
