package cn.tandexue.tcpRouterServer.dataService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class DataNettyServer {
    private int port;//端口

    public DataNettyServer(int port) {
        this.port = port;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b = b.group(bossGroup, workerGroup);
        b = b.channel(NioServerSocketChannel.class);
        b = b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(33, 30, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast(new DataHeartBeatClientHandler());//心跳
                ch.pipeline().addLast(new DataServerHandler()); //进一步处理

            }
        });
        b = b.option(ChannelOption.SO_BACKLOG, 1024);
        b = b.childOption(ChannelOption.SO_KEEPALIVE, true);
        startNetty(bossGroup, workerGroup, b.bind(port), b);
    }

    public static void startNetty(EventLoopGroup bossGroup, EventLoopGroup workerGroup, ChannelFuture bind, ServerBootstrap b) {
        try {
            ChannelFuture f = bind.sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
