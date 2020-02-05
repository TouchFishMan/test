package com.telek.hemsipc.quartz;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.context.SysContext;
import com.telek.hemsipc.model.CurrentDeviceData;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.ElecData;
import com.telek.hemsipc.model.PointData;
import com.telek.hemsipc.model.SdmpProtocol;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.sdmp.OctetString;
import com.telek.hemsipc.sdmp.SdmpConstant;
import com.telek.hemsipc.sdmp.SignedInteger64;
import com.telek.hemsipc.sdmp.VarBind;
import com.telek.hemsipc.service.ICloudService;
import com.telek.hemsipc.util.CommonUtil;
import com.telek.hemsipc.util.Protocol376Util;


/**
 * 定时向云端发送心跳
 * @Class Name：SendDataToCloudQuartz    
 * @Class Description：    
 * @Creater：telek    
 * @Create Time：2019年5月10日下午3:25:47    
 * @Modifier：telek    
 * @Modification Time：2019年5月10日下午3:25:47    
 * @Remarks：
 */
 @Component
public class SendDataToCloudQuartz {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ICloudService cloudService;
    
    @Autowired
    private ICommonDAO commonDAO;
    
    /**
     * 
     * @Modifier:kds 每分钟采集数据上报到云
     * @Date：2018年11月14日下午1:21:17 
     * @Describe:hearbeat
     */
 
    @Scheduled(cron = "53 * * * * ?")
    public void hearbeat() {
        try {
            sendDataToCloud();
        } catch (Exception e) {
            log.error("发送给云端心跳异常", e);
        }
    }
    
    /**
     * 将多个采集点数据合并到一个设备上   
     * @Modifier:kds 
     * @Date：2018年11月21日上午10:14:11 
     * @Describe:combineData      
     * @param deviceList
     */
    private void combineData(Collection<Device> devices){
        for (Device device : devices) {
        	if(device.getSendHeartBit() != Constant.YES) {
        		log.info("设备" + device.getDeviceId() + "不需要向云端发送心跳，所以不需要合并数据");
        		continue;
        	}
        	
        	String deviceID = device.getDeviceId();
            
            String collectionDeviceIDs = device.getCollectionDeviceId();
			if (collectionDeviceIDs == null || collectionDeviceIDs.length() < 1) {
            	continue;
            }
			
			String needCombineKey = SysContext.getSysConfig(Constant.NEED_COMBINE_KEY).getConfigValue();
			Map<Integer, Boolean> notFirstCombineMap = new HashMap<Integer, Boolean>();
			
            //首先处理采集设备的数据
            for(String collectionDeviceID : collectionDeviceIDs.split(Constant.SPLIT_SIGN_COMMA)) { //循环该设备所有采集ID
                log.info("合并数据deviceID ：" + deviceID + " collectionDeviceID: " + collectionDeviceID);
                CurrentDeviceData collectionDevicedata = DeviceContext.getCurrentDeviceDataByDeviceID(collectionDeviceID);
                if(collectionDevicedata == null) {
                    log.info("Iec104Context.getCurrentDeviceDataByDeviceID() is null. collectionDeviceID is: " + collectionDeviceID);
                    continue;
                }
                Iterator<Entry<Integer, PointData>> it = collectionDevicedata.getDataMap().entrySet().iterator();
                while(it.hasNext()) {
                    Entry<Integer, PointData> entry = it.next();
                    
                    //如果不是第一次设置某个key的值，清空该key的值，防止采集设备ID与主设备ID都采集相同key的数据导致数据重复累加。
                    if(!notFirstCombineMap.containsKey(entry.getKey())) { 
                    	PointData tempPointData = DeviceContext.getCurrentPointData(deviceID, entry.getKey());
                    	if(tempPointData != null) {
                    		tempPointData.setValue(0);
                    	}
                    	notFirstCombineMap.put(entry.getKey(), true);
                    }
                    log.info("将" + collectionDeviceID + "的数据合并到" + deviceID + "key is: " + entry.getKey() + " value is: " + entry.getValue().getFloatValue());
                    setCollectionData(deviceID, needCombineKey, entry);
                }
            }
            //补充主设备的数据
            log.info("补充主设备ID ：" + deviceID + " collectionDeviceID: " + deviceID);
            CurrentDeviceData mainDevicecollectionedData = DeviceContext.getCurrentDeviceDataByDeviceID(deviceID);
            if(mainDevicecollectionedData == null) {
                log.info("Iec104Context.getCurrentDeviceDataByDeviceID() is null. collectionDeviceID is: " + deviceID);
                continue;
            }
            Iterator<Entry<Integer, PointData>> it = mainDevicecollectionedData.getDataMap().entrySet().iterator();
            while(it.hasNext()) {
                Entry<Integer, PointData> entry = it.next();
                //如果采集的设备ID中收集到的数据不包含主设备ID中收集到的数据，则将主设备中收集到的数据补充进去
                if(DeviceContext.getCurrentPointData(deviceID, entry.getKey()) == null) { 
                    setCollectionData(deviceID, needCombineKey, entry);
                }
            }
            
            log.info("combineData 设备ID " + deviceID ); 
        } 
    }

