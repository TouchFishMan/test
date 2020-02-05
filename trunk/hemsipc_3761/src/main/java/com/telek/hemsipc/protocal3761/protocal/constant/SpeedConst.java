package com.telek.hemsipc.protocal3761.protocal.constant;

/**
 * 波特率
 */
public enum SpeedConst {
    SPEED_600(1,600),
    SPEED_1200(2,1200),
    SPEED_2400(3,2400),
    SPEED_4800(4,4800),
    SPEED_7200(5,7200),
    SPEED_9600(6,9600),
    SPEED_12900(7,12900);

    int code;
    int speed;

    SpeedConst(int code, int speed) {
        this.code = code;
        this.speed = speed;
    }

    public int getCode() {
        return this.code;
    }

    public int getSpeed() {
        return this.speed;
    }

    public static SpeedConst getByCode(int code) {
        for (SpeedConst value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
