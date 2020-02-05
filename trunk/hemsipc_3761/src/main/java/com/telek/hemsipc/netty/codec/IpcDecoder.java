package com.telek.hemsipc.netty.codec;


import com.telek.hemsipc.context.IoSession;
import com.telek.hemsipc.protocol.IProtocol;
import com.telek.hemsipc.protocol.ProtocolFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IpcDecoder extends ByteToMessageDecoder {
    private Map<String,IoSession> sessionMap = new ConcurrentHashMap<>();
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        IoSession session = sessionMap.get(ctx.channel().toString());
        String channelId = ctx.channel().remoteAddress().toString().substring(1);
        if(session == null){
            session = new IoSession();
            sessionMap.put(channelId,session);
        }
        IProtocol protocol = ProtocolFactory.getDecodeProtocol(channelId);
        Object decoded = protocol.decoder(in,session);
        if (decoded != null) {
            out.add(decoded);
        }
    }
}
