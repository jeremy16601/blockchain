package com.blockchain.robot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceFormatUtil {

    public static String format(double price) {
        return String.format("%.8f", price);
    }

    public static double formatDouble5(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(5, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

}
