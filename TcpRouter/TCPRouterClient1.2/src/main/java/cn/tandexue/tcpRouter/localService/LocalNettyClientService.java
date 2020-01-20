package cn.tandexue.tcpRouter.localService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class LocalNettyClientService {
    private int port;
    private String address;
    private ChannelFuture future;
    private ChannelHandlerContext dataCtx;

    public LocalNettyClientService(int port, String address, ChannelHandlerContext dataCtx) {
        this.port = port;
        this.address = address;
        this.dataCtx = dataCtx;
    }

    public void start() {

        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline ph = ch.pipeline();
                        //ph.addLast("ping", new IdleStateHandler(0, 13, 0, TimeUnit.SECONDS));
                        //ph.addLast(new HeartBeatClientHandler());//心跳
                        ph.addLast(new LocalServiceHanlder(dataCtx,port)); //客户端的逻辑
                    }
                });

        try {
            future = bootstrap.connect(address, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            if (null != future) {
                if (future.channel() != null && future.channel().isOpen()) {
                    future.channel().close();
                }
            }
        }
    }
}
