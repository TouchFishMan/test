package com.telek.hemsipc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.service.ICloudService;
import com.telek.hemsipc.service.impl.CloudServiceImpl;

/**
 * @Auther: wll
 * @Date: 2018/9/17 09:23
 * @Description:
 */
public class ElecSupplyServer implements Runnable {
    Logger log = LoggerFactory.getLogger(this.getClass());
  
    private ICommonDAO commonDAO = HemsipcSpringContext.getBean("commonDAO", ICommonDAO.class);
    private ICloudService cloudService = HemsipcSpringContext.getBean(CloudServiceImpl.class);

    @Override
    public void run() {
        // log.info("-----------------开始补发离线电量------------------");
		/*List<ElecData> elecDataList = (List<ElecData>)commonDAO.findByHql("from ElecData order by id ");
        String time = "";
        for (ElecData elecData : elecDataList) {
            //1分钟的数据发送结束后等待5秒后发送下一分钟数据
            if (!time.equals(elecData.getTime())) {
            }
            time = elecData.getTime();
            //补发数据
            Device device = DeviceContext.deviceMap.get(elecData.getDeviceId());
            Vector<VarBind> varBinds =  JSON.parseObject(elecData.getCollectionData(),Vector.class);
            cloudService.sendHeartBeat(varBinds, device);
            //补发结束后从数据库删除该数据
            commonDAO.delObject(ElecData.class, elecData.getId());
        }*/
    }
}
