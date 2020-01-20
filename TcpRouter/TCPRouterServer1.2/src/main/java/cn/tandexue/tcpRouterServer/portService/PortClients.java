package cn.tandexue.tcpRouterServer.portService;

import cn.tandexue.tcpRouterServer.dataService.DataClients;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PortClients {
    private static Logger logger = Logger.getLogger(PortClients.class);

    /*
     *port socket 与 datasocket 的映射
     *
     */
    private static Map<ChannelHandlerContext, ChannelHandlerContext> portSocketAndDataSocketMap;

    static {
        portSocketAndDataSocketMap = new HashMap<ChannelHandlerContext, ChannelHandlerContext>();
    }

    public static void addPortSocketAndDataSocket(ChannelHandlerContext portSocket, ChannelHandlerContext dataSocket) {
        synchronized (portSocketAndDataSocketMap) {
            portSocketAndDataSocketMap.put(portSocket, dataSocket);
        }

    }

    public static void closePortSocketByDataSocket(ChannelHandlerContext dataSocket) {
        synchronized (portSocketAndDataSocketMap) {
            for (Iterator<Map.Entry<ChannelHandlerContext, ChannelHandlerContext>> it = portSocketAndDataSocketMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<ChannelHandlerContext, ChannelHandlerContext> item = it.next();
                if (item.getValue() == dataSocket) {
                    ChannelHandlerContext portSocket = item.getKey();
                    it.remove();
                    logger.debug("protService移除一个客户端,dataCTX="+dataSocket+",PortCTX="+portSocket);
                    try {
                        portSocket.close();
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public static ChannelHandlerContext getPortSocketByDataSocket(ChannelHandlerContext dataSocket) {
        synchronized (portSocketAndDataSocketMap) {
            for (Map.Entry<ChannelHandlerContext, ChannelHandlerContext> entity :
                    portSocketAndDataSocketMap.entrySet()) {
                if (entity.getValue() == dataSocket) {
                    return entity.getKey();
                }
            }
            return null;
        }
    }

    public static void closePortSocketByPortSocket(ChannelHandlerContext portSocket) {
        synchronized (portSocketAndDataSocketMap) {
            ChannelHandlerContext dataSocket = portSocketAndDataSocketMap.get(portSocket);
            if (dataSocket != null) {
                //DataClients.addSocket(dataSocket);
                dataSocket.close();
            }
            portSocket.close();
        }
    }
}
