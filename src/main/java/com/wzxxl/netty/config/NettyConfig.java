package com.wzxxl.netty.config;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author libaowen
 * @Date 2023/11/6 14:24
 * @Version 1.0
 */
@Configuration
public class NettyConfig {

    /**
     * 存储所有在线的客户端channel
     *
     * ChannelGroup是netty中的一种特殊的channel容器，用于管理多个channel对象
     * 它可以用来跟踪和管理与服务器建立的所有活动连接
     *      广播消息，批量操作，管理链接
     * 使用channelGroup可以有效的管理多个channel，简化了对连接的管理和操作，特别适用于需要同时处理多个连接的情况，例如实现聊天室、推送系统等。
     *
     */
    private static final ChannelGroup onlineChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    /**
     * 存储所有在线的userId与之对应的channel
     * ConcurrentHashMap是Java中的一个线程安全的哈希表实现类，它继承自AbstractMap类，实现了ConcurrentMap接口。
     * 与HashMap相比，ConcurrentHashMap在多线程环境下提供了更好的并发性能和线程安全性。
     * 它采用了分段锁的机制，将整个哈希表分成多个段（Segment），每个段维护一个独立的哈希表。
     * 不同的线程可以同时访问不同的段，从而实现了并发读写的能力
     */
    private static final ConcurrentMap<Long, Channel> onlineUserChannelMap = new ConcurrentHashMap<>();

    public static ChannelGroup getOnlineChannelGroup() {
        return onlineChannelGroup;
    }

    public static  ConcurrentMap<Long, Channel> getOnlineUserChannelMap() {
        return onlineUserChannelMap;
    }
}
