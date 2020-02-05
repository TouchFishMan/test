package com.telek.hemsipc.protocal3761;

import com.telek.hemsipc.protocal3761.protocal.Packet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 缓存
 */
public abstract class Protocol3761Cache {

    /**
     * 終端地址A2-地址yu 一一对应
     */
//    public static final Map<String, String> ADDRESS2_TERMINAL_ADDRESS = new ConcurrentHashMap<>();

    /**
     * 登录的终端
     */
    public static final Map<String, Object> TERMINAL_ADDRESS_LOGIN = new ConcurrentHashMap<>();

    /**
     * 终端心跳key-address2, value-time
     */
    public static final Map<String, String> TERMINAL_ADDRESS_HEARTBEAT = new ConcurrentHashMap<>();

    /**
     * key: ternimal address
     * value: device_id
     */
    public static final Map<String, String> TERMINAL_ADDRESS_LINE_DEVICE_ID = new ConcurrentHashMap<>();

    /**
     * HTTP通道缓存，用于异步接收集中器返回指令后发送http response
     * key: terminalAddress:seq  例如 1122022f:1
     * value: channelId   127.0.0.1:42812
     */
    public static Map<String, String> HTTP_COMMAND_CHANNEL_ID_MAP = new ConcurrentHashMap<>();

    /**
     * 发送的帧缓存，前台展示发送给集中器的指令帧
     * key: terminalAddress:seq
     * value: byte[]
     */
    public static Map<String, String> TERMINAL_SEQ_SEND_DATA = new ConcurrentHashMap<>();

    /**
     * 接收的帧缓存，前台展示集中器返回的的指令帧
     * key: terminalAddress:seq
     * value: byte[]
     */
    public static Map<String, String> TERMINAL_SEQ_RECEIVE_DATA = new ConcurrentHashMap<>();

    /**
     * 接收的帧解码得到的packet对象，作为查询指令的缓存
     * key： terminalAddress:command  例如 1122022f:getTerminalIp
     * value: Map<data:obj,sendCommand:"",receiveCommand:"">
     */
    public static Map<String, Map<String, Object>> TERMINAL_GET_COMMAND_PACKET = new ConcurrentHashMap<>();

    /**
     * 发生意外断开链接取消缓存
     *
     * @param address
     */
    public static void accidentRemoveAddress(String address) {
        if (address == null) {
            return;
        }
        TERMINAL_ADDRESS_LOGIN.remove(address);
        TERMINAL_ADDRESS_HEARTBEAT.remove(address);
    }

}
