package com.telek.hemsipc.http.netty.entity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpTransferEntity {
    private ChannelHandlerContext ctx;
    private FullHttpRequest request;
    private ByteBuf bodyBuf;

    public HttpTransferEntity(ChannelHandlerContext ctx, FullHttpRequest request, ByteBuf bodyBuf) {
        super();
        this.ctx = ctx;
        this.request = request;
        this.bodyBuf = bodyBuf;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public ByteBuf getBodyBuf() {
        return bodyBuf;
    }
}
