package cn.tandexue.tcpRouter.DataService;

import cn.tandexue.tcpRouter.localService.LocalService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.net.Socket;

public class DataServiceHanlder extends ChannelHandlerAdapter {

    private int port;

    private Logger logger;

    public DataServiceHanlder() {
        this.port = 0;
        logger = Logger.getLogger(this.getClass());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("newDataSocketWorking....");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        //logger.debug("DataSocket收到数据" + in.readableBytes());
        System.out.print( "#");
        if (port == 0) {
            byte[] recvBytes = new byte[in.readableBytes()];
            in.readBytes(recvBytes);
            String portInfoStr = new String(recvBytes);
            String[] portInfos = portInfoStr.split("#");
            if (portInfos.length != 3 || !portInfos[1].equals("port")) {
                System.out.println("不是端口上报数据" + portInfos.length);
                return;
            }
            port = Integer.parseInt(portInfos[2]);
            logger.debug("dataSocket 端口号接收成功：" + port);
            String msgReply = "#port#ack#";
            final ByteBuf time = ctx.alloc().buffer(msgReply.length() * 2);
            time.writeBytes(msgReply.getBytes());
            final ChannelFuture f = ctx.writeAndFlush(time);
            LocalService localService = new LocalService(port, ctx);
            localService.start();
            return;
        }
        ChannelHandlerContext localCtx = null;
        int retryTimes = 10;
        while ((localCtx = DataClients.getLocalSocketByDataSocket(ctx)) == null && retryTimes > 0) {
            retryTimes--;
            System.out.println("noLocalCtx");
            Thread.sleep(300);
        }
        //System.out.println("LocalPort:"+localCtx);
        if (localCtx == null) {
            logger.debug("localPort打开失败");
            DataClients.removeMapsByDataSocket(ctx);
            ctx.channel().close();
        }
        ChannelFuture f = localCtx.writeAndFlush(in);
//        try {
//            f.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (f.isSuccess()) {
//            System.out.println("writeLocal成功");
//        } else {
//            System.out.println("writeLocal失败" + f.cause());
//        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("dataSocketClosedFirst");
        DataClients.removeMapsByDataSocket(ctx);
        super.channelInactive(ctx);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }


}