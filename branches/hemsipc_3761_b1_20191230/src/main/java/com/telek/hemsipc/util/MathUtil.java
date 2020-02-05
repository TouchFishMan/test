package com.telek.hemsipc.util;

import java.math.BigDecimal;

public class MathUtil {

    public static double divide(double a, double b, int scale) {
        BigDecimal divide = new BigDecimal(a).divide(new BigDecimal(b), scale, BigDecimal.ROUND_HALF_UP);
        return divide.doubleValue();
    }

}
