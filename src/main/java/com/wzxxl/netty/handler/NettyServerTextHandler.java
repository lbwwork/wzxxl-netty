package com.wzxxl.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
 * ChannelHandler.Sharable  注解的作用
 *  是使一个ChannelHandler实例可被多个ChannelPipeline共享使用，提高性能和资源利用，并需要确保线程安全性。
 *  WebSocketFrame 是Netty中提供的一个类，它是对WebSocket协议中的帧(Frame)进行了封装。
 *  帧是WebSocket通信的基本单位，它可以包含各种类型的数据，并且帧之间可以按照一定的协议顺序进行传输。
 * @Author libaowen
 * @Date 2023/11/6 15:05
 * @Version 1.0
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerTextHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    /**
     * 当客户端连接服务器完成就会触发该方法
     * 当channel 的连接建立并准备好接受数据时被调用，这意味着连接已经成功建立，可以开始发送和接受数据了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接建立通道完成。。可以开始发送数据。。");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        //把WebSocketFrame强转成TextWebSocketFrame处理文本新信息
        TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) webSocketFrame;
        log.info("客户端=========:" + textWebSocketFrame.text());
    }
}
