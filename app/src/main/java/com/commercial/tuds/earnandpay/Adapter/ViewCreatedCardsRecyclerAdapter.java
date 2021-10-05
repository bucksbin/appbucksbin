package com.commercial.tuds.earnandpay.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.commercial.tuds.earnandpay.Models.DiscountCardPojo;
import com.commercial.tuds.earnandpay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ViewCreatedCardsRecyclerAdapter extends RecyclerView.Adapter<ViewCreatedCardsRecyclerAdapter.RecyclerViewHolder> {

    List<DiscountCardPojo> list;
    String strDestination;
    Context context;
    private ViewCreatedCardsRecyclerAdapter viewCreatedCardsRecyclerAdapter;


    public ViewCreatedCardsRecyclerAdapter(List<DiscountCardPojo> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_created_cards,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final DiscountCardPojo pojo = list.get(position);
        holder.customerNameTV.setText(pojo.getCustomer_name());
        holder.customerNumberTV.setText(pojo.getCustomer_mobile());
//        holder.shopNameTV.setText(pojo.getShop_name());
        holder.discountPercentageTV.setText(pojo.getDiscount_percentage());
        holder.editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.custom_dialog_edit_discount_card,null);
                EditText discountET = dialogView.findViewById(R.id.edit_discount_ET);
                Button saveBTN1 = dialogView.findViewById(R.id.save_BTN);

                builder.setView(dialogView);
                dialog = builder.show();
                saveBTN1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_value = discountET.getText().toString();
                        if (str_value.equals("")){
                            Toast.makeText(context, "Cannot be empty!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("discount_card").child(currentFirebaseUser.getUid()).child(pojo.getDestination_id()).child("discount_percentage").setValue(str_value);
                        //For update notification
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("discount_update").child(currentFirebaseUser.getUid()).child(pojo.getDestination_id());
                        reference.removeValue();
                        FirebaseDatabase.getInstance().getReference("discount_update").child(currentFirebaseUser.getUid()).child(pojo.getDestination_id()).child("discount_percentage").setValue(str_value);
                        //
                        holder.discountPercentageTV.setText(str_value);
                        Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        holder.deleteBTN.setOnClickListener(view -> {

            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
            View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.custom_dialog_confirmation_delete,null);
            Button cancleBTN,deleteBTN;
            cancleBTN = dialogView.findViewById(R.id.cancle_BTN);
            deleteBTN = dialogView.findViewById(R.id.delete_BTN);
            builder.setView(dialogView);
            dialog = builder.show();
            cancleBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            deleteBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("discount_card").child(currentFirebaseUser.getUid()).child(pojo.getDestination_id());
                    reference.removeValue();
                    Toast.makeText(context, "Discount card deleted", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    list.remove(position);
                    dialog.dismiss();
                }
            });


        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTV,customerNumberTV,shopNameTV,shopAddressTV,discountPercentageTV;
        Button editBTN,deleteBTN;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTV = itemView.findViewById(R.id.CustomerName);
            customerNumberTV = itemView.findViewById(R.id.CustomerNumber);
//            shopNameTV = itemView.findViewById(R.id.ShopName);
            discountPercentageTV = itemView.findViewById(R.id.Discount);
            editBTN = itemView.findViewById(R.id.editBTN);
            deleteBTN = itemView.findViewById(R.id.deleteBTN);

        }
    }
}
