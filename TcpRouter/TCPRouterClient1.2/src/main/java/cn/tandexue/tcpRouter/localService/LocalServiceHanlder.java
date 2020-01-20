package cn.tandexue.tcpRouter.localService;

import cn.tandexue.tcpRouter.DataService.DataClients;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.util.Vector;

public class LocalServiceHanlder extends ChannelHandlerAdapter {

    private int port;

    private Logger logger;

    private ChannelHandlerContext dataCtx;



    private Vector<ChannelFuture> futureList;

    public LocalServiceHanlder(ChannelHandlerContext dataCtx, int port) {
        logger = Logger.getLogger(this.getClass());
        this.dataCtx = dataCtx;
        this.port = port;
        futureList = new Vector<ChannelFuture>();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        //logger.debug("localPort收到数据，长度" + in.readableBytes());
        System.out.print( "O");
//
//        byte[] b = new byte[in.readableBytes()];
//        in.readBytes(b);
//        String bstr = new String(b);
//        logger.debug(bstr);
//
//        final ByteBuf time = ctx.alloc().buffer(b.length);
//        time.writeBytes(b);
        ChannelFuture f;
        f = dataCtx.writeAndFlush(in);
        futureList.add(f);
        //logger.debug("Futrue="+f);
    }

    //
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("LocalSocket加入");
        DataClients.addDataSocketAndLocalSocket(dataCtx, ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("localSocket关闭");
        while(!futureList.isEmpty()){
            ChannelFuture f = futureList.get(0);
            if(f!=null && !f.isSuccess()){
                try{
                    f.await();
                    //logger.debug("等待Futrue完成"+f);
                }catch (Exception e){

                }
            }else{
                //logger.debug("future = null 或 已完成....."+f);
            }
            futureList.remove(f);
        }
        DataClients.removeMapsByLocalSocket(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }


}