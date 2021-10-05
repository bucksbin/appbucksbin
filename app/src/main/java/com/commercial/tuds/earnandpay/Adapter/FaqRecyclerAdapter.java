package com.commercial.tuds.earnandpay.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.commercial.tuds.earnandpay.Models.FAQ;
import com.commercial.tuds.earnandpay.R;

import java.util.List;

public class FaqRecyclerAdapter extends RecyclerView.Adapter<FaqRecyclerAdapter.MyHolder>{

    List<FAQ> list;
    Context context;

    public FaqRecyclerAdapter(List<FAQ> list, Context context) {
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
    public void onBindViewHolder(final MyHolder holder, int position) {
        final FAQ mylist = list.get(position);
        holder.faq_question.setText(mylist.getFaqQuestion());
        holder.faq_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.faq_answer.getText().toString().matches("")){
                    holder.faq_answer.setText(mylist.getFaqAnswer()+"\n\n");
                }
                else
                    holder.faq_answer.setText("");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView faq_question,faq_answer;
        CardView faq_layout;


        public MyHolder(View itemView) {
            super(itemView);
            faq_layout = itemView.findViewById(R.id.card_view);
            faq_question = (TextView) itemView.findViewById(R.id.notificationTitle);
            faq_answer= (TextView) itemView.findViewById(R.id.notificationContent);

        }
    }

}