package cn.tandexue.tcpRouterServer.dataService;

import cn.tandexue.tcpRouterServer.cmdService.CmdNettyServer;
import org.apache.log4j.Logger;

public class DataService extends Thread {
    private Logger logger;

    public DataService() {
        this.logger = Logger.getLogger(this.getClass());
    }
    @Override
    public void run() {
        DataNettyServer nettyServer = new DataNettyServer(5556);
        logger.debug("Data端口：5556 监听中...");
        nettyServer.run();
    }
}
