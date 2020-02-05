package com.telek.hemsipc.netty.init;

import com.telek.hemsipc.netty.codec.SdmpDecoder;
import com.telek.hemsipc.netty.codec.SdmpEncoder;
import com.telek.hemsipc.netty.handler.CloudClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: wll
 * @Date: 2018/6/21 10:51
 * @Description: 与云服务连接客户端
 */
@Component
public class CloudClientPoint implements Runnable {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${netty.cloud.server.host}")
    private String server_host;
    @Value("${netty.cloud.server.port}")
    private int server_port;
    @Value("${netty.cloud.client.port}")
    private int client_port;
    /**
     * 客戶端netty管道.
     */
    private Channel channel;

    /**
     * netty启动线程池.
     */
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void start() {
        singleThreadExecutor.submit(this);
    }

    public void resetPort() {
        if (this.client_port > 65536 || this.client_port < 1024) {
            this.client_port = 1025;
        } else {
            this.client_port++;
        }
        channel.close();
    }

    public void run() {
        final InetSocketAddress remoteAddress = new InetSocketAddress(server_host, server_port);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel nioDatagramChannel) {
                            nioDatagramChannel.pipeline().addLast(new SdmpDecoder()).addLast(new SdmpEncoder(remoteAddress))
                                    .addLast(new CloudClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(client_port).sync();
            if (future.isSuccess()) {
                log.info("启动 " + this.getClass().getSimpleName() + " 成功");
            }
            channel = future.channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            group.shutdownGracefully();
            log.error(this.getClass().getSimpleName() + "断开连接,即将进行重连");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            start();
        }
    }

    public void restartNetty() {
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        start();
    }

    public void stopNetty() {
        channel.close();
        singleThreadExecutor.shutdownNow();
    }

    public void sendMsg(Object msg) {
        channel.writeAndFlush(msg);
    }

    public String getServer_host() {
        return server_host;
    }

    public int getServer_port() {
        return server_port;
    }

    public int getClient_port() {
        return client_port;
    }

    public Channel getChannel() {
        return channel;
    }
}
