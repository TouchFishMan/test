package com.telek.hemsipc.protocal3761.service.request;

import java.nio.ByteBuffer;
import java.util.Map;

import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import net.spy.memcached.compat.log.Logger;
import net.spy.memcached.compat.log.LoggerFactory;
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

@Data
public abstract class AbsRequest {

    static Logger log = LoggerFactory.getLogger(AbsRequest.class);

    private boolean isStartStation;
    /**
     * 数据
     */
    protected Map<String, Object> data = new HashedMap();

    public AbsRequest(boolean isStartStation) {
        this.isStartStation = isStartStation;
    }

    /**
     * @param address
     * @param ordinaryPacket 原始的packet
     * @return
     * @throws Exception
     */
    public byte[] dealSendToTerminal(String address, Packet ordinaryPacket) throws Exception {
        IProtocal protocal = getProtocal(address, isStartStation);
        Packet packet = getPacket(address, ordinaryPacket);
        refreshControlCache(protocal, packet);
        byte[] sendData = sendToTerminal(protocal, packet);
        return sendData;
//        return getReturn();
    }

    /**
     * 获取编解码器
     *
     * @param address
     * @param isStartStation
     * @return
     */
    protected IProtocal getProtocal(String address, boolean isStartStation) {
        return ProtocalFactory.getProtocal(address, isStartStation);
    }

    protected Control getCommonControl() {
        Control control = new Control();
        control.setFunctionCode(FunctionCode.PRM_START_REQUEST_RESPONSE_1);
        control.setPrm(CommConst.PRM_START_STATION);
        control.setDir(CommConst.DIR_DOWN);
        control.setFin(1);
        control.setFir(1);
        return control;
    }

    /**
     * 组装编码
     *
     * @param address
     * @param ordinaryPacket
     * @return
     */
    protected abstract Packet getPacket(String address, Packet ordinaryPacket);

    private void refreshControlCache(IProtocal protocal, Packet packet) {
        Control control = packet.getControl();
        if (control != null) {
            Encoder encoder = protocal.getEncoder();
            encoder.getPacketSegmentContext().getPacketSegmentMap().put(SegmentEnum.control, control);
        }
    }

    private byte[] sendToTerminal(IProtocal protocal, Packet packet) throws Exception {
        if (protocal == null || packet == null) {
            log.error("protocal null is now allowed");
            return null;
        }
        BinPacket binPacket = new BinPacket();
        protocal.encode(packet, binPacket);
        ByteBuffer byteBuffer = binPacket.getByteBuffer();
        byte[] bs = byteBuffer.array();
        byteBuffer.clear();
        NettyStarter.send(packet.getTerminalAddress(), bs);
        return bs;
    }

    private Object getReturn() {
        return null;
    }

    public static void test() {
        try {
            String address = "12345678";
//            SetAfn4F4MainStationIp mainStationIp = new SetAfn4F4MainStationIp(true);
//            mainStationIp.dealSendToTerminal(address,null);
//            GetRequest getRequest = new GetRequest(true);
//            getRequest.dealSendToTerminal(address,null);
//            SetAfn4F10Params f10Params = new SetAfn4F10Params(true,new ArrayList<Afn4F10Data>());
//            f10Params.dealSendToTerminal(address, null);
//            SetAfn4F33ReadParams f33ReadParams = new SetAfn4F33ReadParams(true,new Afn4F33Data());
//            f33ReadParams.dealSendToTerminal(address,null);
//            SetAfn4F65ReadTaskParams f65ReadTaskParams = new SetAfn4F65ReadTaskParams(true);
//            f65ReadTaskParams.dealSendToTerminal(address,null);
//            SetAfn4F67ReadTaskOnOff f67ReadTaskOnOff = new SetAfn4F67ReadTaskOnOff(true);
//            f67ReadTaskOnOff.dealSendToTerminal(address,null);
//            SetAfn4F7TerminalIp f7TerminalIp = new SetAfn4F7TerminalIp(true,new Afn4F7Data());
//            f7TerminalIp.dealSendToTerminal(address,null);
//            SetAfn4F25MeasurePointBaseParams f25MeasurePointBaseParams = new SetAfn4F25MeasurePointBaseParams(true,new Afn4F25Data());
//            f25MeasurePointBaseParams.dealSendToTerminal(address,null);
//            SetAfn4F240TerminalAddress f240TerminalAddress = new SetAfn4F240TerminalAddress(true);
//            f240TerminalAddress.dealSendToTerminal(address,null);
//            GetAfc4F33ReadParams getAfc4F33ReadParams = new GetAfc4F33ReadParams(true);
//            getAfc4F33ReadParams.dealSendToTerminal(address,null);
//            GetAfc4F10Params getAfc4F10Params = new GetAfc4F10Params(true);
//            getAfc4F10Params.dealSendToTerminal(address,null);
//            GetAfc0CF129ElecData afc0CF129ElecData = new GetAfc0CF129ElecData(true,1);
//            afc0CF129ElecData.dealSendToTerminal("12345678",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
