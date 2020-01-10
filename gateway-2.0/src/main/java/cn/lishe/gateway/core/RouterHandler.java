package cn.lishe.gateway.core;

import cn.lishe.gateway.filter.GatewayRouterFilter;
import cn.lishe.gateway.filter.GatewayRouterFilterChain;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        DecoderResult decoderResult = request.decoderResult();

        gatewayRouterFilters.sort(Comparator.comparingInt(GatewayRouterFilter::order));
        new GatewayRouterFilterChain(gatewayRouterFilters).filter(new GatewayContext(ctx, request));
    }
}
