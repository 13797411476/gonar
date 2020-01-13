package cn.lishe.gateway.core;

import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.response.RespDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author YeJin
 * @date 2020/1/9 17:22
 */
public class GatewayContext {

    private ChannelHandlerContext ctx;
    private FullHttpRequest request;
    private long traceId;
    private Router router;
    private RespDTO respDTO;

    public GatewayContext(ChannelHandlerContext ctx, FullHttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }


    public RespDTO getRespDTO() {
        return respDTO;
    }

    public void setRespDTO(RespDTO respDTO) {
        this.respDTO = respDTO;
    }

    public long getTraceId() {
        return traceId;
    }

    public void setTraceId(long traceId) {
        this.traceId = traceId;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }
}
