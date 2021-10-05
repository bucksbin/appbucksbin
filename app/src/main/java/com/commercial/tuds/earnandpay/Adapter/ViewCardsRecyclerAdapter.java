package com.commercial.tuds.earnandpay.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.commercial.tuds.earnandpay.Models.DiscountCardPojo;
import com.commercial.tuds.earnandpay.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ViewCardsRecyclerAdapter extends RecyclerView.Adapter<ViewCardsRecyclerAdapter.RecyclerViewHolder> {

    List<DiscountCardPojo> list;
    Context context;

    public ViewCardsRecyclerAdapter(List<DiscountCardPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_cards,parent,false);
        ViewCardsRecyclerAdapter.RecyclerViewHolder recyclerViewHolder = new ViewCardsRecyclerAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final DiscountCardPojo pojo = list.get(position);
        holder.shopNameTV.setText(pojo.getShop_name());
        holder.shopAddressTV.setText(pojo.getShop_address());
        holder.discountPercentageTV.setText(pojo.getDiscount_percentage());
        String source_id = pojo.getSource_id();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user_details").child(source_id).child("mobile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.shopNumberTV.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView shopNumberTV,shopNameTV,shopAddressTV,discountPercentageTV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            shopAddressTV = itemView.findViewById(R.id.ShopAddress);
            shopNameTV = itemView.findViewById(R.id.ShopName);
            discountPercentageTV = itemView.findViewById(R.id.Discount);
            shopNumberTV = itemView.findViewById(R.id.ShopNumber);

        }
    }
}
