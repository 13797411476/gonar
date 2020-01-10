package cn.lishe.gateway.core;

import cn.lishe.gateway.conf.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YeJin
 * @date 2020/1/9 14:54
 */
@Component
public class GatewayServer {
    private static Logger lg = LoggerFactory.getLogger(GatewayServer.class);
    @Autowired
    private NettyConfig nettyConfig;
    @Autowired
    private RouterHandler routerHandler;

    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .handler(new LoggingHandler(LogLevel.INFO));
        try {
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    // 请求消息解码器
                    pipeline.addLast("http-decoder", new HttpRequestDecoder(8192, 8192, 8192));
                    //将多个消息转换为单一的request或者response对象 [最大10M]
                    pipeline.addLast("http-aggregator", new HttpObjectAggregator(1024 * 1024 * 10));
                    //响应解码器
                    pipeline.addLast("http-encoder", new HttpResponseEncoder());
                    //目的是支持异步大文件传输（）
                    pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                    pipeline.addLast("compressor", new HttpContentCompressor());
                    pipeline.addLast(routerHandler);
                }
            });
            lg.error("Gateway Service Running on port:[{}]...", nettyConfig.getPort());
            ChannelFuture f = serverBootstrap.bind(nettyConfig.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            lg.error("ERROR,shutdown...", e);
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

}
