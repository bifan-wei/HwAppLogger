package com.hw.applogger;

import android.annotation.SuppressLint;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * created by ： bifan-wei
 */

public class LoggerUtil {


    public static String getTimeByString(String Time, String fromFormat, String toFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);

        try {
            return getTimeByDate(sdf.parse(Time), toFormat);
        } catch (Exception var5) {
            var5.printStackTrace();
            return Time;
        }
    }

    public static String getCurrentDateString() {
        return getCurrentDateString("yyyyMMdd HH:mm:ss");
    }

    public static String getCurrentDateString(String form) {
        return getTimeByDate(getCurrentDate(), form);
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return date;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String getTimeByDate(Date date, String form) {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat(form);
        return simpleFormatter.format(date);
    }


    public static String readFromFile(String filepath) {
        try {
            FileReader e1 = new FileReader(filepath);
            StringBuffer stringBuffer = new StringBuffer();
            short length = 512;
            char[] cs = new char[length];
            boolean numS = false;

            while (true) {
                int numS1;
                while ((numS1 = e1.read(cs, 0, length)) != -1) {
                    if (numS1 < length && numS1 > 0) {
                        char[] cs1 = new char[numS1];
                        System.arraycopy(cs, 0, cs1, 0, numS1);
                        stringBuffer.append(cs1);
                    } else {
                        stringBuffer.append(cs);
                    }
                }

                e1.close();
                return stringBuffer.toString();
            }
        } catch (IOException var7) {
            var7.printStackTrace();
            return "";
        }
    }


    public static void writeToFile(String saveStr, String filepath, Boolean inAppendMode) {
        try {
            FileWriter e1 = new FileWriter(filepath, inAppendMode.booleanValue());
            e1.write(saveStr);
            e1.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }
}
