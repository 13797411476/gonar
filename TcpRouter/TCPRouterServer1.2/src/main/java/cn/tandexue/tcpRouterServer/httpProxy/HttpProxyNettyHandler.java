package cn.tandexue.tcpRouterServer.httpProxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HttpProxyNettyHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private Logger logger;

    public HttpProxyNettyHandler() {
        this.logger = Logger.getLogger(this.getClass());
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        try {
            ByteBuf content = msg.content();
            byte[] bts = new byte[content.readableBytes()];
            content.readBytes(bts);
            String msgContent = new String(bts);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            HttpProxyRequestDTO httpProxyRequestDTO = null;
            HttpProxyResponseDTO httpProxyResponseDTO = new HttpProxyResponseDTO();
            try {
                httpProxyRequestDTO = mapper.readValue(msgContent, HttpProxyRequestDTO.class);
            } catch (IOException e) {
                logger.debug("Recv illegal:" + msgContent);
                httpProxyResponseDTO.setErrorCode(1);
                httpProxyResponseDTO.setErrorMsg("illegal Json Request:" + msgContent);
                reponse(ctx, httpProxyResponseDTO);
                return;
            }
            String type = httpProxyRequestDTO.getMethod();
            String contentResponse = null;
            if (type.equals("get")) {
                contentResponse = HttpRequestHandler.sendGet(
                        httpProxyRequestDTO.getUrl(),
                        httpProxyRequestDTO.getParam()
                );
            } else if (type.equals("post")) {
                contentResponse = HttpRequestHandler.sendPost(
                        httpProxyRequestDTO.getUrl(),
                        httpProxyRequestDTO.getParam()
                );
            } else {
                httpProxyResponseDTO.setErrorCode(2);
                httpProxyResponseDTO.setErrorMsg("error method:" + httpProxyRequestDTO.getMethod());
                reponse(ctx, httpProxyResponseDTO);
                return;
            }
            httpProxyResponseDTO.setErrorCode(0);
            httpProxyResponseDTO.setContent(contentResponse);
            reponse(ctx, httpProxyResponseDTO);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reponse(ChannelHandlerContext ctx, HttpProxyResponseDTO httpProxyResponseDTO) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            String respString = mapper.writeValueAsString(httpProxyResponseDTO);
            ByteBuf responseBuf = Unpooled.copiedBuffer(respString,CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set("content-Type","text/html;charset=UTF-8");
            response.content().writeBytes(responseBuf);
            responseBuf.release();
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (JsonProcessingException e) {
            logger.debug("can't send....");
        }
    }
}
