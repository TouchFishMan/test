package com.telek.hemsipc.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.sdmp.SDMP;
import com.telek.hemsipc.sdmp.SDMPv1;
import com.telek.hemsipc.server.DealCloudDataServer;
import com.telek.hemsipc.util.ThreadPoolUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Auther: wll
 * @Date: 2018/6/20 14:58
 * @Description:
 */
public class CloudClientHandler extends SimpleChannelInboundHandler<SDMP> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端和服务端建立连接时调用，向云服务发送areacode
        log.info("已经和云服务建立连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        log.error("netty出现异常", cause);
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, SDMP sdmp) throws Exception {
        ThreadPoolUtil.executeDealCloudData(new DealCloudDataServer((SDMPv1) sdmp));
    }
}
