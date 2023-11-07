package com.wzxxl.netty.server;

import com.wzxxl.netty.config.NettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author libaowen
 * @Date 2023/11/6 15:14
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyServer {

    /**
     * netty 服务端口号
     */
    @Value("${netty.websocket.port}")
    private int port;

    /**
     * netty 服务path
     */
    @Value("${netty.websocket.path}")
    private String path;

    /**
     * netty 消息帧最大体积
     */
    @Value("${netty.websocket.max-frame-size}")
    private long maxFrameSize;

    /**
     * 主线程组 只处理连接请求
     */
    private EventLoopGroup bossGroup;

    /**
     * 子线程组 处理业务
     */
    private EventLoopGroup workerGroup;

    /**
     * 服务器端的启动对象
     */
    private ServerBootstrap serverBootstrap;

    private void start() {
        /**
         * 实例化 主、子线程组，服务端启动引导类
         */
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInitializer(path, maxFrameSize));

        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @PostConstruct
    public void init() {
        new Thread(this::start).start();
    }
}
