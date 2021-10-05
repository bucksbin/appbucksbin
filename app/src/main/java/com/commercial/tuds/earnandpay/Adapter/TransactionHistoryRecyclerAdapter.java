package com.commercial.tuds.earnandpay.Adapter;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.commercial.tuds.earnandpay.Models.Transaction;
import com.commercial.tuds.earnandpay.R;
import com.commercial.tuds.earnandpay.TransactionResultActivity;

import java.util.List;

public class TransactionHistoryRecyclerAdapter extends RecyclerView.Adapter<TransactionHistoryRecyclerAdapter.MyHolder>{

    List<Transaction> list;
    Context context;

    public TransactionHistoryRecyclerAdapter(List<Transaction> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.transaction_history_card,parent,false);
        MyHolder myHolder = new MyHolder(view);


        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Transaction transaction = list.get(position);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionResultActivity.class);
                intent.putExtra("transaction",transaction);
                context.startActivity(intent);
            }
        });
        holder.TransactionTypeEt.setText(transaction.getTransactionType());
        holder.TransactionAccNoEt.setText(transaction.getAccountNumber());
        holder.TransactionAmountEt.setText("₹ "+transaction.getTransactionAmount());
        holder.TransactionRefEt.setText("Txn No: "+transaction.getTransactionReferenceNumber());
        if(transaction.getIsSuccessful()) {
            if (transaction.getTransactionAction().matches("debited"))
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.debited_icon));
            else if (transaction.getTransactionAction().matches("credited"))
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.credited_icon));
        }
        else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.failed_icon));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView TransactionTypeEt,TransactionAccNoEt,TransactionAmountEt,TransactionRefEt;
        CardView mainLayout;
        ImageView imageView;


        public MyHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.card_view);
            TransactionTypeEt = itemView.findViewById(R.id.transactionType);
            TransactionAccNoEt = itemView.findViewById(R.id.transactionContact);
            TransactionAmountEt = itemView.findViewById(R.id.transactionAmount);
            TransactionRefEt = itemView.findViewById(R.id.transactionRefNo);
            imageView = itemView.findViewById(R.id.action_image);

        }
    }

}