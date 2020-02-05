package com.telek.hemsipc.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.telek.hemsipc.model.*;

/**
 * @Auther: wll
 * @Date: 2018/9/13 10:07
 * @Description:
 */
public class DeviceContext {
    /**
     * 最近电量数据.
     */
    public static Map<String, ElecData> elecDataMap = new ConcurrentHashMap<>();
    /**
     * 设备列表.
     */
    public static Map<String, Device> deviceMap = new ConcurrentHashMap<>();
    /**
     * dl645协议对象
     */
    public static Map<String, Dl645ReadingProtocol> dl645ReadingProtocolMap = new ConcurrentHashMap<>();
    /**
     * sdmp协议对象.
     */
    public static Map<Integer, SdmpProtocol> sdmpProtocolMap = new ConcurrentHashMap<>();
    /**
     * 保存设备最近采集到的数据(key：设备ID，value：该设备当前采集到的数据)
     * 注意：这里的设备ID可能是虚拟设备，比如南钢，有多个虚拟的设备ID对应一个真实的设备ID，发送心跳时要将虚拟的设备ID的数据合并到真实设备上
     */
    private static Map<String, CurrentDeviceData> currentDeviceDataMap = new ConcurrentHashMap<String, CurrentDeviceData>();

    /**
     * 设备类型具备的属性（key:设备类型编码 value:设备类型属性）
     */
    public static Map<String, DeviceTypeAttribute> deviceTypeAttributeMap = new ConcurrentHashMap<>();

    /**
     * ModbusReadDataConfig(key:设备ID, value：该设备ID对应的读数据配置)
     */
    public static Map<String, List<ModbusReadDataConfig>> modbusReadDataConfigMap = new ConcurrentHashMap<String, List<ModbusReadDataConfig>>();

    /**
     * ModbusWriteDataConfig(key:设备ID, key:控制类型， value：该设备ID对应的写数据配置)
     */
    public static Map<String, Map<String, List<ModbusWriteDataConfig>>> modbusWriteDataConfigMap = new ConcurrentHashMap<String, Map<String, List<ModbusWriteDataConfig>>>();

    /**
     * ControlConfig(key:设备ID+ "," + 控制类型,  value：该设备ID对应的控制配置)
     */
    public static Map<String, ControlConfig> controlConfigMap = new  ConcurrentHashMap<String, ControlConfig>();
    
    /**
     * SimulationData(key:设备ID, value：该设备ID对应的SimulationData对象)
     */
    public static Map<String, SimulationData> simulationDataMap = new ConcurrentHashMap<String, SimulationData>();

    /**
     * 集中器缓存
     */
    public static Map<Integer, Concentrator> concentratorMap = new ConcurrentHashMap<>();


    public static CurrentDeviceData getCurrentDeviceDataByDeviceID(String deviceID) {
        return currentDeviceDataMap.get(deviceID);
    }

    public static List<Device> getDeviceByType(int type) {
        List<Device> deviceList = new ArrayList<>();
        for (Device device : deviceMap.values()) {
            if (device.getType() == type) {
                deviceList.add(device);
            }
        }
        return deviceList;
    }

    /**
     * 返回指定设备协议的所有设备
     *
     * @param decodeProtocol 设备协议
     * @return
     */
    public static List<Device> getDeviceByProtocol(String decodeProtocol) {
        List<Device> deviceList = new ArrayList<>();
        for (Device device : deviceMap.values()) {
            if (device.getDecodeProtocol().equals(decodeProtocol)) {
                deviceList.add(device);
            }
        }
        return deviceList;
    }

    public static void setCurrentPointData(String deviceID, int key, PointData pointData) {
        CurrentDeviceData deviceData = currentDeviceDataMap.get(deviceID);
        if (deviceData == null) {
            deviceData = new CurrentDeviceData();
            currentDeviceDataMap.put(deviceID, deviceData);
        }
        deviceData.setValue(key, pointData);
    }

    public static PointData getCurrentPointData(String deviceID, int key) {
        CurrentDeviceData currentDeviceData = currentDeviceDataMap.get(deviceID);
        if (currentDeviceData == null) {
            return null;
        }
        return currentDeviceData.getValue(key);
    }

    public static List<ModbusWriteDataConfig> getCommandConfigList(String deviceID, String controlType) {
        Map<String, List<ModbusWriteDataConfig>> map = modbusWriteDataConfigMap.get(deviceID);
        if (map == null) {
            return null;
        } else {
            return map.get(controlType);
        }
    }


}
