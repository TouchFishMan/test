package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.datamodel.Afn4F33Data;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F33Dto;
import com.telek.hemsipc.protocal3761.dto.F33ReadSettingParams;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * * 设置终端抄读表参数
 */
public class SetAfn4F33ReadParams extends AbsRequest {

    public SetAfn4F33ReadParams(boolean isStartStation, List<CommandAfn4F33Dto> afn4F33DtoList) {
        super(isStartStation);
        List<Map<String, Object>> list = new ArrayList<>();
        data = new HashMap<>();
        if (afn4F33DtoList == null || afn4F33DtoList.isEmpty()) {
            throw new RuntimeException("SetAfn4F33ReadParams 中data不能为null");
        }
        data.put("configNum", afn4F33DtoList.size());
        data.put("list", list);
        for (CommandAfn4F33Dto dto : afn4F33DtoList) {
            Map<String, Object> map = new HashMap();
            map.put("terminalPort", dto.getTerminalPort());
            F33ReadSettingParams readSettingParams = dto.getReadSetting();
            if (dto.getReadSetting() == null) {
                readSettingParams = new F33ReadSettingParams();
            }
            map.put("notAllowAutoRead", readSettingParams.getNotAllowAutoRead());
            map.put("readImportantMeterOnly", readSettingParams.getReadImportantMeterOnly());
            map.put("broadcast", readSettingParams.getBroadcast());
            map.put("checkTimeRegular", readSettingParams.getCheckTimeRegular());
            map.put("updateNewMeter", readSettingParams.getUpdateNewMeter());
            map.put("readMeterStatus", readSettingParams.getReadMeterStatus());
//            map.put("readDate",dto.getReadDate());   基本不改的字段暂时写死，放dto里接收参数时候容易报错
            map.put("readDate", "7fffffff");
            map.put("readTime", dto.getReadTime());
            map.put("intervalTime", dto.getIntervalTime());
            map.put("checkTime", dto.getCheckTime());
//            map.put("allowReadTimeNum",dto.getAllowReadTimeNum());
            map.put("allowReadTimeNum", 1);
//            map.put("list",dto.getAllowReadTimePeriod());
            map.put("list", getDefaultAllowReadTime());
            list.add(map);
        }
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.READ_RUNNING_PARAMS.getCommand());
        packet.setData(data);
        packet.setControl(getCommonControl());
        log.info("准备发送F33帧：" + packet);
        return packet;
    }

    private List<Map> getDefaultAllowReadTime() {
        List<Map> list = new ArrayList<>();
        Map readTimeMap = new HashMap();
        readTimeMap.put("allowReadTimeStart", "00:00");
        readTimeMap.put("allowReadTimeEnd", "24:00");
        list.add(readTimeMap);
        return list;
    }

    private Map<String, Object> getTestData() {
        Afn4F33Data data = new Afn4F33Data();
        Map<String, Object> result = new HashMap<>();
        result.put("configNum", 1);
        List<Map<String, Object>> list = new ArrayList<>();
        result.put("list", list);
        Map map = new HashMap();
        map.put("terminalPort", data.getTerminalPort());
        map.put("notAllowAutoRead", data.getNotAllowAutoRead());
        map.put("readImportantMeterOnly", data.getReadImportantMeterOnly());
        map.put("broadcast", data.getBroadcast());
        map.put("checkTimeRegular", data.getCheckTimeRegular());
        map.put("updateNewMeter", data.getUpdateNewMeter());
        map.put("readMeterStatus", data.getReadMeterStatus());
        map.put("readDate", data.getReadDate());
        map.put("readTime", data.getReadTime());
        map.put("intervalTime", data.getIntervalTime());
        map.put("checkTime", data.getCheckTime());
        map.put("allowReadTimeNum", data.getAllowReadTimeNum());
        List<Map<String, String>> readTimeList = new ArrayList<>();
        Map readTimeMap = new HashMap();
        readTimeMap.put("allowReadTimeStart", "00:00");
        readTimeMap.put("allowReadTimeEnd", "24:00");
        readTimeList.add(readTimeMap);
        map.put("list", readTimeList);
        list.add(map);
        return result;
    }
}
