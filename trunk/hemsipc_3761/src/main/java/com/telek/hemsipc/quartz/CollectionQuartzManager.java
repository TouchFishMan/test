package com.telek.hemsipc.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.telek.hemsipc.contant.ProtocolType;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.Dl645ReadingProtocol;
import com.telek.hemsipc.model.PointData;
import com.telek.hemsipc.model.SdmpProtocol;
import com.telek.hemsipc.protocol.dl645.ReadDataDecode;
import com.telek.hemsipc.service.ICollectionService;
import com.telek.hemsipc.util.StringUtil;

/**
 * @Auther: wll
 * @Date: 2018/9/18 09:57
 * @Description:
 */
//@Component
public class CollectionQuartzManager {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ICollectionService collectionService;
    
    /**
     * @Description: 采集柜和采集控制柜每分钟采集数据上报到云
     * @auther: wll
     * @date: 9:56 2018/9/13
     * @param: []
     * @return: void
     */
    @Scheduled(cron = "27 * * * * ?")
    public void collectionHearbeat() {
        for (final Device device : DeviceContext.deviceMap.values()) {
			if (ProtocolType.DL645.getCode().equals(device.getDecodeProtocol())) {
            	run(device);
            }
        }
    }
    
    
    public void run(Device device) {
        String[] supportCommands = device.getSupportCommands().split(",");
        for (String protocalId : supportCommands) {
        	if(!StringUtil.isBlank(protocalId)) {
        		try {
            		Dl645ReadingProtocol readingProtocol = DeviceContext.dl645ReadingProtocolMap.get(protocalId);
                    String result = collectionService.readCollectionData(device, readingProtocol);
                    log.info("---------------------deviceID: " + device.getDeviceId() + " data: " + result);
                    dealData(readingProtocol, result, device);
            	}catch(Exception e) {
            		log.error(protocalId + " 处理异常 " + e.getMessage(), e);
            	}
        	}
        }
    }

    private float dealRate(Device device, Dl645ReadingProtocol readingProtocol, float value) {
    	float result = value;
    	if ("I_A".equals(readingProtocol.getProtocolId())
    			||"I_B".equals(readingProtocol.getProtocolId())
    			||"I_C".equals(readingProtocol.getProtocolId())
    			||"I_BLOCK".equals(readingProtocol.getProtocolId())
    			||"I_A".equals(readingProtocol.getProtocolId())
    			||"I_A".equals(readingProtocol.getProtocolId())) {
    		result = result * device.getIrate();
        }
    	if ("U_A".equals(readingProtocol.getProtocolId())
    			||"U_B".equals(readingProtocol.getProtocolId())
    			||"U_C".equals(readingProtocol.getProtocolId())
    			||"U_BLOCK".equals(readingProtocol.getProtocolId())) {
    		result = result * device.getUrate();
        }
    	if ("P_A".equals(readingProtocol.getProtocolId()) 
    			||"P_A_A".equals(readingProtocol.getProtocolId())
    			||"P_A_B".equals(readingProtocol.getProtocolId())
    			||"P_A_C".equals(readingProtocol.getProtocolId())
    			||"P_A_BLOCK".equals(readingProtocol.getProtocolId())
    			||"P_A_T".equals(readingProtocol.getProtocolId())
    			||"P_B".equals(readingProtocol.getProtocolId())
    			||"P_BLOCK".equals(readingProtocol.getProtocolId())
    			||"P_C".equals(readingProtocol.getProtocolId())
    			||"P_R_A".equals(readingProtocol.getProtocolId())
    			||"P_R_B".equals(readingProtocol.getProtocolId())
    			||"P_R_BLOCK".equals(readingProtocol.getProtocolId())
				||"P_R_C".equals(readingProtocol.getProtocolId())
    			||"P_R_T".equals(readingProtocol.getProtocolId())
				||"P_T".equals(readingProtocol.getProtocolId())) {
    		result = result * device.getPowerRate();
        }
    	if ("E_A_C".equals(readingProtocol.getProtocolId())
    			|| "E_A_R".equals(readingProtocol.getProtocolId())
    			|| "E_R_1".equals(readingProtocol.getProtocolId())
    			|| "E_R_2".equals(readingProtocol.getProtocolId())
    			|| "E_V".equals(readingProtocol.getProtocolId()) ) {
    		result = result * device.getElecRate();
        }
    	return result;
    }

