package cn.lishe.gateway.core;

import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.filter.GatewayRouterFilter;
import cn.lishe.gateway.filter.GatewayRouterFilterChain;
import cn.lishe.gateway.response.CommonResponseConfig;
import cn.lishe.gateway.response.ResponseHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * @author YeJin
 * @date 2020/1/9 15:03
 */
@Component
@ChannelHandler.Sharable
public class RouterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Autowired
    private List<GatewayRouterFilter> gatewayRouterFilters;
    @Autowired
    private ResponseHandler responseHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // request decoder error
        if(!request.decoderResult().isSuccess()) {
            responseHandler.reply(ctx, CommonResponseConfig.getRespDto(CommonResponseConfig.DECODE_ERROR));
        }

        GatewayContext gatewayContext = new GatewayContext(ctx, request);
        gatewayContext.setTraceId(ctx.hashCode());
        gatewayContext.setRouter(getRouter(request.uri()));

        gatewayRouterFilters.sort(Comparator.comparingInt(GatewayRouterFilter::order));
        new GatewayRouterFilterChain(gatewayRouterFilters).filter(gatewayContext);

    }

    private Router getRouter(String uri) {
        return RequestCache.routerMap.get(uri.toLowerCase());
    }
}
