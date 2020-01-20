package cn.tandexue.tcpRouter.CmdService;


import cn.tandexue.tcpRouter.main.Main;
import org.apache.log4j.Logger;

public class CmdClient {
    private static Logger logger = Logger.getLogger(CmdClient.class);
    public void start() throws Exception{
        while(true){
            logger.debug("cmd尝试连接服务器中");
            CmdNettyClientService cmdNettyClientServic = new CmdNettyClientService(5555, Main.SERVERIP);
            cmdNettyClientServic.start();
            Thread.sleep(2000);
        }

    }
}