    private void dealData(Dl645ReadingProtocol readingProtocol, String resultValue, Device device) {
        if (readingProtocol.getDecodeType() == ReadDataDecode.DECODE_TYPE_FLOAT) {
        	if(resultValue != null) {
        		log.info("------------------" + resultValue);
        		SdmpProtocol sdmpProtocol = DeviceContext.sdmpProtocolMap.get(readingProtocol.getSdmpProtocolKey());
                if (readingProtocol == null || sdmpProtocol == null) {
                    log.error("【read解码】协议对象为空，readingProtocol：{}，sdmpProtocol:{}", readingProtocol, sdmpProtocol);
                    throw new RuntimeException();
                }
                if (sdmpProtocol.getProtocolDataType() == 1) {
                    float value = Float.parseFloat(resultValue);
                    value = dealRate(device, readingProtocol, value);
                    DeviceContext.setCurrentPointData(device.getDeviceId(), sdmpProtocol.getProtocolKey(), new PointData(System.currentTimeMillis(), value));
                    log.info("deviceid:" + device.getDeviceId() + " key: " + 
                    		DeviceContext.sdmpProtocolMap.get(sdmpProtocol.getProtocolKey()).getProtocolRemark()  +  " value: " + value);
                } else if (sdmpProtocol.getProtocolDataType() == 2) {
                    DeviceContext.setCurrentPointData(device.getDeviceId(), sdmpProtocol.getProtocolKey(), new PointData(System.currentTimeMillis(), resultValue));
                } else {
                    log.error("【read解码】不支持该sdmp协议，{}", sdmpProtocol);
                }
        	}
        } else if (readingProtocol.getDecodeType() == ReadDataDecode.DECODE_TYPE_FLOAT_BLOCK) {
        	if(resultValue != null) {
        		String[] blockDatas = readingProtocol.getBlockDatas().split(",");
                String[] dataResults = resultValue.split(",");
                log.info("--------------------" + resultValue);
                for (int i = 0; i < blockDatas.length; i++) {
                    Dl645ReadingProtocol readingProtocolBlock = DeviceContext.dl645ReadingProtocolMap.get(blockDatas[i]);
                    SdmpProtocol SdmpProtocolBlock = DeviceContext.sdmpProtocolMap.get(readingProtocolBlock.getSdmpProtocolKey());
                    if (SdmpProtocolBlock.getProtocolDataType() == 1) {
                        float value = Float.parseFloat(dataResults[i]);
                        value = dealRate(device, readingProtocol, value);
                        DeviceContext.setCurrentPointData(device.getDeviceId(), SdmpProtocolBlock.getProtocolKey(), new PointData(System.currentTimeMillis(), value)); 
                        log.info("deviceid:" + device.getDeviceId() + " key: " + 
                        		DeviceContext.sdmpProtocolMap.get(SdmpProtocolBlock.getProtocolKey()).getProtocolRemark()  +  " value: " + value);
                    } else if (SdmpProtocolBlock.getProtocolDataType() == 2) {
                    	DeviceContext.setCurrentPointData(device.getDeviceId(), SdmpProtocolBlock.getProtocolKey(), new PointData(System.currentTimeMillis(), dataResults[i])); 
                    } else {
                        log.error("【read解码】不支持该sdmp协议，{}", SdmpProtocolBlock);
                    }
                }
        	}
        } else {
            log.error("【read解码】不支持该解码方式，{}", readingProtocol);
        }
    }
}
