package com.telek.hemsipc.protocal3761.model;

import lombok.Data;

/**
 * 电表信息
 */
@Data
public class DeviceDetail {
    // 集中器终端地址
    private String terminalAddress;
    // 电表地址
    private int line;
    // 云端对应的设备id
    private String deviceId;
    // 密钥
    private String key;
}
