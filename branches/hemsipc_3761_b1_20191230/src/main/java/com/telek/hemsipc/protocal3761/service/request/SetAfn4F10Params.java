package com.telek.hemsipc.protocal3761.service.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.datamodel.Afn4F10Data;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;


/**
 * 终端电能表/交流采样装置配置参数
 */
public class SetAfn4F10Params extends AbsRequest {

    public SetAfn4F10Params(boolean isStartStation, List<Afn4F10Data> datas) {
        super(isStartStation);
        if (datas != null && datas.size() != 0) {
            data = new HashMap<>();
            data.put("configNum", datas.size());
            List<Map<String, Object>> list = new ArrayList<>();
            data.put("list", list);
            for (Afn4F10Data data : datas) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("protectorIndex", data.getProtectorIndex());
                temp.put("measuringPoint", data.getMeasuringPoint());
                temp.put("port", data.getPort());
                temp.put("speed", data.getSpeed().getCode());
                temp.put("protocalType", data.getProtocalType().getCode());
                temp.put("address", data.getAddress());
                temp.put("password", data.getPassword());
                list.add(temp);
            }
        } else {
            throw new RuntimeException("SetAfn4F10Params中data不能为null");
        }
    }

    @Override
    protected Packet getPacket(String address2, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.get(address2));
        packet.setCommand(CommandAfn.RESIDUAL_CURRENT_PROTECTOR_CONFIG.getCommand());
        packet.setData(data);

        Control control = new Control();
        control.setFunctionCode(FunctionCode.PRM_START_REQUEST_RESPONSE_1);
        control.setPrm(CommConst.PRM_START_STATION);
        control.setDir(CommConst.DIR_DOWN);
        control.setFin(1);
        control.setFir(1);

        packet.setControl(control);

        return packet;
    }
}