	private void setCollectionData(String deviceID, String needCombineKey, Entry<Integer, PointData> entry) {
		long collectTime = entry.getValue().getTime();
		// 如果该采集点数据有效（当前时间-采集时间 < 设置的离线时间）
		if(CommonUtil.validateData(collectTime)) {
		    PointData pointData = DeviceContext.getCurrentPointData(deviceID, entry.getKey()); // 获取设备的采集点(对应sdmpkey)数据
		    
		    if(pointData == null || !CommonUtil.validateData(pointData.getTime())) { // 一旦离线，数据作废
		        pointData = new PointData();
		    }
		    if(pointData.getTime() < collectTime) {
		        pointData.setTime(collectTime);
		    }
		    
		    if(needCombineKey.contains(entry.getKey().toString() + Constant.SPLIT_SIGN_COMMA)) { //如果需要合并
		        pointData.setValue(pointData.getFloatValue() + entry.getValue().getFloatValue());
		    }else {
		        pointData.setValue(entry.getValue().getValue());
		    }
		    
		    DeviceContext.setCurrentPointData(deviceID, entry.getKey(), pointData); //这里可优化，提到while循环外面
		}
		else{
		    log.info("设备" + deviceID + "采集点" + entry.getKey() +  "长时间没有采集到数据"); 
		}
	}
    
