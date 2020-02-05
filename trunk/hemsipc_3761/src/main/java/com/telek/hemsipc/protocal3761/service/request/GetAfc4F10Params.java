package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 ** 获取终端电表配置数据单元格式
 */
public class GetAfc4F10Params extends AbsRequest {

    private int queryNum;

    private List<Integer> indexList;

    public GetAfc4F10Params(boolean isStartStation,List<Integer> indexList) {
        super(isStartStation);
        this.indexList = indexList;
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.GET_RESIDUAL_CURRENT_PROTECTOR_CONFIG_SEND_PARAMS.getCommand());
        data = new HashMap<>();
        if(indexList != null && indexList.size() > 0){
            data.put("queryNum",indexList.size());
            List list = new ArrayList();
            for(int index: indexList){
                Map<String,Integer> map = new HashMap<>();
                map.put("index",index);
                list.add(map);
            }
            data.put("list",list);
        }
        packet.setData(data);
        packet.setControl(getCommonControl());
        log.info("发送 AFN0A F10 帧");
        return packet;
    }
}
