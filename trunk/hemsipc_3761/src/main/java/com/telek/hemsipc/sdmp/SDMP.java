package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Description: SDMP接口，各具体版本SDMP类均需实现此接口 SDMP.java Create on 2012-11-12
 * 下午3:46:26
 * 
 * @author jnb
 * @version 1.0 Copyright (c) 2012 telek. All Rights Reserved.
 */
public interface SDMP {
    /**
     * Description: 返回此BERSerializable根据基本编码规则（BER）进行编码时，以字节为单位的对象长度，包括类型，长度字节。
     * Date:2012-11-12
     * 
     * @author jnb
     * @param @return
     * @return int
     */
    int getBERLength();

    /**
     * Description: 返回此BERSerializable根据基本编码规则（BER）进行编码时，以字节为单位的对象长度。
     * Date:2012-11-12
     * 
     * @author jnb
     * @param @return
     * @return int
     */
    int getBERPayloadLength();

    /**
     * Description: 从一个输入流中解码 Date:2012-11-12
     * 
     * @author jnb
     * @param @param inputStream
     * @param @throws IOException
     * @return void
     */
    void decodeBER(ByteBuf buffer) throws IOException;

    /**
     * Description:解码未加密的buffer Date:2013-1-18
     * 
     * @author jnb
     * @param @param buffer
     * @param @throws IOException
     * @return void
     */
    void decodeCommandBER(ByteBuf buffer) throws IOException;

    /**
     * Description: 将变量编码到一个输出流中 Date:2012-11-12
     * 
     * @author jnb
     * @param @param outputStream
     * @param @throws IOException
     * @return void
     */
    void encodeBER(ByteBuf buffer);

    /**
     * Description:判断解码是否成功 Date:2012-11-12
     * 
     * @author jnb
     * @param @return
     * @return boolean
     */
    boolean isFlag();
}