    private void sendDataToCloud() throws Exception {
    	Collection<Device> devices = DeviceContext.deviceMap.values();
        if(devices == null || devices.size() == 0) {
            return;
        }
        
        // combineData(devices); //先处理数据合并
        
        for (Device device : devices) {
        	if(device.getSendHeartBit() != Constant.YES) {
        		log.info("设备" + device.getDeviceId() + "不需要向云端发送心跳");
        		continue;
        	}
            if (!Protocol376Util.is376Device(device)) {
                log.info("设备" + device.getDeviceId() + "非376协议，不需要向云端发送心跳");
                continue;
            }
            Vector<VarBind> binds = new Vector<>();
            CurrentDeviceData data = DeviceContext.getCurrentDeviceDataByDeviceID(device.getDeviceId());
            
            if(data != null) {
                Map<Integer, PointData> dataMap = data.getDataMap();
                Iterator<Entry<Integer, PointData>> it = dataMap.entrySet().iterator();
                while(it.hasNext()) { 
                    Entry<Integer, PointData> entry = it.next();
                    if(!CommonUtil.validateData(entry.getValue().getTime()) ) {//过滤掉长时间没有采集到数据的
                    	continue;
                    }
                    SdmpProtocol sdmpProtocol = DeviceContext.sdmpProtocolMap.get(entry.getKey());
                    if(sdmpProtocol.getProtocolDataType() == SdmpConstant.PDU_TYPE_NUMBER) {
                        // TODO 电表协议可能配错了，暂时处理：如果有功功率为0，则视在功率赋值为了
                    	if(sdmpProtocol.getProtocolKey() == SdmpConstant.PDU_APPARENT_POWER) {
                    		PointData powerPointData = dataMap.get(SdmpConstant.PDU_ACTIVE_POWER);
                    		log.info(device.getDeviceId() + "处理视在功率powerPointData"  + powerPointData);
                    		if(powerPointData != null && powerPointData.getFloatValue() == 0) {
                    			log.info(device.getDeviceId() + "处理视在功率powerPointData.getFloatValue()"  + powerPointData.getFloatValue());
                    			binds.add(new VarBind(SdmpConstant.PDU_APPARENT_POWER, new SignedInteger64(0)));
                    		}else {
                    			log.info(device.getDeviceId() + "处理视在功率　用实际值");
                    			binds.add(new VarBind(sdmpProtocol.getProtocolKey(), new SignedInteger64(
                                        (long) ((entry.getValue().getFloatValue()  + sdmpProtocol.getValueOffset()) * sdmpProtocol.getProtocolMultiplier()))));
                    		}
                    	} else {
                    		binds.add(new VarBind(sdmpProtocol.getProtocolKey(), new SignedInteger64(
                                    (long) ((entry.getValue().getFloatValue()  + sdmpProtocol.getValueOffset()) * sdmpProtocol.getProtocolMultiplier()))));
                    		//如果需要用有功功率生成视在功率
                        	if(sdmpProtocol.getProtocolKey() == SdmpConstant.PDU_ACTIVE_POWER) {
                        		if(SysContext.getSysConfig(Constant.CREATE_POWER_APPARENT_WITH_POWER_ACTIVE).getConfigValue().equals("" + Constant.YES) 
                        				&& dataMap.get(SdmpConstant.PDU_APPARENT_POWER) == null) { 
                                    binds.add(new VarBind(SdmpConstant.PDU_APPARENT_POWER, new SignedInteger64(
                                    		(long) ((entry.getValue().getFloatValue()  + sdmpProtocol.getValueOffset()) * sdmpProtocol.getProtocolMultiplier()))));
                                    log.info("++++++++发送给云端数据deviceID:" + device.getDeviceId() + " key:" + SdmpConstant.PDU_APPARENT_POWER + " value: " + entry.getValue().getValue()); 
                                }
                            }
                    	}
                    } else if(sdmpProtocol.getProtocolDataType() == SdmpConstant.PDU_TYPE_STRING){
                    	binds.add(new VarBind(sdmpProtocol.getProtocolKey(), new OctetString(entry.getValue().getValue().toString())));
                    }
                    log.info("++++++++发送给云端数据deviceID:" + device.getDeviceId() + " key:" + sdmpProtocol.getProtocolKey() + " value: " + entry.getValue().getValue()); 
                }
                
                if (!Constant.SERVER_ONLINE) { //如果当前工控机离线
                	//dealOffLine(device, binds);
                } else {//否则，正常发送到云端
                    cloudService.sendHeartBeat(binds, device);
                }
            }
            else{
            	/*Channel channel = NettyContext.clientChannel.get(device.getChannelId());
                if (channel == null) { // 采集器与工控机网路不通，不发心跳
                    log.error("设备 "  + device.getDeviceId() + " 离线，不发心跳"); 
                } else {
            		cloudService.sendHeartBeat(new Vector<VarBind>(), device); 
                    log.info("数据为空 id： " + device.getDeviceId() + "  发送不包含数据的心跳");
            	}*/
            	log.error("数据为空，不发送心跳");
            }
        }  
        
    }

	private void dealOffLine(Device device, Vector<VarBind> binds) {
		log.error("当前工控机离线");
		String now = new SimpleDateFormat("yyyyMMddHHmm").format(System.currentTimeMillis());
		ElecData elecData = new ElecData(device.getDeviceId(), now);
		elecData.setCollectionData(JSON.toJSONString(binds));
		commonDAO.saveOrUpdate(elecData);
	}
    
}

