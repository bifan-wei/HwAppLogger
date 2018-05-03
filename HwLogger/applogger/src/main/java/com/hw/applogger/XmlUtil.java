package com.hw.applogger;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO xml格式字符串与实体转化类
 * --------------------
 * 2017年8月25日
 */
public class XmlUtil {
    /**
     * @param xmlData  要生成xml的数据 xmlData
     * @param startTag 每个结构体的开始与结束tag
     * @return xmlData，返回"",否则返回xml格式字符串
     * --------------------
     * TODO pull方式创建xml字符串结构体
     * --------------------
     * 2017年1月16日上午9:52:54
     */
    public static String pullXMLCreate(List<Object> xmlData, String startTag) {
        if (xmlData == null)
            throw new NullPointerException("xmlData can not be null");
        int size = xmlData.size();
        if (size == 0) {
            return "";
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//用这个可以设置编码

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlSerializer xmlSerializer = factory.newSerializer();
            xmlSerializer.setOutput(outputStream, "utf-8"); // 保存创建的xml

            for (int i = 0; i < size; i++) {
                Object obj = xmlData.get(i);
                pullXMLCreateFromObject(xmlSerializer, obj, startTag);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        try {
            return outputStream.toString("utf-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        return "";

    }

    /**
     * @param xmlData  实体数据
     * @param startTag 开始标签
     * @return 不会返回null
     * --------------------
     * TODO pull方式创建xml字符串结构体
     * --------------------
     * 2017年1月16日下午4:22:22
     */
    public static String pullXMLCreate(Object xmlData, String startTag) {

        if (xmlData == null)
            throw new NullPointerException("xmlData can not be null");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//用这个可以设置编码

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlSerializer xmlSerializer = factory.newSerializer();
            xmlSerializer.setOutput(outputStream, "utf-8"); // 保存创建的xml
            pullXMLCreateFromObject(xmlSerializer, xmlData, startTag);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return outputStream.toString("utf-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        return "";
    }

    private static void pullXMLCreateFromObject(XmlSerializer xmlSerializer, Object xmlData, String startTag)
            throws IllegalArgumentException, IllegalStateException, IOException {
        Object obj = xmlData;

        List<Field> fields = getPublicFields(obj.getClass());

        xmlSerializer.startTag("", startTag); // 创建单个结构体开始节点

        for (Field f : fields) {
            String tagName = f.getName();
            Object tagValue = "";
            try {
                // 获得当前属性的类型和值
                // 类型的话如果是基本类型，会自动装箱
                tagValue = f.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tagValue == null) {
                tagValue = "";
            }

            xmlSerializer.startTag("", tagName);
            xmlSerializer.text(tagValue + "");
            xmlSerializer.endTag("", tagName);

        }

        xmlSerializer.endTag("", startTag);// 创建单个结构体结束节点
        xmlSerializer.endDocument();
    }

    private static List<Field> getPublicFields(Class<?> clazz) {

        // 用来存储clazz中用public修饰的属性的list
        List<Field> list = new ArrayList<Field>();
        // 获得clazz中所有用public修饰的属性
        Field[] fields = clazz.getFields();
        // 将fields加入到list中
        for (int i = 0; i < fields.length; i++) {
            list.add(fields[i]);
        }
        return list;

    }

    /**
     * @param xmlStr     字符串
     * @param objectType 对应的数据结构体
     * @return objectType传入错误或导致IllegalAccessException，
     * objectType 与xmlStr的属性不对应会返回new出来的objecttype
     * objectType 与xmlStr的属性运行忽略掉大小写
     * <p>
     * -------------------- TODO 从单个xml格式数据中获取返回bean
     * 2017年1月16日上午10:25:19
     * @throws IOException            IOException
     * @throws XmlPullParserException XmlPullParserException
     */
    public static <T> T getObjectFromXmlObject(String xmlStr, Class<T> objectType)
            throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new StringReader(xmlStr));
        int event = parser.getEventType();

        T t = null;
        try {
            t = objectType.newInstance();
        } catch (InstantiationException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }

        if (t == null)
            return t;

        List<Field> publicFields = getPublicFields(objectType);
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:

                    String name = parser.getName();
                    event = parser.next();
                    String value = parser.getText();

                    for (Field field : publicFields) {
                        // 获得属性名
                        String n = field.getName();
                        // 如果属性名与键相同
                        if (n.equalsIgnoreCase(name)) {

                            try {
                                field.set(t, value);
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            }
                            break;
                        }
                    }

                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            event = parser.next();
        }
        return t;

    }

    /**
     * @param xmlStr     多个xml结构体字符串
     * @param objectType 单个结构体类型
     * @param startTag   单个xml结构体的开始与结束节点名称
     * @return 不会返回null，解析出错可能返回无数据
     * --------------------
     * TODO 多个结构体的xml数据解析，注意的是，objectType 不支持复杂的类型，只支持基本类型格式与字符串
     * --------------------
     */
    public static <T> List<T> getObjectsFromXmlObjects(String xmlStr, Class<T> objectType, String startTag) {

        XmlPullParser parser = Xml.newPullParser();
        List<T> data = new ArrayList<>();

        List<Field> publicFields = getPublicFields(objectType);
        T t = null;

        String key;

        String value;
        try {
            parser.setInput(new StringReader(xmlStr));

            int event;

            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:// 解析开始开始时触发

                        break;
                    case XmlPullParser.START_TAG:// 标签开始时触发

                        key = parser.getName();

                        if (key.equals(startTag)) {// 判断到是开始tag

                            try {// 重新创建一个实例
                                t = objectType.newInstance();
                            } catch (InstantiationException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }

                            break;

                        } else {

                            if (t != null) {// 不出错才处理
                                value = parser.nextText();
                                value = value == null ? "" : value;// 如果是null的话，变为空字符

                                for (Field field : publicFields) {
                                    // 获得属性名
                                    String n = field.getName();
                                    // 如果属性名与键相同
                                    if (n.equalsIgnoreCase(key)) {

                                        Object tagValue;
                                        try {

                                            // 获得当前属性的类型和值
                                            // 类型的话如果是基本类型，会自动装箱
                                            tagValue = field.get(t);
                                            // 判断各种类型，调用各种类型的put方法将数据存储进去

                                            if (tagValue instanceof String || tagValue == null) {// 字符串的话可能出现tagvalue==null
                                                tagValue = value;
                                            } else if (tagValue instanceof Integer) {
                                                tagValue = Integer.valueOf(value);
                                            } else if (tagValue instanceof Float) {
                                                tagValue = Float.valueOf(value);
                                            } else if (tagValue instanceof Long) {
                                                tagValue = Long.valueOf(value);
                                            } else if (tagValue instanceof Boolean) {
                                                tagValue = Boolean.valueOf(value);
                                            } else if (tagValue instanceof Double) {
                                                tagValue = Double.valueOf(value);
                                            }

                                            field.set(t, tagValue);

                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }

                                    }

                                }

                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:// 解析结束时触发
                        key = parser.getName();
                        if (key != null && key.equals(startTag)) {
                            if (t != null) {
                                data.add(t);
                            }
                        }

                        break;
                }

            }
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return data;

    }

}
