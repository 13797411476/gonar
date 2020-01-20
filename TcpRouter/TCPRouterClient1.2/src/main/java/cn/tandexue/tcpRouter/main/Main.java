package cn.tandexue.tcpRouter.main;

import cn.tandexue.tcpRouter.CmdService.CmdClient;

public class Main {
    //public static final String SERVERIP = "192.168.2.168";
    public static String SERVERIP = "127.0.0.1";


    public static String LOCAL_SERVER_IP = "127.0.0.1";
    //public static final String LOCAL_SERVER_IP = "192.168.2.168";


    public static void main(String[] args) {
        if (args.length == 2) {
            Main.SERVERIP = args[0];
            Main.LOCAL_SERVER_IP = args[1];
        }

        try {
            (new CmdClient()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
