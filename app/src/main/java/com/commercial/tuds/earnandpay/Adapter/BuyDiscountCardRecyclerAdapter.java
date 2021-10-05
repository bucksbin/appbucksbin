package com.commercial.tuds.earnandpay.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.commercial.tuds.earnandpay.BuyDiscountCardsActivity;
import com.commercial.tuds.earnandpay.Models.BuyDiscountCardPojo;
import com.commercial.tuds.earnandpay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class BuyDiscountCardRecyclerAdapter extends RecyclerView.Adapter<BuyDiscountCardRecyclerAdapter.RecyclerViewHolder> {
    List<BuyDiscountCardPojo> BDCList;
    private Context context;
    AVLoadingIndicatorView loadingView;


    public BuyDiscountCardRecyclerAdapter(List<BuyDiscountCardPojo> BDCList, Context context) {
        this.BDCList = BDCList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_discount_card_recycler, parent, false);
        BuyDiscountCardRecyclerAdapter.RecyclerViewHolder recyclerViewHolder = new BuyDiscountCardRecyclerAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BuyDiscountCardRecyclerAdapter.RecyclerViewHolder holder, int position) {
        final BuyDiscountCardPojo pojo = BDCList.get(position);
        holder.price.setText(String.valueOf(pojo.getPrice()));
        if (pojo.getNumber_of_cards() == -1)
            holder.numberOfCards.setText("Unlimited");
        else
            holder.numberOfCards.setText(String.valueOf(pojo.getNumber_of_cards()));

        holder.DiscountCardCV.setOnClickListener(view -> {

            String myUid = FirebaseAuth.getInstance().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user_details").child(myUid).child("numberOfCards");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int numCards = dataSnapshot.getValue(Integer.class);
                    if (numCards != -1) {
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        View dialogview = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.custom_confirmation_dialog, null);
                        TextView priceTV, numberOfCardsTV;
                        Button cancleBTN, payBTN;
                        priceTV = dialogview.findViewById(R.id.priceTV);
                        numberOfCardsTV = dialogview.findViewById(R.id.NumberOfCardsTV);
                        cancleBTN = dialogview.findViewById(R.id.cancleBTN);
                        payBTN = dialogview.findViewById(R.id.payBTN);
                        builder.setView(dialogview);
                        builder.setCancelable(true);
                        dialog = builder.show();
                        priceTV.setText("Rs " + String.valueOf(pojo.getPrice()) + " pay from wallet");

                        if (pojo.getNumber_of_cards() == -1)
                            numberOfCardsTV.setText("Unlimited Cards");
                        else
                            numberOfCardsTV.setText(String.valueOf(pojo.getNumber_of_cards()) + " Cards");

                        payBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(myUid).child("mainBalance");
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Boolean status = false;
                                        if (dataSnapshot.getValue(float.class) >= pojo.getPrice()) {
                                            float wallet = dataSnapshot.getValue(float.class) - pojo.getPrice();
                                            dataSnapshot.getRef().setValue(wallet);
                                            status = true;

                                        } else {
                                            Toast.makeText(context, "Insufficient ammount please add money !", Toast.LENGTH_SHORT).show();
                                        }
                                        if (status) {
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user_details").child(myUid).child("numberOfCards");
                                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if (pojo.getNumber_of_cards() == -1) {
                                                        dataSnapshot.getRef().setValue(-1);
                                                    } else {
                                                        int number_of_cards = dataSnapshot.getValue(Integer.class);
                                                        number_of_cards += pojo.getNumber_of_cards();
                                                        dataSnapshot.getRef().setValue(number_of_cards);
                                                        dialog.dismiss();
                                                        Toast.makeText(context, pojo.getNumber_of_cards() + " cards are added", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        });

                        cancleBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    } else {
                        Toast.makeText(context, "You already have infinite cards !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });//----
            int numCards = 0;


        });
    }

    @Override
    public int getItemCount() {
        return BDCList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView price, numberOfCards;
        CardView DiscountCardCV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.PriceTV);
            numberOfCards = (TextView) itemView.findViewById(R.id.NumberOfCardsTV);
            DiscountCardCV = (CardView) itemView.findViewById(R.id.DiscountCardCV);
        }
    }
}
