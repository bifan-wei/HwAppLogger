# HwAppLogger
android日志记录与查看模块

# compile it:
```java
      allprojects {
           repositories {
             maven { url 'https://jitpack.io' }
                  ...
            }
         }
          compile 'com.github.bifan-wei:HwAppLogger:v1.1.0'
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
        MsgLogger.i(className,"i","testaa业交流版  556   777  ffgfg 面");
        MsgLogger.e(className,"e","testaa");
        MsgLogger.d(className,"d(","testaa");
        RequestLogger.log(className,true,"http://blog.csdn.net/yangshangwei/article/details/51271725","get post ","之前为了优           化gradle的编译速度，选择了Offline Work模式，取消即可");
```
<br>
<br>

## 3.show your log:
```java
        LogActivity.gotoActivity(this);
```

![image](https://github.com/bifan-wei/HwAppLogger/blob/master/Cache.png)

![image](https://github.com/bifan-wei/HwAppLogger/blob/master/MgsLog.png)
