package cn.tandexue.tcpRouter.CmdService;

import cn.tandexue.tcpRouter.DataService.DataClients;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CmdServiceHanlder extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端接受的消息: " + (String) msg);
        if (((String) msg).equals("mallocDataSocket")) {
            DataClients.createDataSocket(30);
        }
    }

    //
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("正在连接... ");
        super.channelActive(ctx);
        String msg = "#register#www.zinsoft.com#\r\n";
        final ByteBuf time = ctx.alloc().buffer(msg.length() * 2);
        time.writeBytes(msg.getBytes());
        final ChannelFuture f = ctx.writeAndFlush(time);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接关闭! ");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }


}