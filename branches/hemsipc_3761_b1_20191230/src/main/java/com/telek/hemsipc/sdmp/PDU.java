package com.telek.hemsipc.sdmp;

import com.telek.hemsipc.util.sercurity.AESUtil;
import com.telek.hemsipc.util.sercurity.HMACUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: PDU实体类 PDU.java Create on 2012-10-11 下午2:59:11
 * 
 * @author jnb
 * @version 1.0 Copyright (c) 2012 telek. All Rights Reserved.
 */
public class PDU implements BERSerializable, Serializable {
    private static final long serialVersionUID = 2091777996050562086L;
    private static PooledByteBufAllocator alloc = new PooledByteBufAllocator();

    public PDU() {
        msgID = new Integer32(0);
        requestType = new Integer32(0);
        actionType = new Integer32(0);
    }

    private Integer32 msgID;
    private Integer32 requestType;
    private Integer32 actionType;
    private List<VarBind> variableBindings;

    public List<VarBind> getVariableBindings() {
        return variableBindings;
    }

    public void setVariableBindings(List<VarBind> variableBindings) {
        this.variableBindings = variableBindings;
    }

    @Override
    public int getBERPayloadLength() {
        int length = msgID.getBERLength();
        length += requestType.getBERLength();
        length += actionType.getBERLength();
        for (int i = 0, j = variableBindings.size(); i < j; i++) {
            length += variableBindings.get(i).getBERLength();
        }
        return length;
    }

    /**
     * Description: 获取pdu加密后的长度 Date:2012-10-16
     * 
     * @author jnb
     * @param @param key
     * @param @return
     * @return int
     */
    public int getEncryptLength(byte[] key) {
        return AESUtil.getEncryptLength(this.getBERLength(), key);
    }

    @Override
    public int getBERLength() {
        int length = getBERPayloadLength();
        // 添加类型和长度的2个字节
        length += BER.getBERLengthOfLength(length) + 1;
        return length;
    }

    @Override
    public void encodeBER(ByteBuf buffer) throws IOException {
        BER.encodeHeader(buffer, BER.SEQUENCE, getBERPayloadLength());
        msgID.encodeBER(buffer);
        requestType.encodeBER(buffer);
        actionType.encodeBER(buffer);
        for (int i = 0, j = variableBindings.size(); i < j; i++) {
            variableBindings.get(i).encodeBER(buffer);
        }
    }

    /**
     * Description:加密pdu,并计算摘要码，并将两项传入outputStream直接编码好 Date:2012-10-16
     * 
     * @author jnb
     * @param @param outputStream
     * @param @param key
     * @param @throws IOException
     * @return void
     */
    public void encryptPDU(ByteBuf buffer, byte[] key) throws IOException {
        int length = AESUtil.getEncryptLength(this.getBERLength(), key);
        ByteBuf bufferTemp = alloc.heapBuffer(this.getBERLength());
        this.encodeBER(bufferTemp);
        byte[] b = new byte[bufferTemp.readableBytes()];
        bufferTemp.readBytes(b);
        byte[] temp = AESUtil.encrypt(b, key);
        bufferTemp.release();
        OctetString msgAuthenticationParameters = new OctetString(HMACUtil.encryptHMAC(temp, key));
        msgAuthenticationParameters.encodeBER(buffer);
        buffer.writeByte(BER.ASN_OCTET_STR);
        BER.encodeLength(buffer, length);
        buffer.writeBytes(temp);
    }

    /**
     * Description:解密字符串到pdu Date:2012-10-16
     * 
     * @author jnb
     * @param temp 加密后的pdu
     * @param key
     * @return
     * @throws IOException
     * @return byte[]
     */
    public void decryptPDU(byte[] temp, byte[] key) throws IOException {
        byte[] aesByte = AESUtil.decrypt(temp, key);
        ByteBuf buffer = alloc.heapBuffer(aesByte.length);
        buffer.writeBytes(aesByte);
        this.decodeBER(buffer);
        buffer.release();
    }

