package com.telek.hemsipc.quartz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.netty.init.CloudClientPoint;
import com.telek.hemsipc.sdmp.SDMPv1;
import com.telek.hemsipc.sdmp.resend.ResendObject;
import com.telek.hemsipc.sdmp.resend.SendMap;
import com.telek.hemsipc.util.SysCmdUtil;

/**
 * @Auther: wll
 * @Date: 2018/9/11 14:00
 * @Description:
 */
@Component
public class IpcQuartzManager {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CloudClientPoint clientPoint;

    /**
     * @Description: 每天定时删除一个月前的日志
     * @auther: wll
     * @date: 9:56 2018/9/13
     * @param: []
     * @return: void
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void deleteLogs() {
        String logPath = System.getProperty("user.dir") + "/logs";
        log.info("开始删除日志：" + logPath);
        //遍历日志文件夹下的所有日志文件
        File logDir = new File(logPath);
        if (!logDir.exists() || logDir.isFile()) {
            log.error("日志文件夹路径有误：" + logPath);
        }
        //获取一个月前日期
        Date date = DateUtils.addDays(new Date(), -60);
        File[] logFiles = logDir.listFiles();
        for (File logFile : logFiles) {
            String fileName = logFile.getName();
            String logFileDate = fileName.substring(fileName.lastIndexOf(".") + 1);
            try {
                Date logDate = new SimpleDateFormat("yyyy-MM-dd").parse(logFileDate);
                //删除一个月前的日志
                if (logDate.compareTo(date) < 0) {
                    logFile.delete();
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * @Description: 工控机重发心跳包到云端，主要是为了保持路由器端口不被释放，因此需要一直保持和云端的通讯；每15秒遍历一次
     * @auther: wll
     * @date: 9:27 2018/9/18
     * @param: []
     * @return: void
     */
    @Scheduled(cron = "0/15 * * * * ?")
    public void resend() {
        Iterator<Map.Entry<String, Map<Integer, ResendObject>>> it = SendMap.resendMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Map<Integer, ResendObject>> entry = it.next();
            Map<Integer, ResendObject> resendObjectMap = entry.getValue();
            Iterator<ResendObject> it2 = resendObjectMap.values().iterator();
            while (it2.hasNext()) {
                ResendObject resendObject = it2.next();
                if (resendObject.getSendTime() > System.currentTimeMillis()) {
                    return;
                }
                SDMPv1 sdmpData = resendObject.getSdmpData();
                clientPoint.sendMsg(sdmpData);
                resendObject.refreshResend();
                //当重发次数到达3次时
                if (resendObject.getResendIndex() >= ResendObject.resendNum) {
                    it.remove();
                }
            }
        }
    }

    /**
     * @Description: 每分钟判断是否和云端断开连接，如果断开连接，则需要对云端进行重连操作
     * @auther: wll
     * @date: 10:01 2018/9/18
     * @param: []
     * @return: void
     */
    //TODO WINDOWS调试先不做网络判断
//    @Scheduled(cron = "0 * * * * ?")
    public void initNetwork() {
        // 如果云云端断连时间大于2分钟则重启网络。
        long nowTime = System.currentTimeMillis();
        if ((nowTime - Constant.LAST_NET_TIME) > 120 * 1000) {
            Constant.SERVER_ONLINE = false;
            if (Constant.CHANGE_PORT_TIMES < 2) {
                Constant.CHANGE_PORT_TIMES++;
                clientPoint.resetPort();
            } else {
                // 如果换端口重启两次还不行则进行重启网卡
                SysCmdUtil.netRestart();
                clientPoint.resetPort();
                Constant.CHANGE_PORT_TIMES = 0;
                Constant.FORCE_RESET_NETWORK++;
            }
            log.info("网络重启次数统计:换端口重启=" + Constant.CHANGE_PORT_TIMES + "强制重启=" + Constant.FORCE_RESET_NETWORK + ";云端IP="
                    + clientPoint.getServer_host() + ";云端端口=" + clientPoint.getServer_port() + "本地端口=" + clientPoint.getClient_port());
        }
    }
}
