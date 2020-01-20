package cn.tandexue.tcpRouter.DataService;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class DataClients {
    public static Map<ChannelHandlerContext, ChannelHandlerContext> dataSocketAndLocalSocketMaps;

    static {
        dataSocketAndLocalSocketMaps = new HashMap<ChannelHandlerContext, ChannelHandlerContext>();
    }

    /*
    产生一个DataSocket与服务器连接
     */
    public static void createDataSocket(int num) {

        for (int i = 0; i < num; i++) {
            DataService dataSocket = new DataService();
            dataSocket.start();
        }

    }


    public static void addDataSocketAndLocalSocket(ChannelHandlerContext dataSocket, ChannelHandlerContext localSocket) {
        synchronized (dataSocketAndLocalSocketMaps) {
            dataSocketAndLocalSocketMaps.put(dataSocket, localSocket);
        }
    }

    public static ChannelHandlerContext getLocalSocketByDataSocket(ChannelHandlerContext dataScoekt){
        return dataSocketAndLocalSocketMaps.get(dataScoekt);
    }

    public static void removeMapsByDataSocket(ChannelHandlerContext dataSocket) {
        synchronized (dataSocketAndLocalSocketMaps) {
            ChannelHandlerContext localSocket = dataSocketAndLocalSocketMaps.get(dataSocket);
            try {
                dataSocket.channel().close();
                System.out.println("dataSocketClosed...");
                if (localSocket != null) {
                    localSocket.channel().close();
                    System.out.println("localSocketClosed...");
                }
            } catch (Exception e) {

            }
            dataSocketAndLocalSocketMaps.remove(dataSocket);
        }
    }

    public static void removeMapsByLocalSocket(ChannelHandlerContext localSocket) {
        ChannelHandlerContext dataSocket = getDataSocketByLocalSocket(localSocket);
        if(dataSocket!=null){
            System.out.println("removeMapsByLocalSocket");
            removeMapsByDataSocket(dataSocket);
        }
    }

    public static ChannelHandlerContext getDataSocketByLocalSocket(ChannelHandlerContext localSocket) {
        synchronized (dataSocketAndLocalSocketMaps) {
            for (Map.Entry<ChannelHandlerContext, ChannelHandlerContext> entity :
                    dataSocketAndLocalSocketMaps.entrySet()) {
                if (entity.getValue() == localSocket) {
                    return entity.getKey();
                }
            }
            //throw new RuntimeException();
            return null;
        }
    }
}
