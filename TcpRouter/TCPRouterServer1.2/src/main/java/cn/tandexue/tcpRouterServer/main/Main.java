package cn.tandexue.tcpRouterServer.main;

import cn.tandexue.tcpRouterServer.alertService.AlertService;
import cn.tandexue.tcpRouterServer.cmdService.CmdService;
import cn.tandexue.tcpRouterServer.dataService.DataService;
import cn.tandexue.tcpRouterServer.httpProxy.HttpProxy;
import cn.tandexue.tcpRouterServer.portService.PortService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService threadPoolExecutor = Executors.newCachedThreadPool();
        for (String arg :
                args) {
            try {
                int port = Integer.valueOf(arg);
                threadPoolExecutor.submit(new PortService(port));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        threadPoolExecutor.submit(new HttpProxy(1082));
        threadPoolExecutor.submit(new AlertService());
        threadPoolExecutor.submit(new CmdService());
        threadPoolExecutor.submit(new DataService());
//
//        (new PortService(8000)).start();//Seafile
//        (new PortService(8080)).start();//OnlyOffice
//        (new PortService(8082)).start();//Seafile
//        (new PortService(8088)).start();//WebDav
//        (new PortService(81)).start();  //SVN
//        (new PortService(8081)).start();//Nexus
//        (new PortService(1022)).start();//SSH
//        (new PortService(80)).start();  //http
//        (new PortService(3389)).start();//远程桌面
    }
}
