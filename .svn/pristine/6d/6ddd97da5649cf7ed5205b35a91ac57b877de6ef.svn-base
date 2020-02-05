package com.telek.hemsipc.protocal3761.service.request;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.telek.hemsipc.protocal3761.ProtocalFactory;
import com.telek.hemsipc.protocal3761.netty.NettyStarter;
import com.telek.hemsipc.protocal3761.protocal.BinPacket;
import com.telek.hemsipc.protocal3761.protocal.IProtocal;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.internal.Encoder;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.SegmentEnum;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class AbsRequest {

    private boolean isStartStation;
    /**
     * 数据
     */
    protected Map<String, Object> data = new HashedMap();

    public AbsRequest(boolean isStartStation) {
        this.isStartStation = isStartStation;
    }

    /**
     *
     * @param address2
     * @param ordinaryPacket  原始的packet
     * @return
     * @throws Exception
     */
    public Object dealSendToTerminal(String address2, Packet ordinaryPacket) throws Exception {
        IProtocal protocal = getProtocal(address2, isStartStation);
        Packet packet = getPacket(address2, ordinaryPacket);
        refreshControlCache(protocal, packet);
        sendToTerminal(protocal, packet);
        return getReturn();
    }

    /**
     * 获取编解码器
     * @param address2
     * @param isStartStation
     * @return
     */
    protected IProtocal getProtocal(String address2, boolean isStartStation){
        return ProtocalFactory.getProtocal(address2, isStartStation);
    }

    /**
     * 组装编码
     *
     * @param address2
     * @param ordinaryPacket
     * @return
     */
    protected abstract Packet getPacket(String address2, Packet ordinaryPacket);

    private void refreshControlCache(IProtocal protocal, Packet packet) {
        Control control = packet.getControl();
        if (control != null) {
            Encoder encoder = protocal.getEncoder();
            encoder.getPacketSegmentContext().getPacketSegmentMap().put(SegmentEnum.control, control);
        }
    }

    private void sendToTerminal(IProtocal protocal, Packet packet) throws Exception {
        if (protocal == null || packet == null) {
            log.error("null is now allowed");
            return;
        }
        BinPacket binPacket = new BinPacket();
        protocal.encode(packet, binPacket);
        ByteBuffer byteBuffer = binPacket.getByteBuffer();
        byte[] bs = byteBuffer.array();
        byteBuffer.clear();
        NettyStarter.send(packet.getAddress2(), bs);
    }

    private Object getReturn() {
        return null;
    }

}
