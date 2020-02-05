package com.telek.hemsipc.service;

import com.telek.hemsipc.model.Concentrator;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F10Dto;
import com.telek.hemsipc.util.JsonResult;

import java.util.List;
import java.util.Map;

/**
 * @author wangxb
 * @date 20-1-5 下午9:42
 */
public interface IDeviceService {

    Map<String, Object> queryDeviceList(String name);

    JsonResult createConcentrator(Concentrator concentrator);

    JsonResult updateConcentrator(Concentrator concentrator);

    Boolean saveOrUpdateConcentrator(Concentrator concentrator);

    JsonResult deleteConcentrator(List<Integer> idList);

    JsonResult replaceConcentrator(Concentrator concentrator);

    JsonResult createElecMeter(Device device);

    JsonResult updateElecMeter(Device device);

    Boolean saveOrUpdateElecMeter(Device device);

    JsonResult bindElecMeter(String concentratorAddress, List<CommandAfn4F10Dto> f10Dtos);

    Boolean updateConcentratorTaskId(String address, int taskId);

    Boolean checkConcentratorTaskId(String address, int taskId);

    Boolean updateConcentratorBindPorts(String address, List<Integer> portList);

    JsonResult queryTaskIds(int id);

//    void deleteTask(int id, int taskId,String channelId);

    JsonResult getFnPnMapList(int concentratorId);
}
