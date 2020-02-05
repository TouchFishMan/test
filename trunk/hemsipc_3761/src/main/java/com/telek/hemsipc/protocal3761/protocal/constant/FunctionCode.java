package com.telek.hemsipc.protocal3761.protocal.constant;

public interface FunctionCode {
    /********************************************************/
    /**********当启动标志位PRM=1时, 启动站********************/
    /********************************************************/
    /**
     * 发送确认--复位命令
     */
    int PRM_START_SEND_CONFIRM = 1;
    /**
     * 发送无回答--用户数据
     */
    int PRM_START_SEND_NO_ANSWER = 4;
    /**
     * 请求响应-链路测试
     */
    int PRM_START_REQUEST_RESPONSE_LINK = 9;
    /**
     * 请求响应-1类数据
     */
    int PRM_START_REQUEST_RESPONSE_1 = 10;
    /**
     * 请求响应-2类数据
     */
    int PRM_START_REQUEST_RESPONSE_2 = 11;

    /********************************************************/
    /**********当启动标志位PRM=0时, 从动站********************/
    /********************************************************/
    /**
     * 确认--认可
     */
    int PRM_SLAVE_CONFIRM = 0;
    /**
     * 响应-用户数据
     */
    int PRM_SLAVE_RESPONSE_USER_DATA = 8;
    /**
     * 响应-否认-无所召唤数据
     */
    int PRM_SLAVE_RESPONSE_DENY = 9;
    /**
     * 响应-链路状态
     */
    int PRM_SLAVE_RESPONSE_LINK = 11;

}
