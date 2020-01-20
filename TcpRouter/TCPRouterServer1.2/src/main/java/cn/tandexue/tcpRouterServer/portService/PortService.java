package cn.tandexue.tcpRouterServer.portService;

public class PortService extends Thread {
    private int port;

    public PortService(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!服务启动，端口："+port);
        PortNettyServer portNettyServer = new PortNettyServer(port);
        portNettyServer.run();
    }
}
