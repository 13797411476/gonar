package cn.tandexue.tcpRouter.DataService;

import cn.tandexue.tcpRouter.CmdService.CmdNettyClientService;
import cn.tandexue.tcpRouter.main.Main;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class DataService extends Thread {

    @Override
    public void run() {
        DataNettyClientService cmdNettyClientServic = new DataNettyClientService(5556, Main.SERVERIP);
        cmdNettyClientServic.start();
    }
}
