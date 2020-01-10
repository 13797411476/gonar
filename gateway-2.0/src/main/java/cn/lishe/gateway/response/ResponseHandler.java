package cn.lishe.gateway.response;

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


    public void reply(ChannelHandlerContext ctx, RespDTO respDTO) {
        this.writeResponseString(ctx, respDTO);
        ctx.channel().close();
        ctx.close();
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
        HttpResponseStatus httpResponseStatus = (respDTO.getCode() == 302 || respDTO.getCode() == 301) ? HttpResponseStatus.MOVED_PERMANENTLY : HttpResponseStatus.OK;
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
        response.headers().set("aa", "中文");
        response.headers().set("Access-Control-Allow-Credentials", "true");
        response.headers().set("Access-Control-Allow-Headers",
                "Accept,Origin,X-Requested-With,Content-Type,Last-Modified,device,token,access-control-allow-origin");
        ctx.channel().writeAndFlush(response);
        try {
            buf.release();
        }catch (Exception e) {
        }

    }
}
