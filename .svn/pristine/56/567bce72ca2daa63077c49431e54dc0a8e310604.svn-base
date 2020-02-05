package com.telek.hemsipc.protocal3761.protocal.internal.wrap;


import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Data;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.SegmentEnum;

/**
 * Created by PETER on 2015/5/5.
 */
public class CipherWrapper extends Wrapper{

    @Override
    void encode(Packet in, PacketSegmentContext packetSegmentContext, ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception {
        Control control=(Control)packetSegmentContext.getSegment(SegmentEnum.control);
        Data dataSeg=(Data)packetSegmentContext.getSegment(SegmentEnum.data);
        int afn=control.getAfn();

        /*if(afn==0x04 || afn==0x05){
            List<byte[]> body=dataSeg.getBuffer();
            //加密
            List<byte[]> crypted=body;
            dataSeg.getBuffer().clear();
            dataSeg.getBuffer().addAll(crypted);
        }*/
        if(next!=null){
            next.encode(in,packetSegmentContext,protocalTemplate,codecConfig);
        }
    }
}
