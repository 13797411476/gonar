package cn.tandexue.tcpRouterServer.cmdService;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CmdClients {
    /*
        存放客户端注册的 域名 和 对应的ChannelHandlerContext
        eg.  "www.zinsoft.com" <---->  ChannelHandlerContext
     */
    public static Map<String, ChannelHandlerContext> clients = new HashMap<String, ChannelHandlerContext>();

    public static Logger logger = Logger.getLogger(CmdClients.class);

    //    public static void writeTest(ChannelHandlerContext ctx){
    //        final ByteBuf time = ctx.alloc().buffer(4);
    //        time.writeBytes("hello".getBytes());
    //        final ChannelFuture f = ctx.writeAndFlush(time);
    //    }
     /*
        加入一个客户端
     */
    public static void addClient(String hostname, ChannelHandlerContext ctx) {
        synchronized (clients) {
            clients.put(hostname, ctx);
            logger.debug("TcpRouter客户端已连接");
            clients.notify();
        }
    }

    /*
        删除一个客户端
     */
    public static void removeClientBySocket(ChannelHandlerContext ctx) {
        synchronized (clients) {
            for (Iterator<Map.Entry<String, ChannelHandlerContext>> it = clients.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, ChannelHandlerContext> item = it.next();
                if (item.getValue() == ctx) {
                    it.remove();
                    logger.debug("CmdService:删除一个客户端");
                    break;
                }
            }
        }
    }


    public static ChannelHandlerContext getCmdCtxByHost(){
        return clients.get("www.zinsoft.com");
    }

    /**
     * 发消息给客户端
     * @param msg
     * @return
     */
    public static Boolean sendToClient(String msg) {
        ChannelHandlerContext cmdCtx;
        synchronized (clients) {
            cmdCtx = clients.get("www.zinsoft.com");
            if (cmdCtx == null) {
                try {
                    logger.debug("等待TcpRouter客户端连接");
                    clients.wait(5000);
                } catch (InterruptedException e) {
                    logger.debug("等待TcpRouter客户端连接超时");
                    return false;
                }
            }
        }
        //System.out.println(cmdCtx+":发送"+msg);
        final ByteBuf time = cmdCtx.alloc().buffer(msg.length() * 2);
        time.writeBytes(msg.getBytes());
        final ChannelFuture f = cmdCtx.writeAndFlush(time);
        try {
            f.await(5000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        return true;
    }
}
