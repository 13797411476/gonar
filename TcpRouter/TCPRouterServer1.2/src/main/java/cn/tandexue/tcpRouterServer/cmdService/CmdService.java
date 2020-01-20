package cn.tandexue.tcpRouterServer.cmdService;

import org.apache.log4j.Logger;


public class CmdService extends Thread {
    private Logger logger;

    public CmdService() {
        this.logger = Logger.getLogger(this.getClass());
    }

    @Override
    public void run() {
        CmdNettyServer nettyServer = new CmdNettyServer(5555);
        logger.debug("Cmd端口：5555 监听中...");
        nettyServer.run();
    }
}
