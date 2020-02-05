package com.telek.hemsipc.protocal3761.protocal;

import java.util.HashMap;
import java.util.Map;

import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Auxiliary;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Data;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Head;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Tail;

/**
 * Created by PETER on 2015/3/13.
 */
@lombok.Data
public class Packet {
    /**
     * 线路编号，0为终端本身
     */
    private Integer line;

    /**
     * 终端号
     */
    private String terminalAddress;

    /**
     * 命令名
     */
    private String command;

    /**
     * 数据
     */
    private Map<String,Object> data;

    private Head head;
    private Control control;
    private Data dataInfo;
    private Auxiliary auxiliary;
    private Tail tail;

    public Packet(){
        data=new HashMap<>();
    }

    public Packet(String command, HashMap<String, Object> data) {
        this.command = command;
        this.data = data;
    }

    public Packet(Integer line, String command, Map<String, Object> data) {
        this.line = line;
        this.command = command;
        this.data = data;
    }
}
