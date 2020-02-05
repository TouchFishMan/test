package com.telek.hemsipc.http.handler;

import com.telek.hemsipc.http.HttpServerStart;
import com.telek.hemsipc.http.netty.entity.HttpTransferEntity;
import com.telek.hemsipc.http.queueserver.HttpRequestQueueServer;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.util.StringUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.BindException;
import java.util.Map;

/**
 * Description:netty通过http方式访问，请求数据处理类 HttpRequestHandler.java Create on
 * 2013-4-27 上午11:14:32
 * 
 * @author Administrator
 * @version 1.0 Copyright (c) 2013 telek. All Rights Reserved.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Log LOG = LogFactory.getLog(HttpRequestHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpServerStart.httpRequestThreadPool.execute(new HttpRequestQueueServer(new HttpTransferEntity(
                ctx, request, Unpooled.copiedBuffer(request.content()))));
    }

    @Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            if (cause.getCause() instanceof BindException) {
                LOG.error("端口被占用" + cause.getCause().toString(), cause.getCause());
                System.exit(0);
            }
            LOG.error(cause.getCause().toString(), cause.getCause());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("接收到HTTP请求："+ctx.channel().remoteAddress().toString().substring(1));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        removeChannel(ctx);
        LOG.error("HTTP通道关闭："+ctx.channel().remoteAddress().toString().substring(1));
    }


    private void removeChannel(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().remoteAddress().toString().substring(1);
        if (StringUtil.isBlank(channelId)) {
            return;
        }
        for(Map.Entry<String,String> entry: Protocol3761Cache.HTTP_COMMAND_CHANNEL_ID_MAP.entrySet()){
            if(channelId.equals(entry.getValue())){
                Protocol3761Cache.HTTP_COMMAND_CHANNEL_ID_MAP.remove(entry.getKey());
                break;
            }
        }
    }
}