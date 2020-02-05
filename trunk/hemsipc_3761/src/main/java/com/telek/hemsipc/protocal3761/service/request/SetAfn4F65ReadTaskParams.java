package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.dto.CommandAfn4F65Dto;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 ** 设置定时上报1类数据任务参数
 */
public class SetAfn4F65ReadTaskParams extends AbsRequest {

    private int taskId;

    public SetAfn4F65ReadTaskParams(boolean isStartStation, CommandAfn4F65Dto afn4F65Dto) {
        super(isStartStation);
        if(afn4F65Dto.getTaskList()==null || afn4F65Dto.getTaskList().isEmpty()){
            throw new RuntimeException("SetAfn4F65ReadTaskParams 中data不能为null");
        }
        taskId = afn4F65Dto.getTaskId();
        data = new HashMap<>();
        data.put("timingCycle", afn4F65Dto.getTimingCycle());
        data.put("timingCycleUnit",afn4F65Dto.getTimingCycleUnit());
        data.put("baseTime", afn4F65Dto.getBaseTime());
        data.put("rate", afn4F65Dto.getRate());
        data.put("taskNum",afn4F65Dto.getTaskList().size());
        List<Map> taskList = afn4F65Dto.getTaskList();
        //接收参数转换过程会把前端int转成double
        for(Map<String,Object> task: taskList){
            for(Map.Entry entry: task.entrySet()){
                if(entry.getValue() instanceof Double){
                    entry.setValue(((Double) entry.getValue()).intValue());
                }
            }
        }
        data.put("list",taskList);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.READ_TASK_PARAMS.getCommand());
        packet.setLine(taskId);
        packet.setData(data);
        packet.setControl(getCommonControl());
        log.info("准备发送F65帧："+packet);
        return packet;
    }

    private Map<String,Object> getTestData(){
        Map<String,Object> data = new HashMap<>();
            data = new HashMap<>();
            //上报周期，1字节，前6位周期，后2位单位（0-3分时日月）
            data.put("timingCycle", 1);
            data.put("timingCycleUnit",0);
//            data.put("baseTime", "199226010500");
            //上报基准时间，6字节，秒时分日年分别1字节，月+星期1字节（月份前5位，星期后3位）2019年12月26号1点整
            data.put("baseTime", "19/12/28 01:05:00 6");
            data.put("rate", 1);
            data.put("taskNum",1);
            //数据单元标识，DA+DT 4字节 文档4.3.4.4.2
            List<Map<String,Integer>> taskList = new ArrayList();
//            Map<String,String> f25task = new HashMap();
//            f25task.put("task","03010101"); //小端模式
//            Map<String,String> f129task = new HashMap<>();
//            f129task.put("task","10010101");
//            taskList.add(f25task);  //p0f25
//            taskList.add(f129task);  //p0f129
            Map<String,Integer> f25Task = new HashMap<>();
            f25Task.put("pn",2);
            f25Task.put("fn",25);
//            Map<String,Integer> f129Task = new HashMap<>();
//            f129Task.put("pn",1);
//            f129Task.put("fn",129);
            taskList.add(f25Task);
//            taskList.add(f129Task);
            data.put("list",taskList);
            return data;
    }
}
