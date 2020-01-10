package cn.lishe.gateway.handler;

import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.response.CommonResponseConfig;
import cn.lishe.gateway.response.RespDTO;
import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler implements Handler {

    @Override
    public RespDTO handler(GatewayContext gatewayContext) {
        FullHttpRequest request = gatewayContext.getRequest();
        String uri = request.uri();
        if ("/flushApi".equals(uri)) {
            //flush api

            return CommonResponseConfig.getRespDto(CommonResponseConfig.COMMAND_OK);
        }
        if ("/reportStatus".equals(uri)) {
            //flush local to sql
            return CommonResponseConfig.getRespDto(CommonResponseConfig.COMMAND_OK);
        }
        if ("/showAllApis".equals(uri)) {
            //show all apis in localCache
            return CommonResponseConfig.getRespDto(JSON.toJSONString(RequestCache.routerMap));
        }
        if ("/resetApi".equals(uri)) {
            return CommonResponseConfig.getRespDto(CommonResponseConfig.COMMAND_OK);
        }

        return null;
    }

}
