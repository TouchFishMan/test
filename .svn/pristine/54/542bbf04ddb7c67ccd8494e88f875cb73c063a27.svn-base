package com.telek.hemsipc.protocal3761.protocal.internal.wrap;

import java.util.Iterator;
import java.util.List;

import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Head;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Segment;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.SegmentEnum;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Tail;


/**
 * Created by PETER on 2015/3/24.
 */
public class HeadTailWrapper extends Wrapper{

    @Override
    public void encode(Packet in, PacketSegmentContext packetSegmentContext, ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception {
        Head head=(Head)packetSegmentContext.getSegment(SegmentEnum.head);
        Tail tail=(Tail)packetSegmentContext.getSegment(SegmentEnum.tail);
        byte checkSum=0;
        int length=0;
        Iterator<Segment> segmentIterator=packetSegmentContext.getIterator();
        while (segmentIterator.hasNext()){
            Segment segment=segmentIterator.next();
            for(byte[] bs:segment.getBuffer()){
                length+=bs.length;
                for(byte b:bs){
                    checkSum+=b;
                }
            }
        }
        head.setLength(length);
        List<byte[]> headBuffer=head.getBuffer();
        List<byte[]> tailBuffer=tail.getBuffer();

        //标记头
        byte headTag=0x68;
        Control control=(Control)packetSegmentContext.getSegment(SegmentEnum.control);
        int afn=control.getAfn();
        if(afn==0x04 || afn==0x05) {
            // headTag=0x69;
        }

        headBuffer.add(new byte[]{headTag,(byte)(head.getVersion()+(length<<2)),(byte)(length>>6),
                (byte)(head.getVersion()+(length<<2)),(byte)(length>>6),headTag});
        tailBuffer.add(new byte[]{checkSum,0x16});

        if(next!=null){
            next.encode(in,packetSegmentContext,protocalTemplate,codecConfig);
        }
    }
}
