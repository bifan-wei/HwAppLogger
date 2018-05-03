package com.hw.logger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hw.applogger.LogActivity;
import com.hw.applogger.MsgLogger;
import com.hw.applogger.RequestLogger;

public class MainActivity extends AppCompatActivity {
    String className = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MsgLogger.init();
        RequestLogger.init();
    }

    public void onClick(View view) {
        MsgLogger.i(className,"i","testaa业交流版  556   777  ffgfg 面");
        MsgLogger.e(className,"e","testaa");
        MsgLogger.d(className,"d(","testaa");
        className = "AppCompatActivity";
        MsgLogger.i(className,"i","testaa业交流版  556   777  ffgfg 面");
        MsgLogger.e(className,"e","testaa");
        MsgLogger.d(className,"d(","testaa");


        RequestLogger.log(className,true,"http://blog.csdn.net/yangshangwei/article/details/51271725","get post ","之前为了优化gradle的编译速度，选择了Offline Work模式，取消即可");


        LogActivity.gotoActivity(this);
        //ActivityWithWebView.LoadUrl(this,"https://wk.baidu.com/search?word=android","testSearch");
    }
}
