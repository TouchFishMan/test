package com.telek.hemsipc.protocol;

import com.telek.hemsipc.context.IoSession;
import io.netty.buffer.ByteBuf;

/**
 * @Auther: wll
 * @Date: 2018/9/10 17:15
 * @Description:
 */
public interface IProtocol {
    IResponse decoder(ByteBuf buffer, IoSession session);
}
