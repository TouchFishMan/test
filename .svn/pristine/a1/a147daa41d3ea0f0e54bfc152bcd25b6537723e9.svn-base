package com.telek.hemsipc.netty.init;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.context.NettyContext;
import com.telek.hemsipc.netty.codec.IpcDecoder;
import com.telek.hemsipc.netty.codec.IpcEncoder;
import com.telek.hemsipc.netty.future.SyncWriteFuture;
import com.telek.hemsipc.netty.handler.SerialHandler;
import com.telek.hemsipc.protocol.IRequest;
import com.telek.hemsipc.protocol.IResponse;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Auther: wll
 * @Date: 2018/6/21 10:51
 * @Description: 与电表连接服务端
 */
// @Component
public class SerialPoint implements Runnable {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${netty.serial.port}")
    private Integer server_port;

    /**
     * 服务端netty管道.
     */
    private ServerSocketChannel channel;

    public ServerSocketChannel getChannel() {
        return channel;
    }

    /**
     * netty启动线程池.
     */
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void start() {
        singleThreadExecutor.submit(this);
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
							channel.pipeline().addLast(new IpcDecoder())
									 .addLast(new IpcEncoder()) .addLast(new SerialHandler());
                            // TODO IpcEncoder() 不需要
                        }
                    });
            bootstrap.option(ChannelOption.SO_RCVBUF, 1024); //必须设置，否则只能接受64字节
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            ChannelFuture future = bootstrap.bind(server_port).sync();
            if (future.isSuccess()) {
                log.info("启动 " + this.getClass().getSimpleName() + " 成功");
            }
            channel = (ServerSocketChannel) future.channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            log.error(this.getClass().getSimpleName() + "断开连接,即将进行重连");
            try {
                Thread.sleep(60000);
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

    /**
     * @Description: 发送消息到指定的客户端管道
     * @auther: wll
     * @date: 12:15 2018/6/23
     * @param: [channekKey, msg]
     * @return: void
     */
    public void sendMsgByClientChannel(String channelId, IRequest msg) {
        Channel channel = NettyContext.clientChannel.get(channelId);
        if (channel != null) {
        	System.out.println("发送控制指令给PLC--------------" + HexBin.encode(msg.getMessageData()));
            channel.writeAndFlush(msg.getMessageData());
        }
        else {
        	log.warn("找不到 " + channelId +  "  对应的通道");
        }
    }

    public IResponse syncSendMsgByClientChannel(String channelId, IRequest request) {
        Channel channel = NettyContext.clientChannel.get(channelId);
        if (channel == null) {
            log.error("{}-netty连接未连接", channelId);
            return null;
        }
        String syncKey = channelId;
        //if (request instanceof ReadRequest) {
            syncKey = syncKey + request.getSyncKey();
       // }
        SyncWriteFuture future = new SyncWriteFuture();
        NettyContext.syncKey.put(syncKey, future);
        IResponse resp = null;
        try {
            final String finalSyncKey = syncKey;
            System.out.println("发送数据给PLC--------------" + HexBin.encode(request.getMessageData()));
            channel.writeAndFlush(request.getMessageData()).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    // 失败移除
                    if (!future.isSuccess()) {
                        NettyContext.syncKey.remove(finalSyncKey);
                    }
                }
            }).sync();
            resp = future.get(3000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("采集数据异常", e);
        } finally {
            NettyContext.syncKey.remove(syncKey);
        }
        return resp;
    }
}
