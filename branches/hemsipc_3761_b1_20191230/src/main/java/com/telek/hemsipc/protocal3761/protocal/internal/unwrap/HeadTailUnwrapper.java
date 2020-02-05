package com.telek.hemsipc.protocal3761.protocal.internal.unwrap;

import java.nio.ByteBuffer;

import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.exception.DecodingException;
import com.telek.hemsipc.protocal3761.protocal.exception.VerifyException;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Head;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.SegmentEnum;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Tail;


/**
 * Created by PETER on 2015/3/25.
 */
public class HeadTailUnwrapper extends Unwrapper{
    private static final int HEAD_LENGTH=6;
    private static final int TAIL_LENGTH=2;
    @Override
    public void decode(ByteBuffer in, PacketSegmentContext packetSegmentContext,
                       ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception{
        in.clear();
        if(in.limit()<6){
            throw new DecodingException(1111,"报文长度小于6");
        }
        byte[] headArray=new byte[HEAD_LENGTH];
        in.get(headArray);
        Head head=(Head)packetSegmentContext.getSegment(SegmentEnum.head);
        Tail tail=(Tail)packetSegmentContext.getSegment(SegmentEnum.tail);
        if(headArray[0]!=headArray[5] || !(headArray[0]==0x68 || headArray[0]==0x69)
                || headArray[1]!=headArray[3] || headArray[2]!=headArray[4]){
            throw new VerifyException(1111,"报文头校验错误");
        }
        head.setVersion(headArray[1] & 0x3);
        head.setLength(((headArray[1] & 0xff)>>2)+((headArray[2] & 0xff) << 6));
        if(in.limit()<head.getLength()+8){
            throw new DecodingException(1111,"报文长度为:"+(head.getLength()+8)+",实际长度为:"+in.limit());
        }
        byte[] tailArray=new byte[TAIL_LENGTH];
        byte checkSum=0;
        for(int i=0;i<head.getLength();i++){
            checkSum+=in.get();
        }
        in.get(tailArray);
        tail.setCheckSum(tailArray[0]);

        if(checkSum!=tailArray[0]){
            throw new VerifyException(1111,"校验值错误，计算值为:"+checkSum+",实际值为:"+tailArray[0]);
        }
        if(next!=null){
            next.decode(in,packetSegmentContext,protocalTemplate,codecConfig);
        }

    }

}
