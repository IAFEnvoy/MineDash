package com.iafenvoy.minedash.util;

public final class MathUtil {
    public static double findClosestMultipleOf90(double num) {
        double quotient = num / 90;
        long integerPart = (long) quotient;
        double fractionalPart = quotient - integerPart;
        double lowerMultiple = integerPart * 90;
        double upperMultiple = (integerPart + 1) * 90;
        return fractionalPart < 0.5 ? lowerMultiple : upperMultiple;
    }
}
