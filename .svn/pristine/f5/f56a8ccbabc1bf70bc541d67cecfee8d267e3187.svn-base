package com.telek.hemsipc.repository.impl;

import com.telek.hemsipc.model.Concentrator;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.repository.IDeviceDao;
import com.telek.hemsipc.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangxb
 * @date 20-1-6 上午10:51
 */
@Component("deviceDao")
public class DeviceDaoImpl implements IDeviceDao {

    @Autowired
    ICommonDAO commonDAO;

    @Override
    public List<Device> queryElecMeterBySlaveAdd(String slaveAdd) {
        String hql = "from Device where slaveAdd = '" + slaveAdd + "'";
        return (List<Device>) commonDAO.findByHql(hql);
    }

    @Override
    public List<Concentrator> queryConcentratorById(Integer id) {
        String hql = "from Concentrator where isDelete = 0 and id = '" + id + "'";
        return (List<Concentrator>) commonDAO.findByHql(hql);
    }

    @Override
    public List<Device> queryElecMeterByDeviceId(String deviceId) {
        String hql = "from Device where id = '" + deviceId + "'";
        return (List<Device>) commonDAO.findByHql(hql);
    }

    @Override
    public List<Device> queryElecMeterWithSlaveAdd() {
        String hql = "from Device where slaveAdd is not null";
        return (List<Device>) commonDAO.findByHql(hql);
    }

    @Override
    public int countElecMeter() {
        String sql = "select count(*) from device";
        List count = commonDAO.findBySQL(sql);
        if (StringUtil.isBlank(count)) {
            return 0;
        }
        return StringUtil.parseInt(count.get(0));
    }

    @Override
    public List<Concentrator> queryConcentratorByParams(String name, String address) {
        String hql = "from Concentrator where isDelete = 0";
        if (name != null) {
            hql += "and name like '%" + name + "%'";
        }
        if (address != null) {
            hql += "and address = '" + address + "'";
        }
        return (List<Concentrator>) commonDAO.findByHql(hql);
    }

    @Override
    public List<Concentrator> queryConcentratorByDeleteAndAddress(String address) {
        String hql = "from Concentrator where isDelete = -1 and address = " + address;
        return (List<Concentrator>) commonDAO.findByHql(hql);
    }

    @Override
    public Device saveOrUpdateElecMeter(Device device) {
        Object obj = commonDAO.saveOrUpdate(device);
        if (obj == null) {
            return null;
        }
        return (Device) obj;
    }

    @Override
    public Concentrator saveOrUpdateConcentrator(Concentrator concentrator) {
        Object obj = commonDAO.saveOrUpdate(concentrator);
        if (obj == null) {
            return null;
        }
        return (Concentrator) obj;
    }

    @Override
    public List<Device> queryOneUnBindElecMeter() {
        String hql = "from Device where deviceName = null and slaveAdd = null";
        return (List<Device>) commonDAO.findByHql(hql);
    }

    @Override
    public boolean deleteConcentrator(Integer id) {
        String sql = "update concentrator set is_delete = -1 where id = " + id;
        int count = commonDAO.executeUpdateSQL(sql);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }
}
