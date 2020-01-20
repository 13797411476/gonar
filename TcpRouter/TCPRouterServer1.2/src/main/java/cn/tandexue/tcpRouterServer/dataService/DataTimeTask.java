package cn.tandexue.tcpRouterServer.dataService;


import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.TimerTask;

public class DataTimeTask extends TimerTask {
    private ChannelHandlerContext ctx;
    private DataServerHandler dataServerHandler;
    private Logger logger;

    public DataTimeTask(ChannelHandlerContext ctx, DataServerHandler dataServerHandler) {
        this.ctx = ctx;
        this.dataServerHandler = dataServerHandler;
        logger = Logger.getLogger(this.getClass());
    }

    public void run() {
        if(!dataServerHandler.getPortAct() || !dataServerHandler.getUsed()){
            logger.debug("dataSocket 超过3秒没收到port回复，关闭");
            ctx.close();
        }
    }
}
