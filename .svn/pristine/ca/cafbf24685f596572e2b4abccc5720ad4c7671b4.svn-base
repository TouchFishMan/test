package com.telek.hemsipc.sdmp;

import com.telek.hemsipc.util.sercurity.AESUtil;
import com.telek.hemsipc.util.sercurity.HMACUtil;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * Description: SDMP协议实体类 SDMP.java Create on 2012-10-11 下午3:08:33
 * 
 * @author jnb
 * @version 1.0 Copyright (c) 2012 telek. All Rights Reserved.
 */
public class SDMPv1 implements SDMP, Serializable {
    private static final long serialVersionUID = -1376664452925330148L;
    private static Logger LOG = LoggerFactory.getLogger(SDMPv1.class);

    public SDMPv1() {
        msgVersion = new Integer32(1);
        // msgID = new Integer32(0);
        msgAuthoritativeEngineID = new OctetString(new byte[16]);
        msgAuthenticationParameters = new OctetString(new byte[16]);
        msgData = new PDU();
        flag = true;
    }

    public SDMPv1(int version) {
        msgVersion = new Integer32(version);
        msgAuthoritativeEngineID = new OctetString(new byte[16]);
        msgAuthenticationParameters = new OctetString(new byte[16]);
        msgData = new PDU();
        flag = true;
    }

    private Integer32 msgVersion;
    // HeaderData
    // private Integer32 msgID;
    // USM
    // 设备id
    private OctetString msgAuthoritativeEngineID;
    private OctetString msgAuthenticationParameters;
    // PDU
    private PDU msgData;
    private boolean flag;
    // 密钥
    private byte[] key = null;

    @Override
    public int getBERPayloadLength() {
        int length = msgVersion.getBERLength();
        // length += msgID.getBERLength();
        length += msgAuthoritativeEngineID.getBERLength();
        if (key == null || key.length == 0) {
            LOG.error("decoder error ,devicekey not found");
            return 0;
        }
        length += msgAuthenticationParameters.getBERLength();
        int encryptedLength = AESUtil.getEncryptLength(msgData.getBERLength(), key);
        length += encryptedLength + BER.getBERLengthOfLength(encryptedLength) + 1;
        return length;
    }

    @Override
    public int getBERLength() {
        int length = getBERPayloadLength();
        // 添加类型和长度的2个字节
        length += BER.getBERLengthOfLength(length) + 1;
        return length;
    }

    @Override
    public void encodeBER(final ByteBuf buffer) {
        try {
            BER.encodeHeader(buffer, BER.SEQUENCE, getBERPayloadLength());
            if (null == key) {
                flag = false;
                LOG.error("Can't find " + msgAuthoritativeEngineID + " key!");
                return;
            }
            msgVersion.encodeBER(buffer);
            msgAuthoritativeEngineID.encodeBER(buffer);
            msgData.encryptPDU(buffer, key);
        } catch (Exception e) {
            flag = false;
            if (msgData.getMsgID().toInt() != 0 && msgAuthoritativeEngineID != null) {
                LOG.error("DeviceID:" + msgAuthoritativeEngineID + " MsgID:" + msgData.getMsgID()
                        + " encode error");
            } else {
                LOG.error("Encode error,There is no msgid,not out sdmp");
            }
        }
    }

    public void decodeMsgAuthoritativeEngineID(ByteBuf buffer) throws IOException {
        msgAuthoritativeEngineID.decodeBER(buffer);
    }

    /**
     * Description:decodeBER Date:2012-10-23
     * 
     * @author jnb
     * @param buffer
     * @throws IOException
     * @return void
     */
    @Override
    public void decodeBER(final ByteBuf buffer) throws IOException {
        try {
            if (null == key) {
                flag = false;
                buffer.readerIndex(buffer.capacity());
                return;
            }
            msgAuthenticationParameters.decodeBER(buffer);
            // 把获取ScopedPDU提取出来，目的为了放在AES解密之前进行消息验证
            byte[] temp = msgData.getScopedPDU(buffer);
            // 摘要码验证
            if (!HMACUtil.HMACIsValid(msgAuthenticationParameters, temp, key)) {
                flag = false;
                buffer.readerIndex(buffer.capacity());
                return;
            }
            // 解密pdu
            msgData.decryptPDU(temp, key);
        } catch (Exception e) {
            flag = false;
            buffer.readerIndex(buffer.capacity());
            LOG.error("InvalidReport:解码过程中抛出异常！！deviceID:" + msgAuthoritativeEngineID + "；msgID："
                    + msgData.getMsgID() + ";key:" + new String(key) + "   " + e.getMessage(),e);
            return;
        }
    }

    @Override
    public void decodeCommandBER(final ByteBuf buffer) {
        try {
            // msgID.decodeBER(buffer);
            msgAuthoritativeEngineID.decodeBER(buffer);
            // msgAuthenticationParameters.decodeBER(buffer);
            msgData.decodeBER(buffer);
        } catch (Exception e) {
            flag = false;
            buffer.readerIndex(buffer.capacity());
            LOG.error("解码过程中抛出异常！！deviceID:" + msgAuthoritativeEngineID + "；msgID：" + msgData.getMsgID()
                    + ";key:" + new String(key));
            return;
        }
    }

    public Integer32 getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(final int msgVersion) {
        this.msgVersion.setValue(msgVersion);
    }

    public OctetString getMsgAuthoritativeEngineID() {
        return msgAuthoritativeEngineID;
    }

    public void setMsgAuthoritativeEngineID(final byte[] msgAuthoritativeEngineID) {
        this.msgAuthoritativeEngineID.setValue(msgAuthoritativeEngineID);
    }

    public OctetString getMsgAuthenticationParameters() {
        return msgAuthenticationParameters;
    }

    public PDU getMsgData() {
        return msgData;
    }

    public void setMsgData(final PDU msgData) {
        this.msgData = msgData;
    }

    @Override
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(final boolean flag) {
        this.flag = flag;
    }

    public void setKey(final byte[] key) {
        this.key = key;
    }

    public byte[] getKey() {
        return key;
    }
}
