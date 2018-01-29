package com.dahai.dateselectdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 描述：
 * 时间：2018/1/27/027
 * 作者：xjh
 */
public class DayDateSelectAdapter extends RecyclerView.Adapter<DayDateSelectAdapter.Holder> {

    private Context context;
    private List<DayBean> list;
    private onDateSelectListener listener;

    public DayDateSelectAdapter(Context context, List<DayBean> list) {
        this.context = context;
        this.list = list;
    }

    public interface onDateSelectListener{
        void onSelect(int pos);
    }

    public void setListener(onDateSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dateselect_day, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        DayBean entity = list.get(position);
        if (entity.getDay()==0) {
            holder.tv_day.setText("");
        } else {
            holder.tv_day.setText(entity.getDay()+"");
            holder.tv_jr.setText(entity.getJr());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null) {
                        listener.onSelect(holder.getAdapterPosition());
                    }
                }
            });

            if (entity.isSelect()) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.color_them));
                holder.tv_day.setTextColor(ContextCompat.getColor(context,R.color.text_color_black));
            } else if (entity.isMiddle()){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.color_them_alpha));
                holder.tv_day.setTextColor(ContextCompat.getColor(context,R.color.text_color_black));
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.tv_day.setTextColor(ContextCompat.getColor(context,R.color.color_text_b3));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView tv_day,tv_jr;

        Holder(View itemView) {
            super(itemView);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_jr = (TextView) itemView.findViewById(R.id.tv_jr);
        }
    }
}
