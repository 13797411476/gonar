package cn.lishe.gateway.handler;

import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.response.CommonResponseConfig;
import cn.lishe.gateway.response.RespDTO;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class OptionRequestHandler implements Handler {

    @Override
    public RespDTO handler(GatewayContext gatewayContext) {
        FullHttpRequest request = gatewayContext.getRequest();
        if (request.method().name().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            return CommonResponseConfig.getRespDto(CommonResponseConfig.COMMAND_OK);
        }
        return null;
    }
}
