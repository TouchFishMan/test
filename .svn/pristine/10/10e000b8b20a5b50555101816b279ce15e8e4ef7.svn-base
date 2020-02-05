package com.telek.hemsipc.protocal3761.protocal.internal.wrap;

import java.util.List;

import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.exception.EncodingException;
import com.telek.hemsipc.protocal3761.protocal.exception.Preconditions;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.FieldGroup;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.FieldTypeParam;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.fieldType.HexString;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Data;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.SegmentEnum;


/**
 * Created by PETER on 2015/3/24.
 */
public class ControlWrapper extends Wrapper{
    private HexString hexString=new HexString();
    private FieldTypeParam address1Param=new FieldTypeParam(2);
    private FieldTypeParam address2Param=new FieldTypeParam(2);
    private static final String MULTIPLECOMMAND="multipleCommand";


    /**
     * 设置参数值
     * @param in
     * @param control
     * @param protocalTemplate
     * @throws Exception
     */
    public void beforeEncode(Packet in, Control control, Data dataSeg,
                             ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception{
        FieldGroup dataGroup=null;
        if(in.getCommand().equals(MULTIPLECOMMAND)){
            String command=in.getData().keySet().iterator().next();
            dataGroup=protocalTemplate.getDataMapByName().get(command);
        }else{
            dataGroup=protocalTemplate.getDataMapByName().get(in.getCommand());
        }

        if(dataGroup==null){
            throw new EncodingException(1111,"不支持命令号:"+in.getCommand());
        }

        int afn=dataGroup.getAfn();
        control.setAfn(afn);
        dataSeg.setFn(dataGroup.getFn());
        //线路设置
        if(in.getLine()==null){
            dataSeg.setPn(0);
        }else{
            dataSeg.setPn(in.getLine());
        }

        //设置是否需要确认
        if(afn==0x01 || afn==0x04 || afn==0x05 || afn==0x0F){
            control.setIsNeedConfirm(1);
        }else {
            control.setIsNeedConfirm(0);
        }

        //终端地址设置
        if(in.getTerminalAddress()==null){
            Preconditions.checkNotNull(codecConfig.getTerminalAddress(), 1111, "终端地址为空");
            control.setAddress1(codecConfig.getTerminalAddress().substring(0,4).toUpperCase());
            control.setAddress2(codecConfig.getTerminalAddress().substring(4).toUpperCase());
        }else{
            control.setAddress1(in.getTerminalAddress().substring(0, 4).toUpperCase());
            control.setAddress2(in.getTerminalAddress().substring(4).toUpperCase());
        }

        //设置广播地址
        if("FFFF".equals(control.getAddress1()) && "FFFF".equals(control.getAddress2())){
            control.setIsGroup(1);
        }else{
            control.setIsGroup(0);
        }
        //主站地址设置
        control.setMsa(codecConfig.getMsa());
        //是否有时间标识
        control.setTpV(codecConfig.isHaveTp() ? 1 : 0);

    }

    /**
     *
     * @param in
     * @param packetSegmentContext
     * @param protocalTemplate
     * @throws Exception
     */
    @Override
    public void encode(Packet in, PacketSegmentContext packetSegmentContext,
                       ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception{
        Control control=(Control)packetSegmentContext.getSegment(SegmentEnum.control);
        Data dataSeg=(Data)packetSegmentContext.getSegment(SegmentEnum.data);
        beforeEncode(in, control, dataSeg, protocalTemplate, codecConfig);
        List<byte[]> buffer=control.getBuffer();
        //控制域功能码,帧计数有效位,fcb帧计数位,启动标志位,传输方向位 编码
        buffer.add(new byte[]{(byte) (control.getFunctionCode()+
                (control.getFcv()<<4)+(control.getFcbOrAcd()<<5)+(control.getPrm()<<6)+(control.getDir()<<7))});
        //地址编码
        address1Param.setLength(codecConfig.getDistinctLength());
        buffer.add(hexString.encode(control.getAddress1(),address1Param));
        address2Param.setLength(codecConfig.getTerminalAddressLength());
        buffer.add(hexString.encode(control.getAddress2(),address2Param));
        //主站地址，afn编码
        buffer.add(new byte[]{(byte) (control.getIsGroup()+(control.getMsa()<<1)),
                (byte)control.getAfn()});
        //帧序列，是否需确认，是否报文第一帧，是否报文最后一帧，附加信息是否有时间标签 编码
        buffer.add(new byte[]{(byte) ((control.getSeq()&0x0f)+(control.getIsNeedConfirm()<<4)+
                        (control.getFin()<<5)+(control.getFir()<<6)+(control.getTpV()<<7))});

        if(next!=null){
            next.encode(in,packetSegmentContext,protocalTemplate,codecConfig);
        }

    }

}
