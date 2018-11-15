package com.hw.logger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hw.applogger.LogActivity;
import com.hw.applogger.MsgLogger;
import com.hw.applogger.RequestLogger;

public class MainActivity extends AppCompatActivity {
    String className = this.getClass().getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x01;
    private Boolean Permit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (CheckPermission()) {
            Permit = true;
            MsgLogger.init();
            RequestLogger.init();
        }else{
            toast("权限拒绝");
        }


    }


    private Boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Permit = true;
                MsgLogger.init();
                RequestLogger.init();
            } else {
                toast("Permission Denied");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private Toast t;

    private void toast(String msg) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        t.show();
    }


    public void onClick(View view) {
        if(!Permit){
            toast("Permission Denied");
            return;
        }
        MsgLogger.i(className,"i","testaa业交流版  556   777  ffgfg 面");
        MsgLogger.e(className,"e","testaa");
        MsgLogger.d(className,"d(","testaa");
        className = "AppCompatActivity";
        MsgLogger.i(className,"i","testaa业交流版  556   777  ffgfg 面");
        MsgLogger.e(className,"e","testaa");
        MsgLogger.d(className,"d(","testaa");

        RequestLogger.log(className,false,"http://blog.csdn.net/yangshangwei/article/details/51271725","get post ","之前为了优化gradle的编译速度，选择了Offline Work模式，取消即可");
        LogActivity.gotoActivity(this);
    }
}
