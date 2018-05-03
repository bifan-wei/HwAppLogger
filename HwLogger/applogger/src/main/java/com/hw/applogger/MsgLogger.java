package com.hw.applogger;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * bifan-wei
 */
public class MsgLogger {
    public static String SaveFilePath = Environment.getExternalStorageDirectory() + "/hw526b14";
    public static final String StartTag = "exception";
    public static final int FileMaxSize = 100 * 1024 * 1; //限制不超过100K
    private static Boolean isLog = false;//是否运行记录

    /**
     * @param saveFilePath 只要进行初始化就会默认开启该功能
     */
    public static void init(String saveFilePath) {
        SaveFilePath = saveFilePath;
        isLog = true;
        init();
    }

    public static void init() {
        isLog = true;
        if (isLog && !new File(SaveFilePath).exists()) {
            new File(SaveFilePath).canRead();
        }
    }


    /**
     * @return 返回的是字节数
     * --------------------
     * 获取异常日志文件大小
     */
    public static long getLoggerSize() {
        if (isLog) {
            File file = new File(SaveFilePath);
            return file.length();
        }
        return 0;
    }

    /**
     * @param msgBean
     */
    public static void log(MsgBean msgBean) {
        if (isLog) {
            checkFileSize();
            msgBean.Message = LoggerEncrypt.encode(msgBean.Message + "");
            String xmlData = XmlUtil.pullXMLCreate(msgBean, StartTag);
            LoggerUtil.writeToFile(xmlData, SaveFilePath, true);
        }
    }


    public static void log(@NonNull String className, String tag, String exceptionMsg, String level) {
        MsgBean msgBean = new MsgBean();
        msgBean.ClassName = className;
        msgBean.Message = exceptionMsg;
        msgBean.Level = level;
        tag = tag == null ? "" : tag;
        msgBean.Tag = tag;
        msgBean.Time = LoggerUtil.getCurrentDateString();
        log(msgBean);

    }

    public static void d(@NonNull String className, String tag, String exceptionMsg) {
        log(className, tag, exceptionMsg, LogLevel.Debug);

    }

    public static void i(@NonNull String className, String tag, String exceptionMsg) {
        log(className, tag, exceptionMsg, LogLevel.Info);

    }

    public static void e(@NonNull String className, String tag, String exceptionMsg) {
        log(className, tag, exceptionMsg, LogLevel.Error);

    }

    public static void d(@NonNull String className, String exceptionMsg) {
        log(className, null, exceptionMsg, LogLevel.Debug);

    }

    public static void i(@NonNull String className, String exceptionMsg) {
        log(className, null, exceptionMsg, LogLevel.Info);

    }

    public static void e(@NonNull String className, String exceptionMsg) {
        log(className, null, exceptionMsg, LogLevel.Error);

    }

    /**
     * --------------------
     * 检查文件大小，过大的话进行清除
     */
    private static void checkFileSize() {
        if (getLoggerSize() >= FileMaxSize) {
            Clear();
        }
    }

    public static void Clear() {
        if (isLog) {
            LoggerUtil.writeToFile("", SaveFilePath, false);
        }
    }

    /**
     * @return 不会返回null
     * --------------------
     * 获取异常记录信息数据
     */
    public static List<MsgBean> getLogs() {
        if (isLog) {
            String exceptionStr = LoggerUtil.readFromFile(SaveFilePath);
            List<MsgBean> exceptions = XmlUtil.getObjectsFromXmlObjects(exceptionStr, MsgBean.class, StartTag);
            Decode(exceptions);
            return exceptions;
        } else {
            return new ArrayList<>();
        }
    }

    private static void Decode(List<MsgBean> exceptions) {
        for (MsgBean e : exceptions) {
            e.Message = LoggerEncrypt.decode(e.Message);
        }
    }


}
