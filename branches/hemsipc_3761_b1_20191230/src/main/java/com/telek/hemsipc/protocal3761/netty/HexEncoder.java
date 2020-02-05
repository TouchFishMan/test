package com.telek.hemsipc.protocal3761.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: wll
 * @Date: 2019/1/17 15:42
 * @Description:
 */
@Slf4j
public class HexEncoder extends MessageToByteEncoder<byte[]> {
    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] data, ByteBuf out) throws Exception {
        out.writeBytes(data);
    }
}
