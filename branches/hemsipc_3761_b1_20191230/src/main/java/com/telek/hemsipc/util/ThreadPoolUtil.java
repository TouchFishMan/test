package com.telek.hemsipc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.telek.hemsipc.server.DealCloudDataServer;
import com.telek.hemsipc.server.ElecSupplyServer;
import com.telek.hemsipc.server.FirmwareUpdateServer;

/**
 * @Auther: wll
 * @Date: 2018/9/17 09:38
 * @Description:
 */
public class ThreadPoolUtil {
    /**
     * 设备电量补发线程池.
     */
    private static ExecutorService elecSupplyExecutor = Executors.newSingleThreadExecutor();
    /**
     * 处理云端反馈和控制线程池.
     */
    private static ExecutorService dealCloudDataExecutor = Executors.newFixedThreadPool(4);
 
    /**
     * 固件升级线程池.
     */
    private static ExecutorService firmwareUpdateExecutor = Executors.newSingleThreadExecutor();

    /**
     * 可定时线程池
     */
    private static ScheduledExecutorService timerExecutor = Executors.newScheduledThreadPool(2);
    
    
    public static void executeElecSupply(ElecSupplyServer elecSupplyServer) {
        elecSupplyExecutor.execute(elecSupplyServer);
    }

    public static void executeDealCloudData(DealCloudDataServer dealCloudDataServer) {
        dealCloudDataExecutor.execute(dealCloudDataServer);
    }

    public static synchronized void exceuteFirmwareUpdate(FirmwareUpdateServer firmwareUpdateServer) {
        //固件升级只能升级一次，升级失败即代表不能升级了，线程池关闭，不接受新的固件升级任务
        firmwareUpdateExecutor.execute(firmwareUpdateServer);
        firmwareUpdateExecutor.shutdown();
    }
    
    public static ScheduledFuture<?> addTimerTask(Runnable task, long initialDelay, long period, TimeUnit unit) {
        ScheduledFuture<?> newFuture = timerExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
        return newFuture;
    }
 
    public static ScheduledFuture<?> addTimerTask(Runnable task, long delay,TimeUnit unit) {
        ScheduledFuture<?> newFuture = timerExecutor.schedule(task, delay, unit);
        return newFuture;
    }
}
