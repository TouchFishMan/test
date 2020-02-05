package com.telek.hemsipc.protocal3761.netty;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * netty启动器
 */
@Slf4j
@Component
public class NettyStarter implements Runnable{

    /**
     * 端口
     */
    @Value("${netty.protocol3761.port}")
    private int port;

    /**
     * 服务端netty管道
     */
    private ServerSocketChannel channel;

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.MINUTES))
                                    .addLast(new HexDecoder()).addLast(new HexEncoder()).addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                log.info("启动 " + this.getClass().getSimpleName() + " 成功, port:" + port);
            }
            channel = (ServerSocketChannel) future.channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            log.error(this.getClass().getSimpleName() + "断开连接,即将进行重连");
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            run();
        }
    }

    public static void send(String address2, byte[] data) {
        log.info("发送帧:" + HexBin.encode(data));
        String channelId = NettyContext.addressChannelIdMap.get(address2);
        Channel channel = NettyContext.channelIdChannelMap.get(channelId);
        if (channel == null) {
            log.error("找不到{}对应的channel,集中器地址{}", channelId, address2);
            return;
        }
        channel.writeAndFlush(data);
    }

    public static void sendTest(String hex) {
        String s = hex.replaceAll(" ", "");
        Collection<Channel> values = NettyContext.channelIdChannelMap.values();
        for (Channel channel : values) {
            channel.writeAndFlush(HexBin.decode(s));
            return;
        }

    }
}
