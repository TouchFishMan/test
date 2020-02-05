package com.telek.hemsipc.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.context.NettyContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.Dl645ReadingProtocol;
import com.telek.hemsipc.netty.init.SerialPoint;
import com.telek.hemsipc.protocol.dl645.ReadDataDecode;
import com.telek.hemsipc.protocol.dl645.request.ReadAddressRequest;
import com.telek.hemsipc.protocol.dl645.request.ReadRequest;
import com.telek.hemsipc.protocol.dl645.response.ReadAddressResponse;
import com.telek.hemsipc.protocol.dl645.response.ReadResponse;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.service.ICollectionService;

/**
 * @Auther: wll
 * @Date: 2018/9/14 14:12
 * @Description:
 */
@Service("collectionService")
public class CollectionServiceImpl implements ICollectionService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    // @Autowired
    private SerialPoint serialPoint;

    @Autowired
    private ReadDataDecode readDataDecode;

    @Autowired
    private ICommonDAO commonDAO;
    
    /**
     * 采集柜查询失败累计次数，当累计到一定数量，则channel可能出现问题，需要重新连接chanel.
     */
    public Map<String, AtomicInteger> increaseMap = new ConcurrentHashMap<String, AtomicInteger>();

    public String readCollectionData(Device device, Dl645ReadingProtocol readingProtocol) {
        ReadRequest readRequest = new ReadRequest(device.getSlaveAdd(), HexBin.decode(readingProtocol.getCommand()));
        
		ReadResponse readResponse = (ReadResponse) serialPoint.syncSendMsgByClientChannel(device.getChannelId(),
				readRequest /*new TempRequest()*/ );
        if (readResponse == null || readResponse.getDataid() == null || readResponse.getDatas() == null) {
            log.error("设备：{" + device.getDeviceId() + "},地址：{" + device.getChannelId() + "},指令：{" + readingProtocol + "},查询失败");
            reconnectNetty(device.getChannelId());
            return null;
        }
        increaseMap.remove(device.getChannelId());
        return readDataDecode.decodeData(readResponse.getDataid(), readResponse.getDatas(), readingProtocol);
    }

    @Override
    public void readAddress() {
        ReadAddressRequest request = new ReadAddressRequest();
        for (String channelId : NettyContext.clientChannel.keySet()) {
            ReadAddressResponse response = (ReadAddressResponse) serialPoint.syncSendMsgByClientChannel(channelId, request);
            if (response == null) {
                log.error("channel：{}，查询电表地址失败", channelId);
                continue;
            }
            String slave = response.getSlave();
            
            //Device device = deviceRepository.findFirstBySlaveAdd(slave);
            Device device = null;
            @SuppressWarnings("unchecked")
			List<Device> devices = (List<Device>)commonDAO.findByHql("from Device where slaveAdd = '" + slave + "'");
            if(devices != null && devices.size() > 0) {
            	device = devices.get(0);
            }
            if (device == null) {
                log.error("channelId:{},不存在macadd为{}的设备", channelId, slave);
                continue;
            }
            device.setChannelId(channelId);
        }
    }

    /**
     * @Description: 查询次数统计，当连续查询失败次数大于3次时，对netty通道进行重连
     * @auther: wll
     * @date: 16:33 2018/9/13
     * @param: [deviceId, channelId]
     * @return: void
     */
    private void reconnectNetty(String channelId) {
		/*
		 * if (increaseMap.containsKey(channelId)) { if
		 * (increaseMap.get(channelId).incrementAndGet() > 3) { Channel channel =
		 * NettyContext.clientChannel.get(channelId); if (channel != null) {
		 * channel.close(); } increaseMap.remove(channelId); } } else {
		 * increaseMap.put(channelId, new AtomicInteger(1)); }
		 */
    }
}
