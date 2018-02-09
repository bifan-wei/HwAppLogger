package com.hw.applogger;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数据格式化工具
 */
 class FormatUtil {


    /**
     * @param floatV
     * @return 判断浮点数是否是整数
     */
    public static Boolean IsInt(float floatV) {
        float f = floatV;
        int i = (int) f;
        return i == f;
    }

    public static String getShowStr__KeepTwoDecimalplaces(float floatV) {
        if (IsInt(floatV)) {
            return (int) floatV + "'";
        }
        return getFloat_KeepTwoDecimalplaces(floatV) + "";
    }

    public static String getShowStr__KeepOneDecimalplaces(float floatV) {
        if (IsInt(floatV)) {
            return (int) floatV + "'";
        }
        return getFloat_KeepOneDecimalplaces(floatV) + "";
    }

    /**
     * @param floatV 保留两位小数点，舍入为四舍五入
     * @return
     */
    public static float getFloat_KeepTwoDecimalplaces(float floatV) {
        String value = getFloatFormat_KeepTwoDecimalplaces().format(floatV);
        float a = Float.valueOf(value);
        return a;
    }

    /**
     * @param doublev 保留两位小数点，舍入为四舍五入
     * @return
     */
    public static float getFloat_KeepTwoDecimalplaces(Double doublev) {
        String value = getFloatFormat_KeepTwoDecimalplaces().format(doublev);
        float a = Float.valueOf(value);
        return a;
    }

    /**
     * @param floatV 保留1位小数点，舍入为四舍五入
     * @return
     */
    public static float getFloat_KeepOneDecimalplaces(float floatV) {
        String value = getFloatFormat_KeepOneDecimalplaces().format(floatV);
        float a = Float.valueOf(value);
        return a;
    }

    /**
     * @param doubleV 保留1位小数点，舍入为四舍五入
     * @return
     */
    public static float getFloat_KeepOneDecimalplaces(Double doubleV) {
        String value = getFloatFormat_KeepOneDecimalplaces().format(doubleV);
        float a = Float.valueOf(value);
        return a;
    }

    /**
     * @param floatV
     * @return 舍入为四舍五入
     * --------------------
     */
    public static int getInt(float floatV) {
        String value = getIntFormat().format(floatV);
        int a = Integer.valueOf(value);
        return a;
    }


    /**
     * @param doubleV
     * @return 舍入为四舍五入
     */
    public static int getInt(Double doubleV) {
        String value = getIntFormat().format(doubleV);
        int a = Integer.valueOf(value);
        return a;
    }

    private static DecimalFormat getFloatFormat_KeepTwoDecimalplaces() {
        DecimalFormat df = new DecimalFormat("0.0#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return  df;
    }

    private static DecimalFormat getFloatFormat_KeepOneDecimalplaces() {
        DecimalFormat df = new DecimalFormat("0.#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return  df;
    }

    private static DecimalFormat getIntFormat() {
        DecimalFormat df = new DecimalFormat("0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return  df;
    }

}
