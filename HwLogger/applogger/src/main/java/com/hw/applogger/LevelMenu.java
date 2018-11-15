package com.hw.applogger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hw.txtreaderlib.R;


public class LevelMenu extends PopupWindow {
    private View mRootView;
    private Context mContext;

    LevelMenu(Context context) {
        super(context);
        mContext = context;
        initRootView();
    }

    private void initRootView() {
        mRootView = LinearLayout.inflate(mContext, R.layout.view_level_menu, null);
        this.setContentView(mRootView);
        //设置包裹状态
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2183fb")));
        registerListener();
    }

    private int viewWidth = 0;

    public int getWidth() {
        if (viewWidth == 0 && mRootView != null) {
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mRootView.measure(width, height);
            viewWidth = mRootView.getMeasuredWidth();
        }
        return viewWidth;
    }


    private void registerListener() {
        View all = mRootView.findViewById(R.id.logger_menu_all);
        View info = mRootView.findViewById(R.id.logger_menu_info);
        View debug = mRootView.findViewById(R.id.logger_menu_debug);
        View error = mRootView.findViewById(R.id.logger_menu_error);
        all.setOnClickListener(new LevelClick(LogLevel.ALL));
        info.setOnClickListener(new LevelClick(LogLevel.Info));
        debug.setOnClickListener(new LevelClick(LogLevel.Debug));
        error.setOnClickListener(new LevelClick(LogLevel.Error));
    }

    private class LevelClick implements View.OnClickListener {
        String Level;

        LevelClick(String level) {
            Level = level;
        }

        @Override
        public void onClick(View view) {
            if (levelClickListener != null) {
                levelClickListener.onLevel(Level);
            }
        }
    }

    private onLevelClickListener levelClickListener;

    public interface onLevelClickListener {
        void onLevel(String level);
    }

    public void setLevelClickListener(onLevelClickListener levelClickListener) {
        this.levelClickListener = levelClickListener;
    }
}
