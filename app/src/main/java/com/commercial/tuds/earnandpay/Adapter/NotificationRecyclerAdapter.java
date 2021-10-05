package com.commercial.tuds.earnandpay.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.Models.Notification;
import com.commercial.tuds.earnandpay.R;

import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyHolder>{

    List<Notification> list;
    Context context;

    public NotificationRecyclerAdapter(List<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_card,parent,false);
        MyHolder MyHolder = new MyHolder(view);


        return MyHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Notification mylist = list.get(position);
        holder.notification_title.setText(mylist.getNotificationTitle());
        holder.notification_desc.setText(mylist.getNotificationMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView notification_title,notification_desc;


        public MyHolder(View itemView) {
            super(itemView);
            notification_title = (TextView) itemView.findViewById(R.id.notificationTitle);
            notification_desc= (TextView) itemView.findViewById(R.id.notificationContent);

        }
    }

}