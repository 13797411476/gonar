package cn.tandexue.tcpRouter.CmdService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class CmdNettyClientService {
    private int port;
    private String address;
    ChannelFuture future;

    public CmdNettyClientService(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public void start() {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline ph = ch.pipeline();
                        ph.addLast(new IdleStateHandler(15, 13, 0, TimeUnit.SECONDS));
                        ph.addLast(new CmdHeartBeatHandler());//接收心跳
                        ph.addLast(new LineBasedFrameDecoder(1024)); //按行分割
                        ph.addLast(new StringDecoder()); //转为字符串
                        ph.addLast(new CmdServiceHanlder()); //客户端的逻辑
                    }
                });

        try {
            future = bootstrap.connect(address, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            //e.printStackTrace();
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
