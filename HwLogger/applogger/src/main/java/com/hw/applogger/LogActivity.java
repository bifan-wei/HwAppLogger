package com.hw.applogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.txtreaderlib.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * created by ： bifan-wei
 */

public class LogActivity extends Activity {
    private String tag = "MainActivity";

    public static void gotoActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStatusBarColor();
        setContentView(R.layout.av_hw_applogger);
        findViewIds();
        registerListener();
        loadData();

    }

    private TextView mLevelText;
    private View mTagR, mTagM;
    private View mLevelLayout;
    private ListView mListView;
    private TextView mRCacheText, mMCacheText;


    private void findViewIds() {
        mLevelText = (TextView) findViewById(R.id.av_logger_level_text);
        mLevelLayout = findViewById(R.id.av_logger_level_layout);
        mTagR = findViewById(R.id.av_page_switcher2);
        mTagM = findViewById(R.id.av_page_switcher1);
        mListView = (ListView) findViewById(R.id.av_logger_list);
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
        levelMenu = new LevelMenu(this);
        levelMenu.setLevelClickListener(new LevelMenu.onLevelClickListener() {
            @Override
            public void onLevel(String level) {
                mLevelText.setText(level);
                levelMenu.dismiss();
                if (mAdapter != null) {
                    ((MsgLogAdapter) mAdapter).getFilter().filter(level);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (levelMenu != null && levelMenu.isShowing()) {
            levelMenu.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        levelMenu = null;
        mLevelText = null;
        mTagR = null;
        mTagM = null;
        mLevelLayout = null;
        mListView = null;
        mRCacheText = null;
        mMCacheText = null;
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
        mRCacheText.setText(byteSizeToString(RequestLogger.getLoggerSize()));
        mMCacheText.setText(byteSizeToString(MsgLogger.getLoggerSize()));
    }

    private void updatePageTag() {
        if (CurrentPage == Page_msgLog) {
            mTagM.setBackgroundResource(R.drawable.shape_item_selected);
            mTagR.setBackgroundResource(R.drawable.shape_item_unselected);
            mLevelLayout.setVisibility(View.VISIBLE);
        } else if (CurrentPage == Page_requestLog) {
            mTagM.setBackgroundResource(R.drawable.shape_item_unselected);
            mTagR.setBackgroundResource(R.drawable.shape_item_selected);
            mLevelLayout.setVisibility(View.GONE);
        } else {
            mTagM.setBackgroundResource(R.drawable.shape_item_unselected);
            mTagR.setBackgroundResource(R.drawable.shape_item_unselected);
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

    private Toast t;

    private void toast(String msg) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        t.show();
    }


    public void onLevelClick(View view) {
        if (levelMenu.isShowing()) {
            levelMenu.dismiss();
        } else {
            levelMenu.showAsDropDown(mLevelLayout, 0, 2);
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

    private String byteSizeToString(long byteSize) {
        if (byteSize <= 0) {
            return "0B";
        }
        if (byteSize < 1024)
            return byteSize + "B";

        int kb = Math.round(byteSize / 1024);

        if (kb >= 1024) {
            float mb = getFloat_KeepTwoDecimalplaces(((float) kb) / 1024);
            return mb + " MB";
        }
        return kb + " KB";
    }

    private float getFloat_KeepTwoDecimalplaces(float floatV) {
        DecimalFormat df = new DecimalFormat("0.0#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String value = df.format(floatV);
        float a = Float.valueOf(value);
        return a;
    }

}
