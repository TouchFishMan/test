package com.telek.hemsipc.protocol.iec104.server;

import org.apache.log4j.Logger;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Description: 变位送数。定时上报。
 * 
 * @author lxf
 * @created 2017年12月29日 下午2:16:44
 */
public class TimerServer {
    private static final Logger LOG = Logger.getLogger(TimerServer.class);
    /**
     * 周期参数。用于变位上数(数据上报)的周期.数值为几就以几秒为周期，检测数据上报。协议建议不超过60秒。
     */
    public static int cycleParameter = 30;

    /**
     * Description:数据主动上报主站。如果这次检测到的数据与上次一样,那就可以不上报。
     * 实际上数据会一直变化。所以这边可以认为定时上报所有数据。
     * 
     * @author lxf
     * @created 2017年12月26日 下午1:25:48
     */
    @Scheduled(cron = "* * * * * ? ")
    @Async
    public void upData() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (second % cycleParameter == 0) {
            // TODO 以下所有方法都需要补充完整
            float nowUValue = DataCollectionHelper.getSystemUValue();
            float nowIValue = DataCollectionHelper.getSystemIValue();
            float nowActivepower = DataCollectionHelper.getSystemActivepower();
            float nowReactivePower = DataCollectionHelper.getSystemReactivePower();
            float nowApparentPower = DataCollectionHelper.getSystemApparentpower();
            if (nowUValue != DataCollectionHelper.lastUValue || nowIValue != DataCollectionHelper.lastIValue
                    || nowActivepower != DataCollectionHelper.lastActivePowere
                    || nowReactivePower != DataCollectionHelper.lastReactivePower) {
                LOG.info("开始上报变位送数信息");
                // 发送带时标的单点遥信信息(开关)、测值浮点数(电流电压功率等)
                // 这里有疑问。单点遥信其实一直都是开。是否需要重复发送。
                ASdu singlePointAsdu = DataCollectionHelper.getSinglePointASduBy(
                        DataCollectionServerStart.commonAddress, CauseOfTransmission.SPONTANEOUS, true);
                ServerListener.sendASdu(singlePointAsdu);
                ASdu shortFloatAsdu = DataCollectionHelper.getShortFloatASduBy(
                        DataCollectionServerStart.commonAddress, CauseOfTransmission.BACKGROUND_SCAN,
                        nowUValue, nowIValue, nowActivepower, nowReactivePower, nowApparentPower);
                ServerListener.sendASdu(shortFloatAsdu);
            } else {
                LOG.info("数据都与上次相同，就不再上报");
            }
        }
    }
}
