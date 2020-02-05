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

    MAIN_STATION_IP("setMsIp",0x4),
    TERMINAL_IP("setTerminalIp",0x4),
    RESIDUAL_CURRENT_PROTECTOR_CONFIG("setResidualCurrentProtectorConfig",0x4),

    GET_TERMINAL_IP("getTerminalIp", 0x0a),

    TERMINAL_TIME("terminalTime",0x0c),
    REAL_TIME_DATA("realTimeData",0x0c),
    ELEC_REAL_DATA("elecRealData",0x0c),

    DEFAULT_EMPTY_TEST("", -1),;

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
