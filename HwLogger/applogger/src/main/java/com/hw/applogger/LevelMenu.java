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

    public LevelMenu(Context context) {
        super(context);
        mContext = context;
        initRootView();
    }

    public void initRootView() {


        mRootView = LinearLayout.inflate(mContext, R.layout.view_level_menu, null);

        this.setContentView(mRootView);
        //设置固定大小状态
       // this.setWidth();
       // this.setHeight(rootHeight);

        //设置包裹状态
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setFocusable(false);
        this.setOutsideTouchable(true);
        //  this.setAnimationStyle(R.style.popmenu_animation);
          this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2183fb")));

        RegisterListener();


    }

    private void RegisterListener() {
        View all = mRootView.findViewById(R.id.logger_menu_all);
        View info = mRootView.findViewById(R.id.logger_menu_info);
        View debug = mRootView.findViewById(R.id.logger_menu_debug);
        View error = mRootView.findViewById(R.id.logger_menu_error);
        all.setOnClickListener(new LevelClick("All"));
        info.setOnClickListener(new LevelClick(LogLevel.Info));
        debug.setOnClickListener(new LevelClick(LogLevel.Debug));
        error.setOnClickListener(new LevelClick(LogLevel.Error));
    }

    private class LevelClick implements View.OnClickListener {
        String Level;

        public LevelClick(String level) {
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
