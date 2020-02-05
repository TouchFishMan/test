package com.telek.hemsipc.protocal3761.protocal.constant;

/**
 * 波特率
 */
public enum SpeedConst {
    SPEED_600(1),
    SPEED_1200(2),
    SPEED_2400(3),
    SPEED_4800(4),
    SPEED_7200(5),
    SPEED_9600(6),
    SPEED_12900(7);

    int speed;

    SpeedConst(int speed) {
        this.speed = speed;
    }

    public int getCode() {
        return this.speed;
    }

    public static SpeedConst getByCode(int code) {
        for (SpeedConst value : values()) {
            if (value.speed == code) {
                return value;
            }
        }
        return null;
    }
}
