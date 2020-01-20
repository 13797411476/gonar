package cn.tandexue.tcpRouterServer.portService;

import cn.tandexue.tcpRouterServer.cmdService.CmdClients;
import cn.tandexue.tcpRouterServer.dataService.DataClients;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;


public class PortServerHandler extends ChannelHandlerAdapter {
    private Logger logger;

    private Boolean portAct;

    private ChannelHandlerContext dataCtx;

    private int port;

    public PortServerHandler(int port) {
        this.logger = Logger.getLogger(this.getClass());
        this.port = port;
        portAct = false;
        dataCtx = null;
    }


    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        logger.debug("Port-ChannerActive");
        if (!TestCmdClient(ctx))
            return;

        logger.debug("Port端有用户进来，尝试申请一个可用的datasocket,端口:" + port + ",CTX=" + ctx);
        if (dataCtx == null) {
            try {
                dataCtx = DataClients.getValidSocket(port);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (dataCtx != null) {
            PortClients.addPortSocketAndDataSocket(ctx, dataCtx);
        }
    }

    private boolean TestCmdClient(ChannelHandlerContext ctx) {
        ChannelHandlerContext cmdCtx = CmdClients.getCmdCtxByHost();
        if (cmdCtx == null) {
            logger.debug("没有Cmd客户端");
            ctx.close();
            return false;
        }
        System.out.print("%");
        return true;
    }

    //接收结果
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (!TestCmdClient(ctx))
            return;
        //logger.debug("Port收到数据，端口:" + port+ ",CTX=" + ctx);
        System.out.print("O");
        ByteBuf in = (ByteBuf) msg;
        if (dataCtx == null) {
            try {
                dataCtx = DataClients.getValidSocket(port);
                if(dataCtx!=null){
                    logger.debug("Port端新用户获得了可用的datasocket:" + dataCtx+ ",CTX=" + ctx+ ",DataCTX=" + dataCtx);
                    PortClients.addPortSocketAndDataSocket(ctx, dataCtx);
                }

            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
        if (dataCtx == null) {
            logger.debug("因无法获得datasocket关掉portsocket,PortCtx="+ctx);
            ctx.close();
        } else {
            dataCtx.writeAndFlush(in);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("PortServer:客户端断开,CTX="+ctx);
        PortClients.closePortSocketByPortSocket(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //cause.printStackTrace();
        logger.debug("DataServer:非正常关闭客户端");
        ctx.close();
    }
}
