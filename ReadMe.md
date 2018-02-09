# HwAppLogger
AppLogger for android

# compile it:
```java
      allprojects {
           repositories {
             maven { url 'https://jitpack.io' }
                  ...
            }
         }
          compile 'com.github.bifan-wei:HwAppLogger:v1.0.0'
 ```
<br>
<br>

# how to use:

## 1.init Logger:
```java
        MsgLogger.init("MsgLogger save file path");
        RequestLogger.init("RequestLogger save file path");
```

<br>
<br>

## 2.use logger:
```java
        MsgLogger.i(className,"i","testaaҵ������  556   777  ffgfg ��");
        MsgLogger.e(className,"e","testaa");
        MsgLogger.d(className,"d(","testaa");
        RequestLogger.log(className,true,"http://blog.csdn.net/yangshangwei/article/details/51271725","get post ","֮ǰΪ����           ��gradle�ı����ٶȣ�ѡ����Offline Workģʽ��ȡ������");
```
<br>
<br>

## 3.show your log:
```java
        LogActivity.gotoActivity(this);
```

![image](https://github.com/bifan-wei/HwAppLogger/blob/master/Cache.png)

![image](https://github.com/bifan-wei/HwAppLogger/blob/master/MgsLog.png)