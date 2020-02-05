package com.telek.hemsipc.protocal3761.protocal.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandAfn {

    CONFIRM("Confirm", 0),

    ALL_DATA_INITIALIZATION_EXCEPT_COMM("allDataInitializationExceptComm", 1),

    LOGIN("login", 2),
    LOGOUT("logout", 2),
    HEARTBEAT("heartbeat", 2),

    MAIN_STATION_IP("setMainStationIp", 0x4),
    TERMINAL_IP("setTerminalIp", 0x4),
    RESIDUAL_CURRENT_PROTECTOR_CONFIG("setResidualCurrentProtectorConfig", 0x4),
    READ_RUNNING_PARAMS("setReadRunningParams", 0x04),
    TASK_ON_OFF("setTaskOnOff", 0x04),
    READ_TASK_PARAMS("setReadTaskParams", 0x04),
    MEASURE_POINT_BASE_PARAMS("setMeasurePointBaseParams", 0x04),
    TERMINAL_ADDRESS("setTerminalAddress", 0x04),

    GET_TERMINAL_IP("getTerminalIp", 0x0a),
    GET_READ_RUNNING_PARAMS("getReadRunningParams", 0x0a),
    GET_RESIDUAL_CURRENT_PROTECTOR_CONFIG("getResidualCurrentProtectorConfig", 0x0a),
    GET_TASK_ON_OFF("getTaskOnOff", 0x0a),
    GET_READ_TASK_PARAMS("getReadTaskParams", 0x0a),
    GET_MAIN_STATION_IP("getMsIp", 0x0a),
    /**
     * 读取F10时发送的参数
     */
    GET_RESIDUAL_CURRENT_PROTECTOR_CONFIG_SEND_PARAMS("getResidualCurrentProtectorConfigSendParams", 0x0a),
    /**
     * 读取F33时发送的参数
     */
    GET_READ_RUNNING_PARAMS_SEND_PARAMS("getReadRunningParamsSendParams", 0x0a),


    GET_TERMINAL_TIME("terminalTime", 0x0c),
    GET_REAL_TIME_DATA("realTimeData", 0x0c),
    GET_ELEC_REAL_DATA("elecRealData", 0x0c),

    DEFAULT_EMPTY_TEST("", -1),
    ;

    String command;

    int afn;

    public static CommandAfn get(String command) {
        CommandAfn[] values = CommandAfn.values();
        for (CommandAfn value : values) {
            if (value.getCommand().equals(command)) {
                return value;
            }
        }
        return DEFAULT_EMPTY_TEST;
    }

}
