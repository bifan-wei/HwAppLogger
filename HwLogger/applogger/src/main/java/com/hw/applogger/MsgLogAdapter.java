package com.hw.applogger;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hw.txtreaderlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ： bifan-wei
 */

public class MsgLogAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<MsgBean> data;

    public MsgLogAdapter(Context context, List<MsgBean> data) {
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
        MsgLogAdapter.Holder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_msg_logger, null);
            holder = new MsgLogAdapter.Holder();
            holder.tag = view.findViewById(R.id.msg_log_tag_text);
            holder.level = view.findViewById(R.id.msg_log_level);
            holder.time = view.findViewById(R.id.msg_log_Time);
            holder.className = view.findViewById(R.id.msg_log_className);
            holder.message = view.findViewById(R.id.msg_log_msg);
            view.setTag(holder);
        } else {
            holder = (MsgLogAdapter.Holder) view.getTag();
        }

        MsgBean bean = data.get(i);
        holder.time.setText(LoggerUtil.getTimeByString(bean.Time, "yyyyMMdd HH:mm:ss", "yy/MM/dd HH:mm:ss"));
        holder.tag.setText(bean.Tag + "");
        holder.className.setText(bean.ClassName + "");
        holder.level.setText(bean.Level + "");
        holder.message.setText(bean.Message + "");
        if (bean.Level.equals(LogLevel.Info)) {
            holder.level.setTextColor(Color.parseColor("#03d251"));
        } else if (bean.Level.equals(LogLevel.Debug)) {
            holder.level.setTextColor(Color.parseColor("#2104d0"));
        } else {
            holder.level.setTextColor(Color.parseColor("#be0118"));
        }

        return view;
    }

    class Holder {
        TextView tag;
        TextView className;
        TextView message;
        TextView time;
        TextView level;
    }

    @Override
    public Filter getFilter() {
        return new MsgLoggerFiler();
    }

    class MsgLoggerFiler extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<MsgBean> data = MsgLogger.getLogs();
            List<MsgBean> results = new ArrayList<>();
            FilterResults filterResults = new FilterResults();
            for (MsgBean bean : data) {
                MsgBean c = checkTag(bean, charSequence);
                if (c != null) {
                    results.add(c);
                } else {
                    MsgBean t = checkClassName(bean, charSequence);
                    if (t != null) {
                        results.add(t);
                    } else {
                        MsgBean n = checkLevel(bean, charSequence);
                        if (n != null) {
                            results.add(n);
                        }
                    }
                }
            }
            filterResults.values = results;
            return filterResults;
        }

        private MsgBean checkClassName(MsgBean bean, CharSequence charSequence) {
            String s = charSequence.toString().toLowerCase();
            if ((bean.ClassName + "").toLowerCase().contains(s)) {
                return bean;
            }
            return null;
        }

        private MsgBean checkTag(MsgBean bean, CharSequence charSequence) {
            String s = charSequence.toString().toLowerCase();
            if ((bean.Tag + "").toLowerCase().contains(s)) {
                return bean;
            }
            return null;
        }

        private MsgBean checkLevel(MsgBean bean, CharSequence charSequence) {
            String s = charSequence.toString().toLowerCase();
            if (       s.equals(LogLevel.Info.toLowerCase())
                    || s.equals(LogLevel.Debug.toLowerCase())
                    || s.equals(LogLevel.Error.toLowerCase())) {
                if(bean.Level.toLowerCase().equals(s)) {
                    return bean;
                }
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data = (List<MsgBean>) filterResults.values;
            notifyDataSetChanged();
        }
    }


}
