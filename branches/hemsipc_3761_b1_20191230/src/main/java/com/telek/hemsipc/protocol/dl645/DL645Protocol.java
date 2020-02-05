package com.telek.hemsipc.protocol.dl645;


import com.telek.hemsipc.context.IoSession;
import com.telek.hemsipc.protocol.IProtocol;
import com.telek.hemsipc.protocol.IResponse;
import com.telek.hemsipc.protocol.dl645.response.AbstractDL645Response;
import com.telek.hemsipc.protocol.dl645.response.ReadAddressResponse;
import com.telek.hemsipc.protocol.dl645.response.ReadResponse;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Auther: wll
 * @Date: 2018/6/20 14:58
 * @Description:
 */
@Component
public class DL645Protocol implements IProtocol {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String DECODER_STATE_KEY = "DECODER.STATE";
    /**
     * 前导帧.
     */
    public static final int PREFIX_FRAM = 0xFE;
    /**
     * 帧起始符.
     */
    public static final int START_FRAM = 0x68;
    /**
     * 编码末尾byte.
     */
    private static final byte END_FRAM = 0x16;

    private static class Decoder {
        Integer start = null;
        byte[] slaveArr = null;
        Integer funCode = null;
        Integer byteLen = null;
        byte[] data = null;
    }

    @Override
    public IResponse decoder(ByteBuf buffer, IoSession session) {
        Decoder decoder = (Decoder) session.getAttribute(DECODER_STATE_KEY);
        if (decoder == null) {
            decoder = new Decoder();
            session.setAttribute(DECODER_STATE_KEY, decoder);
        }
        if (decoder.start == null) {
            int temp = (buffer.readByte() & 0xFF);
            if (temp == PREFIX_FRAM) {
                //如果有前导帧
                while (buffer.readableBytes() > 0) {
                    temp = (buffer.readByte() & 0xFF);
                    if (temp == PREFIX_FRAM) {// 前导帧
                        continue;
                    }
                    if (temp == START_FRAM) {// 帧起始符
                        break;
                    } else {
                        //前导帧错误不重置读指针，直到读到正确的帧数据
                        log.error("帧起始符读取错误！读取数据：{}", temp);
                        return null;
                    }
                }
            } else {
                //如果没有有前导帧，则直接判断帧起始符
                if (temp != START_FRAM) {
                    log.error("帧起始符读取错误！读取数据：{}", temp);
                    return null;
                }
            }
            decoder.start = temp;
        }
        if (decoder.slaveArr == null) {
            if (buffer.readableBytes() >= 9) {
                decoder.slaveArr = new byte[6];
                buffer.readBytes(decoder.slaveArr);
                // 跳过一个帧起始符
                int temp = (buffer.readByte() & 0xFF);
                if (temp != START_FRAM) {
                    log.error("帧起始符读取错误！读取数据：{}", temp);
                    session.setAttribute(DECODER_STATE_KEY, null);
                    return null;
                }
                // 控制码
                decoder.funCode = (int) buffer.readByte() & 0xFF;
                // 数据域长度
                decoder.byteLen = (int) buffer.readByte() & 0xFF;
            } else {
                return null;
            }
        }
        if (buffer.readableBytes() >= decoder.byteLen + 2) {
            AbstractDL645Response resp = null;
            switch (decoder.funCode) {
                case DL645Constant.RESPONSE_READ_ADDR:
                    if (decoder.byteLen != 6) {
                        log.error("通信地址读取数据长度位读取错误");
                        session.setAttribute(DECODER_STATE_KEY, null);
                        return null;
                    }
                    decoder.data = new byte[6];
                    buffer.readBytes(decoder.data);
                    resp = new ReadAddressResponse(decoder.slaveArr, decoder.data);
                    break;
                case DL645Constant.RESPONSE_READ_DATA:
                    decoder.data = new byte[decoder.byteLen];
                    buffer.readBytes(decoder.data);
                    resp = new ReadResponse(decoder.slaveArr, decoder.byteLen, decoder.data);
                    break;
                default:
                    log.error("控制码读取错误！读取数据：{}", decoder.funCode);
                    return null;
            }
            //CRC校验
            int cs = buffer.readByte() & 0xFF;
            if (cs != resp.getCs()) {
                log.error("CRC校验错误");
                session.setAttribute(DECODER_STATE_KEY, null);
                return null;
            }
            //结束符校验
            byte tail = buffer.readByte();
            if (tail != END_FRAM) {
                log.error("末尾结束符错误");
                session.setAttribute(DECODER_STATE_KEY, null);
                return null;
            }
            session.setAttribute(DECODER_STATE_KEY, null);
            return resp;
        }
        return null;
    }
}
