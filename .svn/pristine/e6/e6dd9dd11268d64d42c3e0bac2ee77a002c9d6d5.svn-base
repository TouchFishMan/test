package com.telek.hemsipc.protocal3761.protocal.internal;

import java.nio.ByteBuffer;

import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.Constants;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Auxiliary;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Data;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Head;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.SegmentEnum;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Tail;
import com.telek.hemsipc.protocal3761.protocal.internal.unwrap.AuxUnwrapper;
import com.telek.hemsipc.protocal3761.protocal.internal.unwrap.CipherUnwrapper;
import com.telek.hemsipc.protocal3761.protocal.internal.unwrap.ControlUnwrapper;
import com.telek.hemsipc.protocal3761.protocal.internal.unwrap.DataUnwrapper;
import com.telek.hemsipc.protocal3761.protocal.internal.unwrap.HeadTailUnwrapper;
import com.telek.hemsipc.protocal3761.protocal.internal.unwrap.UnwrapperChain;


/**
 * Created by PETER on 2015/3/14.
 */
@lombok.Data
public class Decoder implements Constants {
    private PacketSegmentContext packetSegmentContext=new PacketSegmentContext();
    private ProtocalTemplate protocalTemplate;
    private CodecConfig codecConfig;
    private static final int SEQ_NOT_MATCH=2;
    private static final int TIMEOUT_REACH=4;
    private static final int HAVE_NEXT_FRAME=8;
    private static final int NEED_CONFIRM=16;
    private static final int INITIATIVE_UPLOAD=32;
    private static final int HAVE_EVENT=64;
    private static final int BROADCAST=128;
    //解码链
    private UnwrapperChain unwrapperChain=new UnwrapperChain();
    public Decoder(ProtocalTemplate protocalTemplate, CodecConfig codecConfig){
        this.protocalTemplate=protocalTemplate;
        this.codecConfig=codecConfig;
        //定义解码顺序
        unwrapperChain.add(new HeadTailUnwrapper());
        unwrapperChain.add(new ControlUnwrapper());
        unwrapperChain.add(new CipherUnwrapper());
        unwrapperChain.add(new DataUnwrapper());
        unwrapperChain.add(new AuxUnwrapper());

    }

    public int decode(final ByteBuffer in, Packet out, int pfc) throws Exception{
        unwrapperChain.decode(in,packetSegmentContext,protocalTemplate,codecConfig);
        int result=1;
        Control control=(Control)packetSegmentContext.getSegment(SegmentEnum.control);
        Auxiliary auxiliary=(Auxiliary)packetSegmentContext.getSegment(SegmentEnum.auxiliary);
        Data data=(Data)packetSegmentContext.getSegment(SegmentEnum.data);
        //设置解码结果
        out.setCommand(data.getCommand());
        out.setLine(data.getPn());
        out.setData(data.getData());
        out.setTerminalAddress(String.format("%s%s",control.getAddress1(),control.getAddress2()).toUpperCase());

        //设置返回值
        if(control.getPrm()==1 && control.getDir()==1){
            result=INITIATIVE_UPLOAD;
            if(control.getIsNeedConfirm()==1){
                result=NEED_CONFIRM;
            }
        }else{
            if(control.getSeq()!=(pfc&0x0f)){
                result=SEQ_NOT_MATCH;
            }
        }

        if(control.getTpV()==1){
            if(System.currentTimeMillis()-auxiliary.getSendTime()>auxiliary.getTimeout()*60000){
                result=TIMEOUT_REACH;
            }
        }

        if(control.getFir()==0){
            result=HAVE_NEXT_FRAME;
        }


        if(control.getFcbOrAcd()==1){
            result=HAVE_EVENT;
        }

        if(control.getIsGroup()==1){
            result=BROADCAST;
        }

        // 赋值
        out.setHead(((Head) packetSegmentContext.getSegment(SegmentEnum.head)).clone());
        out.setControl(((Control) packetSegmentContext.getSegment(SegmentEnum.control)).clone());
        out.setDataInfo(((Data) packetSegmentContext.getSegment(SegmentEnum.data)).clone());
        out.setAuxiliary(((Auxiliary)packetSegmentContext.getSegment(SegmentEnum.auxiliary)).clone());
        out.setTail(((Tail) packetSegmentContext.getSegment(SegmentEnum.tail)).clone());

        //清空编码结果
        packetSegmentContext.reset();
        return result;
    }
}
