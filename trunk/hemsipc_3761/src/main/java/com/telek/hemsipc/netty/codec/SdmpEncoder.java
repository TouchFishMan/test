package com.telek.hemsipc.netty.codec;

import com.telek.hemsipc.sdmp.SDMP;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Auther: wll
 * @Date: 2018/9/14 16:03
 * @Description:
 */
public class SdmpEncoder extends MessageToMessageEncoder<SDMP> {
    private final InetSocketAddress remoteAddress;

    public SdmpEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SDMP sdmp, List<Object> out) throws Exception {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(sdmp.getBERLength());
        sdmp.encodeBER(byteBuf);
        out.add(new DatagramPacket(byteBuf, remoteAddress));
    }
}
