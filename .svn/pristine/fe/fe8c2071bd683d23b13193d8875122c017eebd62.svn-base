package com.telek.hemsipc;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.context.SysContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.SdmpProtocol;
import com.telek.hemsipc.model.SysConfig;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.Protocol3761Starter;
import com.telek.hemsipc.protocal3761.refresh.Afn4F10HotRefresh;
import com.telek.hemsipc.protocal3761.refresh.HotFileListener;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.service.ICloudService;
import com.telek.hemsipc.service.ICollectionService;
import com.telek.hemsipc.util.Protocol376Util;
import com.telek.hemsipc.util.StringUtil;
import com.telek.smarthome.cloudserver.cacheManager.CacheModelManager;
import com.telek.smarthome.cloudserver.cacheManager.util.PortFileUtil;
import com.telek.smarthome.cloudserver.hemstools.monitor.handler.MonitorUdpHandler;
import com.telek.smarthome.cloudserver.hemstools.netty.UdpServerNetty;

public class HemsipcApplication {
    private static Logger log = LoggerFactory.getLogger(HemsipcApplication.class);

    public static void main(String[] args) throws Exception {
    	try {
    		HemsipcSpringContext.initSpringContext();
			initCache();
			// initEnvironment();
            print376Device();
			sendActiveToCloud();
			// 启动3761协议
            Protocol3761Starter.start();
            // 监控工具
            initMonitor();
            // 热更新文件监控
            monitorHotFile();
		} catch (Exception e) {
			log.error("启动过程出现异常:" + e.getMessage(), e);
			System.exit(-1);
		}
        log.info("工控机启动成功！！！！");
    }

    private static void initMonitor() {
        log.info(">>>>>>>>>>>>>>>初始化监控工具开始");
        try {
            String userPath = System.getProperty("user.dir");
            int udpPort = PortFileUtil.getPortByConf(userPath + "/conf/portFile.properties", "udpPort");
            log.info("监控端口为：" + udpPort);
            CacheModelManager.init();
            MonitorUdpHandler.setClassName(HemsipcApplication.class);
            UdpServerNetty.setPort(udpPort);
            UdpServerNetty.setSoReceive(524288000);
            UdpServerNetty.init();
            log.info("<<<<<<<<<<<<<<<初始化监控工具结束");
        } catch (Exception var3) {
            log.error("监控工具启动失败：" + var3.getMessage());
        }

    }

    private static void print376Device() {
        Collection<Device> values = DeviceContext.deviceMap.values();
        log.info("所有376协议打印, 个数为：" + values.size());
        for (Device device : values) {
            log.info(device.toString());
        }
    }

    @SuppressWarnings({ "unchecked" })
	private static void initCache() throws Exception {
    	ICommonDAO dao = HemsipcSpringContext.getBean("commonDAO", ICommonDAO.class);
    	
        List<Device> deviceList = (List<Device>)dao.findAllByClass(Device.class);
        List<SdmpProtocol> sdmpProtocolList = (List<SdmpProtocol>)dao.findAllByClass(SdmpProtocol.class);
        List<SysConfig> sysConfigList = (List<SysConfig>)dao.findAllByClass(SysConfig.class);

        for (Device device : deviceList) {
            if (!Protocol376Util.is376Device(device)) {
                continue;
            }
            String deviceId = device.getDeviceId();
            String slaveAdd = device.getSlaveAdd();
            int slaveMachineNum = device.getSlaveMachineNum();
            if (StringUtil.isBlank(slaveAdd) || slaveMachineNum == 0) {
                log.error("376_1协议中集中器地址（slaveAdd）不能为空并且电表地址（slaveMachineNum）必须为1-31");
            } else {
                Protocol3761Cache.TERMINAL_ADDRESS_LINE_DEVICE_ID.put(slaveAdd + slaveMachineNum, deviceId);
                DeviceContext.deviceMap.put(device.getDeviceId(), device);
                //启动msgid+100
                device.setMsgId(device.getMsgId() + 100);
                dao.saveOrUpdate(device);
            }
        }
        for (SdmpProtocol sdmpProtocol : sdmpProtocolList) {
            try{
                DeviceContext.sdmpProtocolMap.put(sdmpProtocol.getProtocolKey(), sdmpProtocol);
            }catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            
        }
        for (SysConfig sysConfig : sysConfigList) {
            try{
                SysContext.setSysConfig(sysConfig.getConfigName(), sysConfig);
            }catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

	private static void initEnvironment()throws InterruptedException {
    	ICollectionService collectionService = HemsipcSpringContext.getBean("collectionService", ICollectionService.class);
        //最后上线时间设置为启动时间
        Constant.LAST_NET_TIME = System.currentTimeMillis();
        Thread.sleep(5000);
        //读取电表地址
        collectionService.readAddress();
	}

	private static void sendActiveToCloud() {
		ICloudService cloudService = HemsipcSpringContext.getBean("cloudService", ICloudService.class);
		//发送上线包
        for (Device device : DeviceContext.deviceMap.values()) {
            if (!Protocol376Util.is376Device(device)) {
                log.info("设备" + device.getDeviceId() + "非376协议，不发激活信息");
            }
            try{
                cloudService.sendActive(device);
            }catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
	}

    /**
     * 热更新文件更新
     * @throws Exception
     */
    public static void monitorHotFile() throws Exception {
        // 监控目录
        String rootDir = Afn4F10HotRefresh.ROOT_PATH;
        log.info("监控文件夹为：" + rootDir);
        // 轮询间隔 1 分钟
        long interval = TimeUnit.MINUTES.toMillis(1);
        // 创建一个文件观察器用于处理文件的格式
        FileAlterationObserver _observer = new FileAlterationObserver(
                rootDir,
                FileFilterUtils.and(
                        FileFilterUtils.fileFileFilter(),
                        FileFilterUtils.suffixFileFilter(".txt")),  //过滤文件格式
                null);
        FileAlterationObserver observer = new FileAlterationObserver(rootDir);

        observer.addListener(new HotFileListener()); //设置文件变化监听器
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        monitor.start();
    }
}
