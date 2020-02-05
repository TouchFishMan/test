package com.telek.hemsipc.protocal3761.netty;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.service.response.ResponseServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: wll
 * @Date: 2018/6/20 14:58
 * @Description:
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Packet> {

    private ExecutorService service = Executors.newCachedThreadPool();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String channelId = ctx.channel().remoteAddress().toString().substring(1);
        String address = NettyContext.addressChannelIdMap.get(channelId);
        log.error("{}，10分钟未收到服务器消息", address);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.error("关闭这个不活跃通道！");
                removeChannel(ctx);
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().remoteAddress().toString().substring(1);
        NettyContext.channelIdChannelMap.put(channelId, ctx.channel());
        log.error(ctx.channel().remoteAddress().toString().substring(1) + "----连接到服务端");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        removeChannel(ctx);
        log.error(ctx.channel().remoteAddress().toString().substring(1) + "----与服务器断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        removeChannel(ctx);
        cause.printStackTrace();
        log.error("netty出现异常", cause.getCause());
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
        //从服务端收到消息时被调用
        String channelId = ctx.channel().remoteAddress().toString().substring(1);
        String address2 = packet.getAddress2();
        if (address2 == null) {
            log.error("address2 is null");
        } else {
            NettyContext.addressChannelIdMap.put(address2, channelId);
        }
        service.execute(new ResponseServer(packet));
    }

    private void removeChannel(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().remoteAddress().toString().substring(1);
        String address2 = NettyContext.accidentRemoveAddress2(channelId);
        Protocol3761Cache.accidentRemoveAddress2(address2);
    }
}
