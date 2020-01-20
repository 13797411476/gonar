package cn.tandexue.tcpRouterServer.dataService;

import cn.tandexue.tcpRouterServer.dataService.DataClients;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import javafx.scene.chart.PieChart;
import org.apache.log4j.Logger;

public class DataHeartBeatClientHandler extends ChannelHandlerAdapter {

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("hello",
            CharsetUtil.UTF_8));
    private int loss_connect_time = 0;

    private Logger logger;

    DataHeartBeatClientHandler() {
        logger = Logger.getLogger(DataHeartBeatClientHandler.class);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        int dataSocketStatus = DataClients.getDataSocketStatus(ctx);
        if (1 == dataSocketStatus) {
            //如果 dataSocket 是新的（缓存中的）则启用服务端心跳检测机制
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.WRITER_IDLE) {
                    //如果这个dataSocket是新的，未被使用过，写超时 30s 则发送 hello 心跳
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                    logger.debug("DataSocket发送心跳-"+ctx);
                } else if (event.state() == IdleState.READER_IDLE) {
                    //如果dataSocket已经被使用中， 则 读超时 33s * 1 次断开
                    loss_connect_time++;
                    if (loss_connect_time > 0) {
                        logger.debug("空闲的DataSocket超过33s*1次未响应，关闭它");
                        ctx.close();
                    }
                }
            }
        } else if (2 == dataSocketStatus) {
            //如果 dataSocket 已经被使用中，则 读超时 33s * 1 次 断开
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                if (loss_connect_time > 4) {
                    logger.debug("使用中的DataSocket超过33s*5次未响应，关闭它");
                    ctx.close();
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        loss_connect_time = 0;
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