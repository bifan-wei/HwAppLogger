package com.hw.applogger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.txtreaderlib.R;

import java.util.Collections;
import java.util.List;

/**
 * created by ： bifan-wei
 */

public class LogActivity extends AppCompatActivity {
    private String tag = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x01;
    private Boolean Permit = false;

    public static void gotoActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setStatusBarColor();
        setContentView(R.layout.av_hw_applogger);
        findViewIds();
        registerListener();
        if (CheckPermission()) {
            Permit = true;
            loadData();
        }
    }

    private TextView mSelectorText;
    private TextView mLevelText;
    private View mTagR, mTagM, mTagC;
    private View mLevelLayout;
    private EditText mInputText;
    private ListView mListView;
    private View mCahce;
    private TextView mRCacheText, mMCacheText;


    private void findViewIds() {
        mSelectorText = (TextView) findViewById(R.id.av_logger_selector_text);
        mLevelText = (TextView) findViewById(R.id.av_logger_level_text);
        mLevelLayout = findViewById(R.id.av_logger_level_layout);
        mTagR = findViewById(R.id.av_logger_requestLog_tag);
        mTagM = findViewById(R.id.av_logger_msgLog_tag);
        mTagC = findViewById(R.id.av_logger_cache_tag);
        mInputText = (EditText) findViewById(R.id.av_logger_input_text);
        mListView = (ListView) findViewById(R.id.av_logger_list);
        mCahce = findViewById(R.id.av_logger_cache);
        mRCacheText = (TextView) findViewById(R.id.logger_cache_R_text);
        mMCacheText = (TextView) findViewById(R.id.logger_cache_M_text);
    }

    private final int Page_msgLog = 0;
    private final int Page_requestLog = 1;
    private final int Page_Cache = 2;
    private int CurrentPage = Page_msgLog;
    private BaseAdapter mAdapter;
    private LevelMenu levelMenu;

    private void registerListener() {
        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onSearch(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        levelMenu = new LevelMenu(this);
        levelMenu.setLevelClickListener(new LevelMenu.onLevelClickListener() {
            @Override
            public void onLevel(String level) {
                mLevelText.setText(level);
                if (!level.equals("All")) {
                    mInputText.setText(level);
                } else {
                    mInputText.setText("");
                }
                levelMenu.dismiss();
            }
        });

    }

    private void loadData() {
        updatePageTag();
        updateLevel();
        loadMsgLogData();
    }

    private void loadRequestLogData() {
        mListView.setVisibility(View.VISIBLE);
        List<RequestLogBean> data = RequestLogger.getLogs();
        Collections.reverse(data);
        mAdapter = new RequestLogAdapter(this, data);
        mListView.setAdapter(mAdapter);

    }

    private void loadMsgLogData() {
        mListView.setVisibility(View.VISIBLE);
        List<MsgBean> data = MsgLogger.getLogs();
        Collections.reverse(data);
        mAdapter = new MsgLogAdapter(this, data);
        mListView.setAdapter(mAdapter);
    }

    private void loadCacheData() {
        mListView.setVisibility(View.GONE);
        mRCacheText.setText(FileUtil.byteSizeToString(RequestLogger.getLoggerSize()));
        mMCacheText.setText(FileUtil.byteSizeToString(MsgLogger.getLoggerSize()));
    }

    private void updatePageTag() {
        if (CurrentPage == Page_msgLog) {
            mTagM.setVisibility(View.VISIBLE);
            mTagR.setVisibility(View.GONE);
            mTagC.setVisibility(View.GONE);
            mLevelLayout.setVisibility(View.VISIBLE);
        } else if (CurrentPage == Page_requestLog) {
            mTagM.setVisibility(View.GONE);
            mTagR.setVisibility(View.VISIBLE);
            mTagC.setVisibility(View.GONE);
            mLevelLayout.setVisibility(View.GONE);
        } else {
            mTagM.setVisibility(View.GONE);
            mTagR.setVisibility(View.GONE);
            mTagC.setVisibility(View.VISIBLE);
            mLevelLayout.setVisibility(View.GONE);
        }
    }

    private void updateLevel() {
        if (CurrentPage == Page_msgLog) {
            mLevelLayout.setVisibility(View.VISIBLE);
        } else {
            mLevelLayout.setVisibility(View.GONE);
        }
    }

    private void setStatusBarColor() {
        //5.0以上透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void onBack(View view) {
        finish();
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
                loadData();
            } else {
                // Permission Denied
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

    public void onSearchSelector(View view) {

    }

    public void onSearch(View view) {
        if (mAdapter != null && mAdapter instanceof MsgLogAdapter) {
            String text = mInputText.getText().toString().trim();
            if (!text.isEmpty()) {
                ((MsgLogAdapter) mAdapter).getFilter().filter(text);
            } else {
                loadMsgLogData();
            }
        }
    }

    public void onLevelClick(View view) {
        if (!levelMenu.isShowing()) {
            levelMenu.showAsDropDown(mLevelLayout, -50, 0);
        } else {
            levelMenu.dismiss();
        }
    }

    public void onRequestLogClick(View view) {
        CurrentPage = Page_requestLog;
        loadRequestLogData();
        updatePageTag();
    }

    public void onMsgLogClick(View view) {
        CurrentPage = Page_msgLog;
        loadMsgLogData();
        updatePageTag();
    }

    public void onCacheClick(View view) {
        CurrentPage = Page_Cache;
        updatePageTag();
        loadCacheData();
    }

    public void onClearMsgCache(View view) {
        MsgLogger.Clear();
        loadCacheData();
    }

    public void onClearRequestCache(View view) {
        RequestLogger.Clear();
        loadCacheData();
    }


    public void onClearAll(View view) {
        MsgLogger.Clear();
        RequestLogger.Clear();
        loadCacheData();
    }
}
