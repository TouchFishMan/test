package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Description: BERSerializable为使用BER编码的各种数据类型提供公用方法接口。 BERSerializable.java
 * Create on 2012-10-11 下午3:28:24
 * 
 * @author jnb
 * @version 1.0 Copyright (c) 2012 telek. All Rights Reserved.
 */
public interface BERSerializable {
    /**
     * Description: 返回此BERSerializable根据基本编码规则（BER）进行编码时，以字节为单位的对象长度，包括类型，长度字节。
     * Date:2012-10-11
     * 
     * @author jnb
     * @param @return
     * @return int
     */
    int getBERLength();

    /**
     * Description: 返回此BERSerializable根据基本编码规则（BER）进行编码时，以字节为单位的对象长度。
     * Date:2012-10-11
     * 
     * @author jnb
     * @param @return
     * @return int
     */
    int getBERPayloadLength();

    /**
     * Description: 从一个输入流中解码 Date:2012-10-11
     * 
     * @author jnb
     * @param @param inputStream
     * @param @throws IOException
     * @return void
     */
    void decodeBER(ByteBuf buffer) throws IOException;

    /**
     * Description: 将变量编码到一个输出流中 Date:2012-10-11
     * 
     * @author jnb
     * @param @param outputStream
     * @param @throws IOException
     * @return void
     */
    void encodeBER(ByteBuf buffer) throws IOException;
}
