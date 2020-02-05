package com.telek.hemsipc.protocal3761.service.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telek.hemsipc.protocal3761.datamodel.Afn4F10Data;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F10Dto;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.*;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;
import com.telek.hemsipc.util.StringUtil;


/**
 * 终端电能表/交流采样装置配置参数
 */
public class SetAfn4F10Params extends AbsRequest {

    public SetAfn4F10Params(boolean isStartStation, List<CommandAfn4F10Dto> afn4F10Dtos) {
        super(isStartStation);
        if (afn4F10Dtos == null || afn4F10Dtos.size() == 0) {
            throw new RuntimeException("SetAfn4F10Params 中data不能为null");
        } else {
            int size = afn4F10Dtos.size();
            List<Map<String, Object>> list = new ArrayList<>();
            for (CommandAfn4F10Dto dto : afn4F10Dtos) {
                Map<String, Object> temp = new HashMap<>();
                try {
                    temp = StringUtil.objectToMap(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                temp.put("protectorIndex", dto.getProtectorIndex());
//                temp.put("measuringPoint", dto.getMeasuringPoint());
//                temp.put("port", dto.getPort());
//                temp.put("speed", dto.getSpeed());
//                temp.put("protocalType", dto.getProtocalType());
//                temp.put("address", dto.getAddress());
//                temp.put("password", dto.getPassword());
                list.add(temp);
            }
            data = new HashMap<>();
            data.put("configNum", size);
            data.put("list", list);
        }

    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.RESIDUAL_CURRENT_PROTECTOR_CONFIG.getCommand());
        packet.setData(data);
        packet.setControl(getCommonControl());
        log.info("发送 AFN04 F10 帧");
        return packet;
    }

    private Map<String, Object> getTestData() {
        Afn4F10Data testData = new Afn4F10Data();
        testData.setAddress("12345678");
        testData.setMeasuringPoint(1);
        testData.setPort(2);
        testData.setProtocalType(ProcotolTypeConst.DLT645_2007);
        testData.setProtectorIndex(1);
        testData.setSpeed(SpeedConst.SPEED_2400);
        testData.setPassword("0");
        List<Afn4F10Data> datas = new ArrayList<>();
        datas.add(testData);
        Map<String, Object> result = new HashMap<>();
        result.put("configNum", datas.size());
        List<Map<String, Object>> list = new ArrayList<>();
        result.put("list", list);
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
        return result;
    }
}
