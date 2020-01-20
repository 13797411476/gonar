package cn.tandexue.tcpRouterServer.dataService;


import cn.tandexue.tcpRouterServer.portService.PortClients;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;


import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class DataServerHandler extends ChannelHandlerAdapter {
    private Logger logger;

    private Boolean portAct;

    private Boolean Used;

    private Vector<ChannelFuture> futureList;

    public DataServerHandler() {
        this.logger = Logger.getLogger(this.getClass());
        portAct = false;
        Used = false;
        futureList = new Vector<ChannelFuture>();
    }

    public Boolean getPortAct() {
        return portAct;
    }

    public Boolean getUsed() {
        return Used;
    }

    public void setUsed(Boolean used) {
        Used = used;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        DataClients.addSocket(ctx);
    }

    //接收结果
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (!portAct) {
            byte[] recvBytes = new byte[in.readableBytes()];
            in.readBytes(recvBytes);
            String recvString = new String(recvBytes);
            String[] portInfos = recvString.split("#");
            if (portInfos.length == 3 && portInfos[1].equals("port")) {
                logger.debug("dataSocket 收到端口ack");
                DataClients.getSocketAck(ctx);
                portAct = true;
            } else {
                throw new RuntimeException("dataSocket接收portAct出现问题");
            }
            return;
        }
        //logger.debug("dataSocket收到数据长度" + in.readableBytes());
        System.out.print("#");
        Used = true;

//        byte[] recvBytes = new byte[in.readableBytes()];
//        in.readBytes(recvBytes);
//        String recvString = new String(recvBytes);
//        logger.debug(recvString);
//        final ByteBuf time = ctx.alloc().buffer(recvBytes.length);
//        time.writeBytes(recvBytes);


        ChannelHandlerContext portSocketByDataSocket = PortClients.getPortSocketByDataSocket(ctx);
        if (portSocketByDataSocket != null) {
            ChannelFuture f;
            f = portSocketByDataSocket.writeAndFlush(in);
            futureList.add(f);
            //logger.debug("Futrue="+f);
        } else {
            logger.debug("portSocket已经不存在了");
            DataClients.removeSocket(ctx);
            ctx.close();
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        while (!futureList.isEmpty()) {
            ChannelFuture f = futureList.get(0);
            if (f != null && !f.isSuccess()) {
                try {
                    f.await(5000);
                    //logger.debug("等待Futrue完成"+f);
                } catch (Exception e) {

                }
            } else {
                //logger.debug("future = null 或 已完成....."+f);
            }
            futureList.remove(f);
        }
        logger.debug("DataServer:客户端断开,ctx=" + ctx);
        DataClients.removeSocket(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        logger.debug("DataServer:非正常关闭客户端,ctx=" + ctx);
        ctx.close();
    }


}
