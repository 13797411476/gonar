package cn.lishe.gateway.core;

import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.enums.ResultCode;
import cn.lishe.gateway.filter.GatewayRouterFilter;
import cn.lishe.gateway.filter.GatewayRouterFilterChain;
import cn.lishe.gateway.response.CommonResponseConfig;
import cn.lishe.gateway.response.RespDTO;
import cn.lishe.gateway.response.ResponseHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;

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
        if (!request.decoderResult().isSuccess()) {
            responseHandler.reply(ctx, CommonResponseConfig.getRespDto(CommonResponseConfig.DECODE_ERROR));
        }

        GatewayContext gatewayContext = new GatewayContext(ctx, request);
        gatewayContext.setTraceId(ctx.hashCode());
        gatewayContext.setRouter(RequestCache.getRouter(request.uri()));

        gatewayRouterFilters.sort(Comparator.comparingInt(GatewayRouterFilter::order));
        new GatewayRouterFilterChain(gatewayRouterFilters).filter(gatewayContext);

        RespDTO respDTO = gatewayContext.getRespDTO();
        if(respDTO.getCode() == ResultCode.success.getCode()) {
            writeResponseString(ctx, respDTO);
        }

    }



    /**
     * 封装应答的回写
     *
     * @param ctx
     * @param respDTO
     */
    private void writeResponseString(ChannelHandlerContext ctx, RespDTO respDTO) {
        if (null == respDTO.getContent() || respDTO.getContent().length < 1) {
            respDTO.setContent(CommonResponseConfig.DEFAULT_VALUE.getBytes());
        }

        ByteBuf buf = copiedBuffer(respDTO.getContent());
        HttpResponseStatus httpResponseStatus = (respDTO.getCode() == ResultCode.http_redirect.getCode()) ? HttpResponseStatus.MOVED_PERMANENTLY : HttpResponseStatus.OK;
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, httpResponseStatus, buf);
        if (null != respDTO.getHeaders()) {
            for (Map.Entry<String, String> entry :
                    respDTO.getHeaders().entrySet()) {
                response.headers().set(entry.getKey(), entry.getValue());
            }
        }
        response.headers().set("content-length", buf.readableBytes() + "");
        response.headers().set("Access-Control-Allow-Origin", "*");
        response.headers().set("X-Powered-By", "ASP.NET");
        response.headers().set("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,DELETE,OPTIONS");
        response.headers().set("Access-Control-Max-Age", "3600");
        response.headers().set("Access-Control-Allow-Credentials", "true");
        response.headers().set("Access-Control-Allow-Headers",
                "Accept,Origin,X-Requested-With,Content-Type,Last-Modified,device,token,access-control-allow-origin");
        ctx.channel().writeAndFlush(response);
        try {
            buf.release();
        } catch (Exception e) {
        }

    }
}
