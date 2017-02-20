package com.haikaroselab.loginfo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haikaroselab.loginfo.R;
import com.haikaroselab.loginfo.pojos.LogItem;


import java.util.List;

/**
 * Created by root on 1/5/17.
 */

public class LogsItemAdapter extends RecyclerView.Adapter<LogsItemAdapter.LogsViewHolder> {

    private List<LogItem> itemList;
    private Context context;


    public LogsItemAdapter(Context context, List<LogItem> items){
        this.itemList=items;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(LogsViewHolder holder, int position) {
        LogItem item=itemList.get(position);
        holder.setData(item);
    }

    @Override
    public LogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item,parent,false);
        return  new LogsViewHolder(view);
    }

    public class LogsViewHolder extends RecyclerView.ViewHolder{

        LogItem item;
        private TextView phone;
        private TextView received;
        private TextView picked;
        private TextView tended;


        public LogsViewHolder(View view){
            super(view);
            this.phone=(TextView)view.findViewById(R.id.phone);
          /*  this.received=(TextView)view.findViewById(R.id.received);
            this.picked=(TextView)view.findViewById(R.id.picked);
            this.tended=(TextView)view.findViewById(R.id.ended);*/

        }

        public void setData(LogItem item){
            this.item=item;
            this.phone.setText("  "+item.getPhoneNumber());
          /*  this.received.setText(item.getTimeCalled());
            this.picked.setText(item.getTimeAccepted());
            this.tended.setText(item.getTimeEnded());*/
        }
    }

}
