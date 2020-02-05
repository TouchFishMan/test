package com.telek.hemsipc.service.impl;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F10Dto;
import com.telek.hemsipc.protocal3761.dto.ConcentratorDto;
import com.telek.hemsipc.model.Concentrator;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.constant.Afn0AFnEnum;
import com.telek.hemsipc.protocal3761.service.ProtocolCommandService;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.repository.IDeviceDao;
import com.telek.hemsipc.service.IDeviceService;
import com.telek.hemsipc.util.JsonResult;
import com.telek.hemsipc.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wangxb
 * @date 20-1-5 下午9:44
 */
@Service
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    ICommonDAO commonDAO;
    @Autowired
    IDeviceDao deviceDao;
    @Autowired
    ProtocolCommandService protocolCommandService;

    /**
     * 查询集中器、电表列表
     *
     * @param name
     * @return
     */
    @Override
    public Map<String, Object> queryDeviceList(String name) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Concentrator> concentratorList = deviceDao.queryConcentratorByParams(name, null);
        if (concentratorList == null || concentratorList.size() == 0) {
            return resultMap;
        }
        List<ConcentratorDto> concentratorDtoList = new ArrayList<>();
        int elecMeterCount = deviceDao.countElecMeter();
        List<Device> elecMeterList = deviceDao.queryElecMeterWithSlaveAdd();
        for (Concentrator concentrator : concentratorList) {
            ConcentratorDto concentratorDto = new ConcentratorDto();
            concentratorDto.setId(concentrator.getId());
            concentratorDto.setAddress(concentrator.getAddress());
            concentratorDto.setBrand(concentrator.getBrand());
            concentratorDto.setName(concentrator.getName());
            concentratorDto.setIsOnline(Protocol3761Cache.TERMINAL_ADDRESS_LOGIN.containsKey(concentrator.getAddress()));
            concentratorDto.setTaskIds(concentrator.getTaskIds());
            List<Device> list = new ArrayList<>();
            for (Device elecMeter : elecMeterList) {
                if (concentrator.getAddress().equals(elecMeter.getSlaveAdd())) {
                    list.add(elecMeter);
                }
            }
            concentratorDto.setElecMeterList(list);
            concentratorDtoList.add(concentratorDto);
        }
        resultMap.put("list", concentratorDtoList);
        resultMap.put("restlogicElecMeter", elecMeterList == null ? elecMeterCount : elecMeterCount - elecMeterList.size());
        return resultMap;
    }

    /**
     * 新增集中器，默认未设置任何配置
     *
     * @param concentrator
     * @return
     */
    @Override
    public JsonResult createConcentrator(Concentrator concentrator) {
        if (concentrator == null || StringUtil.isBlank(concentrator.getAddress())) {
            return new JsonResult(false, 10002, "集中器地址不能为空");
        }
        List<Concentrator> concentratorList = deviceDao.queryConcentratorByParams(null, concentrator.getAddress());
        if (concentratorList != null && concentratorList.size() > 0) {
            return new JsonResult(false, 10002, "该集中器地址已存在");
        }
        concentratorList = deviceDao.queryConcentratorByDeleteAndAddress(concentrator.getAddress());
        //新增的集中器地址跟之前被删的一样，当做就是之前的集中器，将其还原，保留查询F33用的bindPorts和查询F65用的taskIds
        if (concentratorList != null && concentratorList.size() > 0) {
            Concentrator deleteConcentrator = concentratorList.get(0);
            deleteConcentrator.setName(concentrator.getName());
            deleteConcentrator.setBrand(concentrator.getBrand());
            deleteConcentrator.setIsDelete(0);
            commonDAO.update(deleteConcentrator);
            return new JsonResult(true);
        }
        //新增集中器
        concentrator.setEnable(1);
        concentrator.setIsDelete(0);
        concentrator.setCreatedAt(new Date());
        if (saveOrUpdateConcentrator(concentrator)) {
            return new JsonResult(true);
        }
        return new JsonResult(false, 10003, "新增失败");
    }

    @Override
    public JsonResult updateConcentrator(Concentrator concentrator) {
        List<Concentrator> concentratorList = deviceDao.queryConcentratorById(concentrator.getId());
        if (concentratorList == null || concentratorList.size() == 0) {
            return new JsonResult(false, 10002, "该集中器不存在");
        }
        Concentrator updateConcentrator = concentratorList.get(0);
        //清理旧地址的查询帧缓存
        clearCommandCacheData(updateConcentrator.getAddress());
        updateConcentrator.setName(concentrator.getName());
        updateConcentrator.setBrand(concentrator.getBrand());
        updateConcentrator.setAddress(concentrator.getAddress());
        if (saveOrUpdateConcentrator(updateConcentrator)) {
            return new JsonResult(true);
        }
        return new JsonResult(false, 10003, "保存失败");
    }

    /**
     * 更新逻辑电表中集中器的地址
     *
     * @param oldAddress
     * @param newAddress
     */
    private void updateSlaveAddress(String oldAddress, String newAddress) {
        if (!newAddress.equals(newAddress)) {
            List<Device> elecMeterList = deviceDao.queryElecMeterBySlaveAdd(newAddress);
            if (elecMeterList != null && elecMeterList.size() > 0) {
                for (Device elecMeter : elecMeterList) {
                    elecMeter.setSlaveAdd(newAddress);
                }
                commonDAO.saveOrUpdateAll(elecMeterList);
            }
        }
    }

    @Override
    public Boolean saveOrUpdateConcentrator(Concentrator concentrator) {
        concentrator = deviceDao.saveOrUpdateConcentrator(concentrator);
        return concentrator != null && concentrator.getId() != null;
    }

    @Override
    public JsonResult deleteConcentrator(List<Integer> idList) {
        if (idList == null || idList.size() == 0) {
            return new JsonResult(true);
        }
        for (Integer id : idList) {
            List<Concentrator> concentratorList = deviceDao.queryConcentratorById(id);
            if (concentratorList == null || concentratorList.size() == 0) {
                return new JsonResult(false, 10003, "id为" + id + "的集中器不存在");
            }
            deviceDao.deleteConcentrator(id);
            List<Device> elecMeterList = deviceDao.queryElecMeterBySlaveAdd(concentratorList.get(0).getAddress());
            if (elecMeterList != null || elecMeterList.size() > 0) {
                recoverElecMeterList(elecMeterList);
            }
            clearCommandCacheData(concentratorList.get(0).getAddress());
        }
        return new JsonResult(true);
    }

    /**
     * 更换集中器，之前绑定的电表id保留，用于查询F33、F65的bindPorts、taskIds清除
     *
     * @return
     */
    @Override
    public JsonResult replaceConcentrator(Concentrator concentrator) {
        if (concentrator == null || concentrator.getId() == null) {
            return new JsonResult(false, 10002, "参数错误");
        }
        List<Concentrator> oldConcentratorList = deviceDao.queryConcentratorById(concentrator.getId());
        if (oldConcentratorList == null || oldConcentratorList.size() == 0) {
            return new JsonResult(false, 10003, "原集中器不存在");
        }
        Concentrator oldConcentrator = oldConcentratorList.get(0);
        concentrator.setId(oldConcentrator.getId());
        concentrator.setCreatedAt(new Date());
        concentrator.setIsDelete(0);
        commonDAO.update(concentrator);
        updateSlaveAddress(oldConcentrator.getAddress(), concentrator.getAddress());
        clearCommandCacheData(concentrator.getAddress());
        return new JsonResult(true);
    }

    @Override
    public JsonResult createElecMeter(Device device) {
//        if (device == null || StringUtil.isBlank(device.getDeviceId())) {
//            return new JsonResult(false, 10002, "电表ID不能为空");
//        }
//        if (saveOrUpdateElecMeter(device)) {
//            return new JsonResult(true);
//        }
        return new JsonResult(false, 10003, "新增失败");
    }

    @Override
    public JsonResult updateElecMeter(Device device) {
//        List deviceList = deviceDao.queryElecMeterByDeviceId(device.getDeviceId());
//        if (deviceList == null || deviceList.size() == 0) {
//            return new JsonResult(false, 10002, "该电表不存在");
//        }
//        if (saveOrUpdateElecMeter(device)) {
//            return new JsonResult(true);
//        }
        return new JsonResult(false, 10003, "保存失败");
    }

    @Override
    public Boolean saveOrUpdateElecMeter(Device device) {
        device = deviceDao.saveOrUpdateElecMeter(device);
        return device != null && device.getDeviceId() != null;
    }

    /**
     * 设置f10电表参数后重新分配逻辑电表，更新设备表中name、slaveAdd，必须保证同个集中器中各个MeasuringPoint不同
     *
     * @param concentratorAddress
     * @param f10Dtos
     * @return
     */
    @Override
    public JsonResult bindElecMeter(String concentratorAddress, List<CommandAfn4F10Dto> f10Dtos) {
        List<Device> oldDeviceList = deviceDao.queryElecMeterBySlaveAdd(concentratorAddress);
        List<Device> updateDeviceList = new ArrayList<>();
        Set<Device> needDeleteSet = new HashSet<>(oldDeviceList);
        for (CommandAfn4F10Dto f10Dto : f10Dtos) {
            for (Device oldDevice : oldDeviceList) {
                //已存在的电表，编辑电表名等字段
                if (oldDevice.getDeviceId().equals(f10Dto.getDeviceId())) {
                    needDeleteSet.remove(oldDevice);
                    oldDevice.setDeviceName(f10Dto.getName());
                    oldDevice.setDetail(f10Dto.getLocation());
                    oldDevice.setSlaveMachineNum(f10Dto.getMeasuringPoint());
                    updateDeviceList.add(oldDevice);
                    break;
                }
            }
            //新增的电表，分配逻辑电表
            if (StringUtil.isBlank(f10Dto.getDeviceId())) {
                List<Device> unBindDeviceList = deviceDao.queryOneUnBindElecMeter();
                if (unBindDeviceList == null || unBindDeviceList.size() == 0) {
                    return new JsonResult(false, 10002, "可分配逻辑电表数量不足");
                } else {
                    Device unBindDevice = unBindDeviceList.get(0);
                    unBindDevice.setDeviceName(f10Dto.getName());
                    unBindDevice.setDetail(f10Dto.getLocation());
                    unBindDevice.setSlaveAdd(concentratorAddress);
                    unBindDevice.setSlaveMachineNum(f10Dto.getMeasuringPoint());
                    Device newDevice = (Device) commonDAO.saveOrUpdate(unBindDevice);
                    updateDeviceCache(concentratorAddress, newDevice);
                }
            }
        }
        //更新需要更换电表名等字段的电表
        if (updateDeviceList.size() > 0) {
            commonDAO.saveOrUpdateAll(updateDeviceList);
        }
        //解除与集中器绑定的电表，还原逻辑电表
        if (needDeleteSet.size() > 0) {
            Iterator<Device> it = needDeleteSet.iterator();
            while (it.hasNext()) {
                Device deleteElecMeter = it.next();
                recoverElecMeter(deleteElecMeter);
                Protocol3761Cache.TERMINAL_ADDRESS_LINE_DEVICE_ID.remove(concentratorAddress + deleteElecMeter.getSlaveMachineNum());
                DeviceContext.deviceMap.remove(deleteElecMeter.getDeviceId());
            }
            commonDAO.saveOrUpdateAll(needDeleteSet);
        }
        return new JsonResult(true);
    }

    private void updateDeviceCache(String terminalAddress, Device elecMeter) {
        Protocol3761Cache.TERMINAL_ADDRESS_LINE_DEVICE_ID.put(terminalAddress + elecMeter.getSlaveMachineNum(), elecMeter.getDeviceId());
        DeviceContext.deviceMap.put(elecMeter.getDeviceId(), elecMeter);
        //启动msgid+100
        elecMeter.setMsgId(elecMeter.getMsgId() + 100);
        commonDAO.saveOrUpdate(elecMeter);
    }

    /**
     * 设置F65后更新集中器表中配置任务字段，暂时没删除任务功能，taskId字段只增不减
     *
     * @param address
     * @param taskId
     * @return
     */
    @Override
    public Boolean updateConcentratorTaskId(String address, int taskId) {
        List<Concentrator> concentratorList = deviceDao.queryConcentratorByParams(null, address);
        if (concentratorList == null || concentratorList.size() == 0) {
            return false;
        }
        Concentrator concentrator = concentratorList.get(0);
        String taskIdStr = concentrator.getTaskIds();
        if (checkTaskId(concentrator, taskId)) {
            return true;
        } else {
            if (StringUtil.isBlank(taskIdStr)) {
                taskIdStr = StringUtil.toString(taskId);
            } else {
                taskIdStr = taskIdStr + Constant.SPLIT_SIGN_COMMA + taskId;
            }
        }
        concentrator.setTaskIds(taskIdStr);
        return commonDAO.update(concentrator);
    }

    public Boolean checkConcentratorTaskId(String address, int taskId) {
        List<Concentrator> concentratorList = deviceDao.queryConcentratorByParams(null, address);
        if (concentratorList == null || concentratorList.size() == 0) {
            return false;
        }
        return checkTaskId(concentratorList.get(0), taskId);
    }

    private boolean checkTaskId(Concentrator concentrator, int taskId) {
        String newTaskId = StringUtil.toString(taskId);
        String taskIdStr = concentrator.getTaskIds();
        if (StringUtil.isBlank(taskIdStr)) {
            return false;
        }
        String[] taskIds = taskIdStr.split(Constant.SPLIT_SIGN_COMMA);
        if (taskIds.length > 0) {
            boolean isNew = true;
            for (int i = 0; i < taskIds.length; i++) {
                if (newTaskId.equals(taskIds[i])) {
                    isNew = false;
                }
            }
            if (isNew) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 设置F33后把设置的端口存下来，读取F33的时候做参数传给集中器
     *
     * @param address
     * @param portList
     * @return
     */
    public Boolean updateConcentratorBindPorts(String address, List<Integer> portList) {
        List<Concentrator> concentratorList = deviceDao.queryConcentratorByParams(null, address);
        if (concentratorList == null || concentratorList.size() == 0) {
            return false;
        }
        Concentrator concentrator = concentratorList.get(0);
        StringBuffer portBuf = new StringBuffer();
        for (int port : portList) {
            portBuf.append(port).append(Constant.SPLIT_SIGN_COMMA);
        }
        if (portBuf.length() > 0) {
            portBuf.substring(0, portBuf.length() - 1);
        }
        concentrator.setBindPorts(portBuf.toString());
        return commonDAO.update(concentrator);
    }

    @Override
    public JsonResult queryTaskIds(int id) {
        List<Concentrator> concentratorList = deviceDao.queryConcentratorById(id);
        if (concentratorList == null || concentratorList.size() == 0) {
            return new JsonResult(false, 10003, "id为" + id + "的集中器不存在");
        }
        String taskIds = concentratorList.get(0).getTaskIds();
        String[] taskIdArr = StringUtil.isBlank(taskIds) ? null : taskIds.split(Constant.SPLIT_SIGN_COMMA);
        return new JsonResult(true, 0, taskIdArr);
    }

//    @Override
//    public void deleteTask(int id, int taskId, String channelId) {
//        List<Concentrator> concentratorList = deviceDao.queryConcentratorById(id);
//        if (concentratorList == null || concentratorList.size() == 0) {
//            return;
//        }
//        Concentrator concentrator = concentratorList.get(0);
//        protocolCommandService.deleteTask(concentrator.getAddress(), taskId, channelId);
//        String taskIds = concentrator.getTaskIds();
//        String[] taskIdArr = StringUtil.isBlank(taskIds) ? null : taskIds.split(Constant.SPLIT_SIGN_COMMA);
//        StringBuffer newTaskIds = new StringBuffer();
//        if (taskIdArr != null && taskIdArr.length > 0) {
//            for (int i = 0; i < taskIdArr.length; i++) {
//                if (!taskIdArr[i].equals(StringUtil.toString(taskId))) {
//                    newTaskIds.append(taskIdArr[i]);
//                }
//            }
//        }
//        concentrator.setTaskIds(newTaskIds.toString());
//        commonDAO.update(concentrator);
//    }

    @Override
    public JsonResult getFnPnMapList(int concentratorId) {
        Map<String, List> resultMap = new HashMap<>();
        List<Map> fnMapList = new ArrayList<>();
        for (Afn0AFnEnum fn : Afn0AFnEnum.values()) {
            Map<String, Object> fnMap = new HashMap<>();
            fnMap.put("value", fn.getValue());
            fnMap.put("name", fn.getRemark());
            fnMapList.add(fnMap);
        }
//        List fnMapList = new ArrayList(map.entrySet());
        resultMap.put("fnMap", fnMapList);
        List<Concentrator> concentratorList = deviceDao.queryConcentratorById(concentratorId);
        if (concentratorList == null || concentratorList.size() == 0) {
            return new JsonResult(false, 10002, "集中器不存在");
        }
        List<Device> elecMeterList = deviceDao.queryElecMeterBySlaveAdd(concentratorList.get(0).getAddress());
        List<Integer> pnList = new ArrayList<>();
        for (Device elecMeter : elecMeterList) {
            pnList.add(elecMeter.getSlaveMachineNum());
        }
        resultMap.put("pnMap", pnList);
        return new JsonResult(true, 0, resultMap);
    }

    private boolean recoverElecMeterList(List<Device> elecMeterList) {
        for (Device elecMeter : elecMeterList) {
            recoverElecMeter(elecMeter);
        }
        commonDAO.saveOrUpdateAll(elecMeterList);
        return true;
    }

    private Device recoverElecMeter(Device elecMeter) {
        elecMeter.setDeviceName(null);
        elecMeter.setSlaveAdd(null);
        elecMeter.setSlaveMachineNum(1);
        elecMeter.setMsgId(0);
        elecMeter.setDetail(null);
        return elecMeter;
    }

    /**
     * 更新集中器后，清除所有查询帧的缓存
     *
     * @param terminalAddress
     */
    private void clearCommandCacheData(String terminalAddress) {
        for (Map.Entry<String, Map<String, Object>> entry : Protocol3761Cache.TERMINAL_GET_COMMAND_PACKET.entrySet()) {
            if (entry.getKey().contains(terminalAddress + Constant.SPLIT_SIGN_COLON)) {
                Protocol3761Cache.TERMINAL_GET_COMMAND_PACKET.remove(entry.getKey());
            }
        }
    }
}
