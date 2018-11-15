package com.hw.applogger;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hw.txtreaderlib.R;

import java.util.List;

/**
 * created by ： bifan-wei
 */

public class RequestLogAdapter extends BaseAdapter {
    private Context context;
    private List<RequestLogBean> data;

    public RequestLogAdapter(Context context, List<RequestLogBean> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * @return
     */
    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_request_logger, null);
            holder = new Holder();
            holder.Bg = view.findViewById(R.id.request_log_bg);
            holder.tag = (TextView) view.findViewById(R.id.request_log_tag);
            holder.isSuccess = (TextView) view.findViewById(R.id.request_log_isSuccess);
            holder.time = (TextView) view.findViewById(R.id.request_log_Time);
            holder.url = (TextView) view.findViewById(R.id.request_log_url);
            holder.params = (TextView) view.findViewById(R.id.request_log_params);
            holder.response = (TextView) view.findViewById(R.id.request_log_response);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        RequestLogBean bean = data.get(i);
        holder.time.setText(LoggerUtil.getTimeByString(bean.Time, "yyyyMMdd HH:mm:ss", "yy/MM/dd HH:mm:ss"));
        holder.isSuccess.setText(bean.IsSuccess ? "success" : "false");
        holder.tag.setText(bean.Tag + "");
        holder.url.setText(bean.Url + "");
        holder.params.setText(bean.Params + "");
        holder.response.setText(bean.Response);
        if (bean.IsSuccess) {
            holder.isSuccess.setTextColor(Color.BLACK);
            holder.Bg.setBackgroundColor(Color.WHITE);
            holder.response.setBackgroundColor(Color.parseColor("#ededed"));
        } else {
            holder.isSuccess.setTextColor(Color.RED);
            holder.response.setBackgroundColor(Color.parseColor("#00ffffff"));
            holder.Bg.setBackgroundColor(Color.parseColor("#fbd6d9"));
        }

        return view;
    }

    class Holder {
        View Bg;
        TextView tag;
        TextView isSuccess;
        TextView time;
        TextView url;
        TextView params;
        TextView response;
    }
}
