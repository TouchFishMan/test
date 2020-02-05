package com.telek.hemsipc.protocal3761;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 缓存
 */
public abstract class Protocol3761Cache {

    /**
     * 終端地址A2-地址yu 一一对应
     */
    public static final Map<String, String> ADDRESS2_TERMINAL_ADDRESS = new ConcurrentHashMap<>();

    /**
     * 登录的终端
     */
    public static final Map<String, Object> TERMINAL_ADDRESS2_LOGIN = new ConcurrentHashMap<>();

    /**
     * 终端心跳key-address2, value-time
     */
    public static final Map<String, String> TERMINAL_ADDRESS2_HEARTBEAT = new ConcurrentHashMap<>();

    /**
     * key: ternimal address
     * value: device_id
     */
    public static final Map<String, String> TERMINAL_ADDRESS_LINE_DEVICE_ID = new ConcurrentHashMap<>();

    /**
     * 发生意外断开链接取消缓存
     * @param address2
     */
    public static void accidentRemoveAddress2(String address2) {
        if (address2 == null) {
            return;
        }
        ADDRESS2_TERMINAL_ADDRESS.remove(address2);
        TERMINAL_ADDRESS2_LOGIN.remove(address2);
        TERMINAL_ADDRESS2_HEARTBEAT.remove(address2);
    }

}
