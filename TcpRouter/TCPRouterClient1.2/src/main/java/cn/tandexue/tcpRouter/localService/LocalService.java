package cn.tandexue.tcpRouter.localService;

import cn.tandexue.tcpRouter.main.Main;
import io.netty.channel.ChannelHandlerContext;


public class LocalService extends Thread {

    private int port;

    private ChannelHandlerContext dataCtx;

    public LocalService(int port,ChannelHandlerContext dataCtx) {
        this.port = port;
        this.dataCtx = dataCtx;
    }

    @Override
    public void run() {
        LocalNettyClientService localNettyClientService = new LocalNettyClientService(port, Main.LOCAL_SERVER_IP,dataCtx);
        localNettyClientService.start();
    }
}
