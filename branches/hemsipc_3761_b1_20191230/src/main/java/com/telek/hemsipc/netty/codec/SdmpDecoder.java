package com.telek.hemsipc.netty.codec;

import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.sdmp.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: wll
 * @Date: 2018/9/14 15:58
 * @Description:
 */
public class SdmpDecoder extends MessageToMessageDecoder<DatagramPacket> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public SDMP parseCmessage(ByteBuf buffer) {
        getMsgVersion(buffer);
        SDMPv1 sdmpData = new SDMPv1(SdmpConstant.DEVICE_VERSION);
        try {
            sdmpData.decodeMsgAuthoritativeEngineID(buffer);
            String deviceId = sdmpData.getMsgAuthoritativeEngineID().toString();
            Device device = DeviceContext.deviceMap.get(deviceId);
            sdmpData.setKey(device.getGenkey().getBytes());
            sdmpData.decodeBER(buffer);
        } catch (IOException e) {
            log.error("解码异常", e);
        }
        return sdmpData;
    }

    public int getMsgVersion(ByteBuf buffer) {
        Integer32 msgVersion = new Integer32(0);
        BER.MutableByte type = new BER.MutableByte();
        try {
            BER.decodeHeader(buffer, type);
            msgVersion.decodeBER(buffer);
            return msgVersion.toInt();
        } catch (IOException e) {
            buffer.readerIndex(buffer.capacity());
            return -1;
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf data = datagramPacket.content();
        SDMP sdmpv1 = parseCmessage(data);
        if (sdmpv1 != null) {
            out.add(sdmpv1);
        }
    }
}