    @Override
    public void decodeBER(ByteBuf buffer) throws IOException {
        BER.MutableByte pduType = new BER.MutableByte();
        int length = BER.decodeHeader(buffer, pduType);
        long position = buffer.readerIndex();
        msgID.decodeBER(buffer);
        requestType.decodeBER(buffer);
        actionType.decodeBER(buffer);
        if (pduType.getValue() != BER.SEQUENCE) {
            throw new IOException("Encountered invalid tag, SEQUENCE expected: " + pduType.getValue());
        }
        variableBindings = new ArrayList<VarBind>();
        pduType = new BER.MutableByte();
        while (buffer.readerIndex() < position + length) {
            VarBind vb = new VarBind();
            vb.decodeBER(buffer);
            variableBindings.add(vb);
        }
    }

    public byte[] getScopedPDU(ByteBuf buffer) throws IOException {
        BER.MutableByte type = new BER.MutableByte();
        byte[] temp = BER.decodeString(buffer, type);
        if (type.getValue() != BER.OCTETSTRING) {
            throw new IOException("Wrong type encountered when decoding OctetString: " + type.getValue());
        }
        return temp;
    }

    /**
     * Description:查询pdu中是否包含特定的key值并且value是特定的值 Date:2012-11-1
     * 
     * @author kds
     * @param @param pdu
     * @param @param key
     * @param @param value
     * @param @return
     * @return boolean
     */
  /*  public boolean validateKeyValue(int key, int value) {
        List<VarBind> variableBindsVec = getVariableBindings();
        if (variableBindsVec == null || variableBindsVec.size() == 0) {
            return false;
        }
        for (int i = 0; i < variableBindsVec.size(); i++) {
            VarBind vb = variableBindsVec.get(i);
            if (vb.getName().toInt() == key && vb.getValue().toInt() == value) {
                return true;
            }
        }
        return false;
    }*/

    /**
     * Description:查询pdu中是否包含特定的key Date:2012-11-14
     * 
     * @author kds
     * @param @param key
     * @param @return
     * @return boolean
     */
    public boolean includeKey(int key) {
        List<VarBind> variableBindsVec = getVariableBindings();
        if (variableBindsVec == null || variableBindsVec.size() == 0) {
            return false;
        }
        for (int i = 0; i < variableBindsVec.size(); i++) {
            if (variableBindsVec.get(i).getName().toInt() == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Description:根据key获取键值对对象 Date:2012-12-1
     * 
     * @author kds
     * @param @param key
     * @param @return
     * @return VarBind
     */
    public VarBind getVarBindByKey(int key) {
        List<VarBind> variableBindsVec = getVariableBindings();
        if (variableBindsVec == null || variableBindsVec.size() == 0) {
            return null;
        }
        for (int i = 0; i < variableBindsVec.size(); i++) {
            if (variableBindsVec.get(i).getName().toInt() == key) {
                return variableBindsVec.get(i);
            }
        }
        return null;
    }

    /**
     * Description: 根据key获取值 Date:2012-12-20
     * 
     * @author kds
     * @param @param key
     * @param @return
     * @return String
     */
    public String getValueByKey(int key) {
        List<VarBind> variableBindsVec = getVariableBindings();
        for (int i = 0; variableBindsVec != null && i < variableBindsVec.size(); i++) {
            VarBind vb = variableBindsVec.get(i);
            if (vb.getName().toInt() == key) {
                return vb.getValue().toString();
            }
        }
        return "";
    }

    // public int getLastElecSendTime() {
    // List<VarBind> variableBindsVec = getVariableBindings();
    // for (int i = 0; variableBindsVec != null && i < variableBindsVec.size();
    // i++) {
    // VarBind vb = variableBindsVec.get(i);
    // if (vb.getName().toInt() == SdmpConstants.PDU_C_START_TIME) {
    // return vb.getValue().toInt();
    // }
    // }
    // return 0;
    // }
    /**
     * Description:根据key删除键值对 Date:2012-12-20
     * 
     * @author kds
     * @param @param key
     * @param @return
     * @return void
     */
    public void deleteVarBindByKey(int key) {
        List<VarBind> varBindVecotr = getVariableBindings();
        for (int i = 0; i < varBindVecotr.size(); i++) {
            VarBind varBind = varBindVecotr.get(i);
            if (varBind.getName().toInt() == key) {
                varBindVecotr.remove(i);
                break;
            }
        }
    }

    public Integer32 getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType.setValue(requestType);
    }

    public Integer32 getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType.setValue(actionType);
    }

    public Integer32 getMsgID() {
        return msgID;
    }

    public void setMsgID(int msgID) {
        this.msgID.setValue(msgID);
    }
}
