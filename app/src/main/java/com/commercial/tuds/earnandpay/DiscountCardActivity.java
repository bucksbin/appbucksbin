package com.commercial.tuds.earnandpay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.DiscountCardPojo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;

import static android.content.ContentValues.TAG;

public class DiscountCardActivity extends AppCompatActivity {

    int number_of_cards = 0;
    TextInputEditText shopName, shopAddress, customerName, mobileNumber, discount;
    TextView shopNameTV, shopAddressTV, customerNameTV, mobileNumberTV, discountTV;
    String strShopName, strShopAddress, strCustomerName, strMobileNumber, strDiscount, strSource, strDestination;
    ImageView backIcon;
    String mobile;
    RelativeLayout createDiscountBtn;
    FirebaseDatabase db;
    AVLoadingIndicatorView loadingView;
    Boolean isSucessFull = false;
    Boolean status = false;
    private final int PICK_CONTACT = 102;
    Dialog dialog;
    DatabaseReference reference;
    private DataSnapshot numberOfCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_card);
        loadingView = findViewById(R.id.loaderAnimation);
        shopName = (TextInputEditText) findViewById(R.id.shopNameET);
        shopAddress = (TextInputEditText) findViewById(R.id.shopAddressET);
        customerName = (TextInputEditText) findViewById(R.id.customerNameET);
        mobileNumber = (TextInputEditText) findViewById(R.id.mobileNumberET);
        discount = (TextInputEditText) findViewById(R.id.discountET);

        shopNameTV = (TextView) findViewById(R.id.tvShopName);
        shopAddressTV = (TextView) findViewById(R.id.tvShopAddress);
        customerNameTV = (TextView) findViewById(R.id.tvCustomerName);
        mobileNumberTV = (TextView) findViewById(R.id.tvCustomerPhone);
        discountTV = (TextView) findViewById(R.id.tvDiscount);

        backIcon = (ImageView) findViewById(R.id.back_icon);
        createDiscountBtn = findViewById(R.id.createDiscountBtn);
        dialog = new Dialog(DiscountCardActivity.this);
        dialog.setContentView(R.layout.dialog_discount_card);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mobileNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mobileNumber.getRight() - mobileNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        GenericMethod.showMessage(DiscountCardActivity.this, "Select");
                        Dexter.withActivity(DiscountCardActivity.this)
                                .withPermission(Manifest.permission.READ_CONTACTS)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                        startActivityForResult(intent, PICK_CONTACT);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                })
                                .onSameThread()
                                .check();
                        return true;
                    }
                }
                return false;
            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("user_details").child(currentFirebaseUser.getUid()).child("numberOfCards");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberOfCards = dataSnapshot.<Integer>getValue(Integer.class);
                if (numberOfCards == 0){
                    status = true;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        createDiscountBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (status){
                    Toast.makeText(DiscountCardActivity.this, "Please Buy Discount Card", Toast.LENGTH_LONG).show();
                    Intent intent;
                    intent = new Intent(DiscountCardActivity.this, BuyDiscountCardsActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    return;
                }
                else if (TextUtils.isEmpty(shopName.getText())) {
                    shopName.setError(" Your shop name is required");

                }else if (TextUtils.isEmpty(shopAddress.getText())){
                    shopAddress.setError(" Your shop address is required");

                }else if (TextUtils.isEmpty(customerName.getText())){
                    customerName.setError(" Customer name is required");

                }else if (TextUtils.isEmpty(mobileNumber.getText()) || mobileNumber.getText().length() < 10){
                    mobileNumber.setError("Enter Valid Number");

                }else if (TextUtils.isEmpty(discount.getText())){
                    discount.setError("Discount % is required");

                }
                else {
                    createDiscountBtn.setClickable(false);
                    loadingView.show();
                    strShopName = shopName.getText().toString();
                    strShopAddress = shopAddress.getText().toString();
                    strCustomerName = customerName.getText().toString();
                    strMobileNumber = mobileNumber.getText().toString();
                    strDiscount = discount.getText().toString();


                    strSource = currentFirebaseUser.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user_details");
                    reference.orderByChild("mobile").equalTo(strMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            boolean status = false;
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                if (childSnapshot.getKey() != null) {
                                    strDestination = childSnapshot.getKey();
                                    number_of_cards = childSnapshot.child("numberOfCards").getValue(Integer.class);
                                    status = true;
                                }
                            }

                            if (status) {
                                Create();

                            } else {
                                //invalid user
                                createDiscountBtn.setClickable(true);
                                loadingView.hide();
                                Toast.makeText(DiscountCardActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                intent.putExtra(Intent.EXTRA_TEXT, "Enjoy BucksBin the new way of payment\nhttps://play.google.com/store/apps/details?id=com.commercial.tuds.earnandpay");
                                startActivity(Intent.createChooser(intent, "Share link!"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                }

            }

            private void Create() {

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("discount_card").child(currentFirebaseUser.getUid());
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(strDestination).getValue() != null) {  //Check discount card already created or not
                            loadingView.hide();
                            Intent intent;
                            intent = new Intent(DiscountCardActivity.this, ViewCreatedCardsActivity.class);
                            startActivity(intent);
                            finishAffinity();
                            Toast.makeText(DiscountCardActivity.this, "DiscountCard Already Available For This User", Toast.LENGTH_LONG).show();

                        } else {

                            loadingView.hide();
                            db = FirebaseDatabase.getInstance();
                            reference = db.getReference("discount_card");
                            DiscountCardPojo pojo = new DiscountCardPojo(strCustomerName, strMobileNumber, strDestination, strDiscount, strShopAddress, strShopName, strSource);
                            reference.child(currentFirebaseUser.getUid()).child(strDestination).setValue(pojo);
                            reference = FirebaseDatabase.getInstance().getReference().child("user_details").child(currentFirebaseUser.getUid()).child("numberOfCards");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue(Integer.class) != -1) {
                                        if (dataSnapshot.getValue(Integer.class) > 0) {          //Check cards available or not
                                            number_of_cards = dataSnapshot.getValue(Integer.class);
                                            if (number_of_cards > 0) {
                                                number_of_cards--;
                                                dataSnapshot.getRef().setValue(number_of_cards);
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//                            FirebaseDatabase.getInstance().getReference("discount_update").child(strSource).child(strDestination).child("discount_percentage").setValue(strDiscount);
                            Toast.makeText(DiscountCardActivity.this, "Discount card created", Toast.LENGTH_SHORT).show();
                            TextView ShopAdd, ShopName, CustomerName, CustomerMobile, Discount;
                            Button ShowCards;
                            ShopAdd = dialog.findViewById(R.id.tvShopAddress);
                            ShopName = dialog.findViewById(R.id.tvShopName);
                            CustomerName = dialog.findViewById(R.id.tvCustomerName);
                            CustomerMobile = dialog.findViewById(R.id.tvCustomerPhone);
                            Discount = dialog.findViewById(R.id.tvDiscount);
                            ShowCards = dialog.findViewById(R.id.SHowCardsBTN);
                            ShopAdd.setText(strShopAddress);
                            ShopName.setText(strShopName);
                            CustomerName.setText(strCustomerName);
                            CustomerMobile.setText(strMobileNumber);
                            Discount.setText(strDiscount);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.show();
                            Intent intent = new Intent(DiscountCardActivity.this, ViewCreatedCardsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            showNotification(getApplicationContext(), "Discount Card", "You created discount card for +91"+strMobileNumber+" of "+strDiscount+"%.", intent);
                            ShowCards.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(DiscountCardActivity.this, ViewCreatedCardsActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
        });


        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            if (cNumber.matches(""))
                                GenericMethod.showMessage(DiscountCardActivity.this, "Unable to fetch number.Prefer typing it manually");
                            cNumber = cNumber.replace(" ", "").replace("-", "");
                            if (cNumber.length() != 10)
                                try {
                                    cNumber = cNumber.substring(3, 13);
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            mobileNumber.setText(cNumber);
                        }
                    }
                }
                break;
        }
    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}