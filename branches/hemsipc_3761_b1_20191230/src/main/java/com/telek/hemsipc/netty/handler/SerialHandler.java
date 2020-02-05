package com.telek.hemsipc.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.context.NettyContext;
import com.telek.hemsipc.netty.future.SyncWriteFuture;
import com.telek.hemsipc.protocol.IResponse;
import com.telek.hemsipc.protocol.dl645.response.ReadResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Auther: wll
 * @Date: 2018/6/20 14:58
 * @Description:
 */
public class SerialHandler extends SimpleChannelInboundHandler<IResponse> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 通道重新连接到服务器需要更新Device的Channel
        NettyContext.clientChannel.put(ctx.channel().remoteAddress().toString().substring(1), ctx.channel());
        log.info(ctx.channel().remoteAddress().toString().substring(1) + "----连接到服务端");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyContext.clientChannel.remove(ctx.channel().remoteAddress().toString().substring(1));
        log.info(ctx.channel().remoteAddress().toString().substring(1) + "----与服务器断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常时断开连接 // TODO 这里对吗？
        // NettyContext.clientChannel.clear();
        log.error("netty出现异常", cause);
       // ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, IResponse resp) throws Exception {
        // 从服务端收到消息时被调用
        log.info("从{}收到消息：{}", ctx.channel().remoteAddress().toString().substring(1), resp);
        String syncKey = ctx.channel().remoteAddress().toString().substring(1);
        //if (resp instanceof ReadResponse) {
        syncKey = syncKey + ((ReadResponse) resp).getSyncKey();
        //}
        SyncWriteFuture future = NettyContext.syncKey.get(syncKey);
        if (future != null) {
            future.setResult(resp);
            return;
        } else {
            log.info("从{}收到消息：{} 找不到对应的future", ctx.channel().remoteAddress().toString().substring(1), resp);
            // 处理异步消息 TODO
            // service.submit();
        }
    }
}
