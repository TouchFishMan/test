package com.telek.hemsipc.protocal3761.protocal.internal.packetSegment;

import lombok.Data;

/**
 * Created by PETER on 2015/3/25.
 */
@Data
public class Control extends Segment implements Cloneable {
    /**
     * 控制域功能码
     */
    private int functionCode;
    /**
     * 帧计数有效位
     * <entry key="1" value="fcb位有效"></entry>
     * <entry key="0" value="fcb位无效"></entry>
     */
    private int fcv = 0;
    /**
     * fcb帧计数位(下行)，acd要求访问位(上行)
     * <entry key="1" value="(下行)启用fcb，(上行)终端有重要事件"></entry>
     * <entry key="0" value="(下行)关闭fcb，(上行)终端无重要事件"></entry>
     */
    private int fcbOrAcd;
    /**
     * 启动标志位
     * <entry key="1" value="报文来自启动站"></entry>
     * <entry key="0" value="报文来自从动站"></entry>
     */
    private int prm;
    /**
     * 传输方向位
     * <entry key="1" value="上行报文"></entry>
     * <entry key="0" value="下行报文"></entry>
     */
    private int dir;
    /**
     * 行政区域码
     */
    private String address1;
    /**
     * 终端地址
     */
    private String address2;
    /**
     * 是否组地址
     * <entry key="1" value="组地址"></entry>
     * <entry key="0" value="单地址"></entry>
     */
    private int isGroup = 0;
    /**
     * 主站编码
     */
    private int msa;
    /**
     * 功能码
     */
    private int afn;
    /**
     * 帧序列
     */
    private int seq;
    /**
     * 是否需确认
     * <entry key="1" value="需要确认"></entry>
     * <entry key="0" value="不需要确认"></entry>
     */
    private int isNeedConfirm;
    /**
     * 是否报文第一帧
     * <entry key="1" value="报文第一帧"></entry>
     * <entry key="0" value="不是报文第一帧"></entry>
     */
    private int fin;
    /**
     * 是否报文最后一帧
     * <entry key="1" value="报文最后一帧"></entry>
     * <entry key="0" value="不是报文最后一帧"></entry>
     */
    private int fir;
    /**
     * 附加信息是否有时间标签
     * <entry key="1" value="有时间标签"></entry>
     * <entry key="0" value="无时间标签"></entry>
     */
    private int tpV;

    @Override
    public void reset() {
        functionCode = 0;
        fcbOrAcd = 0;
        prm = 0;
        dir = 0;
        address1 = "";
        address2 = "";
        isGroup = 0;
        msa = 0;
        afn = 0;
        seq = 0;
        isNeedConfirm = 0;
        fin = 0;
        fir = 0;
        tpV = 0;
        getBuffer().clear();
    }

    @Override
    public Control clone() {
        Control control = new Control();
        control.setFunctionCode(functionCode);
        control.setFcv(fcv);
        control.setFcbOrAcd(fcbOrAcd);
        control.setPrm(prm);
        control.setDir(dir);
        control.setAddress1(address1);
        control.setAddress2(address2);
        control.setIsGroup(isGroup);
        control.setMsa(msa);
        control.setAfn(afn);
        control.setSeq(seq);
        control.setIsNeedConfirm(isNeedConfirm);
        control.setFin(fin);
        control.setFir(fir);
        control.setTpV(tpV);
        return control;
    }
}
