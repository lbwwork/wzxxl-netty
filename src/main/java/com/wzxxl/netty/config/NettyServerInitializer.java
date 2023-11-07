package com.wzxxl.netty.config;

import com.wzxxl.netty.handler.NettyServerTextHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author libaowen
 * @Date 2023/11/6 15:11
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<NioSocketChannel> {

    /**
     * WebSocket 服务的接口地址
     */
    public String path;

    /**
     * netty消息最大提及
     */
    private long maxFrameSize;
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        log.info("服务的接口地址：{}", path);
        ChannelPipeline pipeline = ch.pipeline();
        //HTTP协议编解码器，用于处理HTTP请求和响应的编码和解码。其主要作用是将HTTP请求和响应消息转换为Netty的ByteBuf对象，并将其传递到下一个处理器进行处理。
        pipeline.addLast(new HttpServerCodec());
        //用于HTTP服务端，将来自客户端的HTTP请求和响应消息聚合成一个完整的消息，以便后续的处理。
        pipeline.addLast(new HttpObjectAggregator(65536));
        //用于对WebSocket消息进行压缩和解压缩操作。
        pipeline.addLast(new WebSocketServerCompressionHandler());
        // 支持分块写入  在网络通信中，如果要传输的数据量较大，直接将数据一次性写入到网络缓冲区可能会导致内存占用过大或者网络拥塞等问题
        pipeline.addLast(new ChunkedWriteHandler());
        //这行很重要
        pipeline.addLast(new WebSocketServerProtocolHandler(path, null, true, maxFrameSize));
        pipeline.addLast(new NettyServerTextHandler());
    }
}
