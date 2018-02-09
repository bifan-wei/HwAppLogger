package com.hw.applogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class FileUtil {

    public static String byteSizeToString(long byteSize) {
        if (byteSize <= 0) {
            return "0B";
        }
        if (byteSize < 1024)
            return byteSize + "B";

        int kb = Math.round(byteSize / 1024);

        if (kb >= 1024) {
            float mb = FormatUtil.getFloat_KeepTwoDecimalplaces(((float) kb) / 1024);
            return mb + " MB";
        }

        return kb + " KB";
    }

    /**
     * @param path 为null 导致null exception
     * @return --------------------

     */
    public static Boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * @param path
     */
    public static Boolean createFile(String path) {
        File file = new File(path);
        try {
            file.createNewFile();
            return true;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * @param path --------------------
     */
    public static void mkdirs(String path) {
        File file = new File(path);
        file.mkdirs();
    }

    /**
     * 调用该方法前确保文件是存在
     *
     * @param saveStr 要写入的字符串
     * @param filepath       文件路径
     * @param inAppendMode   是否是后面追加模式
     */
    public static void writeToFile(String saveStr, String filepath, Boolean inAppendMode) {
        try {
            FileWriter writer = new FileWriter(filepath, inAppendMode);
            writer.write(saveStr);
            writer.close();
        } catch (IOException e1) {

            e1.printStackTrace();
        }

    }

    /**
     * @param filepath
     * @return 文件不存在是返回""
     */
    public static String readFromFile(String filepath) {
        try {
            FileReader reader = new FileReader(filepath);
            StringBuffer stringBuffer = new StringBuffer();

            int length = 512;
            char[] cs = new char[length];
            int numS;
            while ((numS = reader.read(cs, 0, length)) != -1) {
                if (numS < length && numS > 0) {
                    char[] cs1 = new char[numS];
                    System.arraycopy(cs, 0, cs1, 0, numS);
                    stringBuffer.append(cs1);
                } else {
                    stringBuffer.append(cs);
                }

            }
            reader.close();
            return stringBuffer.toString();

        } catch (IOException e1) {

            e1.printStackTrace();
        }
        return "";
    }

    /**
     * @param fileFrom   要复制的源文件路径
     * @param savePathTo 要复制保存的路径
     * @return --------------------
     */
    public static Boolean copyFile(String fileFrom, String savePathTo) {
        return copyFile(new File(fileFrom), savePathTo, null);
    }

    public static Boolean copyFile(String fileFrom, String savePathTo, FileCopyListener copyListsner) {
        return copyFile(new File(fileFrom), savePathTo, copyListsner);
    }

    /**
     * @param fileFrom   要复制的源文件
     * @param savePathTo 要复制保存的路径
     * @return 返回复制是否成功
     */
    public static Boolean copyFile(File fileFrom, String savePathTo, FileCopyListener copyListener) {

        if (fileFrom == null || !fileFrom.exists()) {
            if (copyListener != null) {
                copyListener.onFail();
            }
            return false;
        }

        File file = new File(savePathTo);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {

            e.printStackTrace();
        }

        if (!file.exists()) {
            if (copyListener != null) {
                copyListener.onFail();
            }
            return false;
        }

        try {
            if (copyListener != null) {
                copyListener.onStart();
            }
            long sumLength = fileFrom.length();
            long copiedNum = 0;
            FileInputStream fis = new FileInputStream(fileFrom);
            FileOutputStream outputStream = new FileOutputStream(file);
            int byteCount = 512;
            int readNum = 0;
            byte[] buffer = new byte[byteCount];
            try {
                while ((readNum = (fis.read(buffer))) != -1) {
                    outputStream.write(buffer, 0, readNum);
                    copiedNum = copiedNum + readNum;
                    if (copyListener != null) {
                        copyListener.onProgress(copiedNum, sumLength);
                    }
                }

                if (copyListener != null) {
                    copyListener.onProgress(sumLength, sumLength);
                    copyListener.onEnd();
                }
                fis.close();
                outputStream.close();
                return true;
            } catch (IOException e) {

                e.printStackTrace();
            } finally {

            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        if (copyListener != null) {
            copyListener.onFail();
        }

        return false;
    }

    public interface FileCopyListener {
         void onStart();
         void onFail();
         void onEnd();
         void onProgress(long copiedNum, long sumNum);
    }
}
