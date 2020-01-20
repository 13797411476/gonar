package cn.tandexue.tcpRouterServer.httpProxy;

import cn.tandexue.tcpRouterServer.dataService.DataNettyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class HttpProxy extends Thread {

    private int proxyPort;

    public HttpProxy(int proxyPort){
        this.proxyPort = proxyPort;
    }

    @Override
    public void run() {
        nettyService();
    }

    public void nettyService(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b = b.group(bossGroup, workerGroup);
        b = b.channel(NioServerSocketChannel.class);
        b = b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(15, 0, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));//将多个消息转化成一个
                ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//解决大码流的问题
                ch.pipeline().addLast(new HttpProxyNettyHandler()); //进一步处理;
            }
        });
        b = b.option(ChannelOption.SO_BACKLOG, 1024);
        b = b.childOption(ChannelOption.SO_KEEPALIVE, true);
        DataNettyServer.startNetty(workerGroup, bossGroup, b.bind(proxyPort), b);
    }

}
