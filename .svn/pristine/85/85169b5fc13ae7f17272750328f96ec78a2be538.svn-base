package com.telek.hemsipc.repository;

import com.telek.hemsipc.model.Concentrator;
import com.telek.hemsipc.model.Device;

import java.util.List;

/**
 * @author wangxb
 * @date 20-1-6 上午10:45
 */
public interface IDeviceDao {

    List<Device> queryElecMeterBySlaveAdd(String slaveAdd);

    List<Device> queryElecMeterByDeviceId(String deviceId);

    List<Device> queryElecMeterWithSlaveAdd();

    int countElecMeter();

    List<Concentrator> queryConcentratorById(Integer id);

    List<Concentrator> queryConcentratorByParams(String name, String address);

    List<Concentrator> queryConcentratorByDeleteAndAddress(String address);

    Device saveOrUpdateElecMeter(Device device);

    Concentrator saveOrUpdateConcentrator(Concentrator concentrator);

    List<Device> queryOneUnBindElecMeter();

    boolean deleteConcentrator(Integer id);
}
