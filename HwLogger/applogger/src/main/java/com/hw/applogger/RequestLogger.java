package com.hw.applogger;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RequestLogger {
    public static final String tag = "RequestLogger";
    public static String SaveFilePath = Environment.getExternalStorageDirectory() + "/hw526b15";
    public static final String StartTag = "request";
    public static final int FileMaxSize = 100 * 1024 * 1;//限制不超过100KB
    private static Boolean isLog = false;//是否运行记录

    /**
     * @param saveFilePath 只要进行初始化就会默认开启该功能
     */
    public static void init(String saveFilePath) {
        SaveFilePath = saveFilePath;
        isLog = true;
        init();
    }

    public static void init(){
        isLog = true;
        if (isLog && !FileUtil.isFileExist(SaveFilePath)) {
            FileUtil.createFile(SaveFilePath);
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


    public static void log(RequestLogBean requestLogBean) {
        if (isLog) {
            checkFileSize();
            Encode(requestLogBean);
            String xmlData = XmlUtil.pullXMLCreate(requestLogBean, StartTag);
            FileUtil.writeToFile(xmlData, SaveFilePath, true);
        }
    }



    /**
     * 检查文件大小，过大的话进行清除
     */
    private static void checkFileSize() {
        if (getLoggerSize() >= FileMaxSize) {
            Clear();
        }
    }

    public static void log(String tag,Boolean isSuccess,String url, String params, String response) {
        RequestLogBean requestLogBean = new RequestLogBean();
        requestLogBean.IsSuccess = isSuccess;
        requestLogBean.Time = TimeUtil.getCurrentDateString();
        requestLogBean.Url = url;
        requestLogBean.Params = params;
        requestLogBean.Tag = tag;
        requestLogBean.Response = response;
        log(requestLogBean);
    }

    public static void Clear() {
        if (isLog) {
            FileUtil.writeToFile("", SaveFilePath, false);
        }
    }

    /**
     * @return 不会返回null
     * 获取异常记录信息数据
     */
    public static List<RequestLogBean> getLogs() {
        if (isLog) {
            String exceptionStr = FileUtil.readFromFile(SaveFilePath);
            List<RequestLogBean> exceptions = XmlUtil.getObjectsFromXmlObjects(exceptionStr, RequestLogBean.class,
                    StartTag);
            Decode(exceptions);
            return exceptions;
        } else {

            return new ArrayList<>();
        }
    }

    private static void Decode(List<RequestLogBean> exceptions) {
        for(RequestLogBean requestLogBean:exceptions ){
            requestLogBean.Params = LoggerEncrypt.decode(requestLogBean.Params + "");
            requestLogBean.Response = LoggerEncrypt.decode(requestLogBean.Response + "");
            requestLogBean.Url =  LoggerEncrypt.decode(requestLogBean.Url + "");
        }
    }

    private static void Encode(RequestLogBean requestLogBean) {
        requestLogBean.Params = LoggerEncrypt.encode(requestLogBean.Params + "");
        requestLogBean.Response = LoggerEncrypt.encode(requestLogBean.Response + "");
        requestLogBean.Url =  LoggerEncrypt.encode(requestLogBean.Url + "");
    }

}
