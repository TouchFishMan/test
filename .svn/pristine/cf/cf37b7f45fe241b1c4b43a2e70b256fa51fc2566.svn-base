package com.telek.hemsipc.protocal3761;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.protocal3761.netty.NettyStarter;
import com.telek.hemsipc.protocal3761.protocal.ProtocalManagerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Protocol3761Starter {

    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public static void start() {
        protocolInit();
        nettyInit();
    }

    private static void nettyInit() {
        singleThreadExecutor.execute(HemsipcSpringContext.getBean(NettyStarter.class));
    }

    private static void protocolInit() {
        try {
            ProtocalManagerFactory.getProtocalManager("rgm_376_1");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("376协议初始化加载失败...");
        }
    }

}
