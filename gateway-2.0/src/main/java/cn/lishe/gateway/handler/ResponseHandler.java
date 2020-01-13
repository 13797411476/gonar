package cn.lishe.gateway.handler;

import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.core.GatewayContext;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.enums.ReduceEnum;
import cn.lishe.gateway.enums.ResultCode;
import cn.lishe.gateway.response.RespDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;

@Component
public class ResponseHandler {


    private void response(GatewayContext gatewayContext) {
        RespDTO respDTO = gatewayContext.getRespDTO();
        if (respDTO != null) {
            if (ResultCode.success.getCode() == respDTO.getCode()) {
                ok(gatewayContext.getCtx(), respDTO.getHeaders(), respDTO.getContent());
            } else if (ResultCode.http_redirect.getCode() == respDTO.getCode()) {
                redirect(gatewayContext.getCtx(), respDTO.getHeaders(), respDTO.getContent());
            }else {
                errorReply(gatewayContext);
            }
        }

    }

    private void errorReply(GatewayContext gatewayContext) {

        Router router = RequestCache.getRouter(gatewayContext.getRequest().uri().toLowerCase());
        Integer healthy = router.getHealthy();
        healthy = healthy - 30;
        if(healthy < 0) {
            healthy = 0;
        }

        if(healthy == 0) {
            router.setReduce(ReduceEnum.reducing);
            RequestCache.reduingMap.put(gatewayContext.getRequest().uri().toLowerCase(), System.currentTimeMillis());
        }else {
            router.setReduce(ReduceEnum.normal);
        }

    }

    private void redirect(ChannelHandlerContext ctx, Map<String, String> headers, byte[] content) {
        write(ctx, HttpResponseStatus.MOVED_PERMANENTLY, headers, content);
    }

    private void ok(ChannelHandlerContext ctx, Map<String, String> headers, byte[] content) {
        write(ctx, HttpResponseStatus.OK, headers, content);
    }

    private void write(ChannelHandlerContext ctx, HttpResponseStatus httpResponseStatus, Map<String, String> headers, byte[] content) {
        ByteBuf buf = copiedBuffer(content);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus, buf);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
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
            e.printStackTrace();
        }
    }


}
