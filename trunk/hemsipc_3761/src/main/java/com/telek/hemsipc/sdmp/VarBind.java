package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.Serializable;

/**
 * Description: VarBind实体类 VarBind.java Create on 2012-10-11 下午3:08:52
 * 
 * @author jnb
 * @version 1.0 Copyright (c) 2012 telek. All Rights Reserved.
 */
public class VarBind implements Serializable, BERSerializable, Cloneable {
    private static final long serialVersionUID = -4226019802009550557L;

    public VarBind() {
        name = new Integer32(0);
    }

    public VarBind(int name, Variable value) {
        this();
        setName(name);
        setValue(value);
    }

    // 键
    private Integer32 name;
    // 值
    private Variable value;

    @Override
    public int getBERLength() {
        int length = getBERPayloadLength();
        // 添加类型和长度的2个字节
        length += BER.getBERLengthOfLength(length) + 1;
        return length;
    }

    @Override
    public int getBERPayloadLength() {
        return name.getBERLength() + value.getBERLength();
    }

    @Override
    public final void decodeBER(ByteBuf buffer) throws IOException {
        BER.MutableByte type = new BER.MutableByte();
        BER.decodeHeader(buffer, type);
        if (type.getValue() != BER.SEQUENCE) {
            throw new IOException("Invalid sequence encoding: " + type.getValue());
        }
        name.decodeBER(buffer);
        value = AbstractVariable.createFromBER(buffer);
    }

    @Override
    public final void encodeBER(ByteBuf buffer) throws IOException {
        int length = getBERPayloadLength();
        BER.encodeHeader(buffer, BER.SEQUENCE, length);
        name.encodeBER(buffer);
        value.encodeBER(buffer);
    }

    public Integer32 getName() {
        return name;
    }

    public void setName(int name) {
        this.name.setValue(name);
    }

    public Variable getValue() {
        return value;
    }

    public void setValue(Variable value) {
        this.value = (Variable) value.clone();
    }
}
