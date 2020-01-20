package cn.tandexue.tcpRouter.CmdService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class CmdHeartBeatHandler extends ChannelHandlerAdapter {
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("hello",
            CharsetUtil.UTF_8));
    private int loss_connect_time = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                System.out.println("CMD超过时间没有接收到消息：第" + loss_connect_time + "次");
                if (loss_connect_time > 1) ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        loss_connect_time = 0;
        ByteBuf in = (ByteBuf) msg;
        if (in.readableBytes() == 5) {
            byte[] recvBytes = new byte[in.readableBytes()];
            ByteBuf inClone = ((ByteBuf) msg).copy();
            inClone.readBytes(recvBytes);
            inClone.release();
            String recvString = new String(recvBytes);
            if (recvString.equals("hello")) {
                // ignore
                //System.out.println("心跳消息");
            } else {
                super.channelRead(ctx, msg);
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}