package com.commercial.tuds.earnandpay;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.AccountDetails;
import com.commercial.tuds.earnandpay.Models.Address;
import com.commercial.tuds.earnandpay.Models.LoanApplication;
import com.commercial.tuds.earnandpay.Models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanApplicationActivity extends AppCompatActivity {
    TextInputEditText nameET, fatherNameET, motherNameET, flatNumberET, streetET, pincodeET, cityET, districtET, stateET, aadharET, gasConnectionNoET, gasAccountNoET, gasBankNameET, accNoET, accHolderET, ifscCodeET;
    RelativeLayout uploadBtn, applyBtn;
    String[] spinnerItemArray;
    Spinner occupationSpinner;
    RelativeLayout employeeLayout, labourLayout;
    String uploadedSlipUri = null;
    String occupationType;
    ImageView backBtn;
    AlertDialog dialog;
    RelativeLayout parentLayout;
    AVLoadingIndicatorView btnLoader;
    TextView btnText;
    private String transTime;
    private int FILE_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);

        nameET = findViewById(R.id.nameET);
        fatherNameET = findViewById(R.id.fatherNameET);
        motherNameET = findViewById(R.id.motherET);
        flatNumberET = findViewById(R.id.flatNumberET);
        streetET = findViewById(R.id.streetET);
        pincodeET = findViewById(R.id.pincodeET);
        cityET = findViewById(R.id.cityET);
        districtET = findViewById(R.id.districtET);
        stateET = findViewById(R.id.stateET);
        aadharET = findViewById(R.id.aadharET);
        gasConnectionNoET = findViewById(R.id.gasConnectionNumberET);
        gasAccountNoET = findViewById(R.id.gasAccNoET);
        gasBankNameET = findViewById(R.id.gasBankET);
        uploadBtn = findViewById(R.id.uploadBtn);
        applyBtn = findViewById(R.id.applyBtn);
        occupationSpinner = findViewById(R.id.occupationType);
        employeeLayout = findViewById(R.id.employeeLayout);
        backBtn = findViewById(R.id.back_icon);
        labourLayout = findViewById(R.id.labourLayout);
        parentLayout = findViewById(R.id.parent);
        btnLoader = findViewById(R.id.buttonLoader);
        btnText = findViewById(R.id.buttonText);
        accNoET = findViewById(R.id.accountNumberET);
        accHolderET = findViewById(R.id.accountHolderET);
        ifscCodeET = findViewById(R.id.IFSCCodeET);


        spinnerItemArray = new String[]{"Select Occupation Type", "Employee", "Labour", "Businessman"};
        occupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupationType = (String) occupationSpinner.getItemAtPosition(position);
                if (position == 0) {
                    labourLayout.setVisibility(View.GONE);
                    employeeLayout.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.employeeLayout);
                    findViewById(R.id.accountLayout).setLayoutParams(params);
                } else if (position == 1 || position == 2) {
                    employeeLayout.setVisibility(View.GONE);
                    labourLayout.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.labourLayout);
                    findViewById(R.id.accountLayout).setLayoutParams(params);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uploadBtn.setOnClickListener(v -> {
            uploadBtn.setEnabled(false);
            Dexter.withActivity(LoanApplicationActivity.this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = new Intent();
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_REQUEST_CODE);
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        });

        final AlphaAnimation buttonClickAnimation = new AlphaAnimation(1.0f, 0.8f);
        applyBtn.setOnClickListener(v -> {
            v.startAnimation(buttonClickAnimation);
            final String name = nameET.getText().toString().trim();
            final String fatherName = fatherNameET.getText().toString().trim();
            final String motherName = motherNameET.getText().toString().trim();
            final String flatNo = flatNumberET.getText().toString().trim();
            final String street = streetET.getText().toString().trim();
            final String pincode = pincodeET.getText().toString().trim();
            final String city = cityET.getText().toString().trim();
            final String district = districtET.getText().toString().trim();
            final String state = stateET.getText().toString().trim();
            final String aadhar = aadharET.getText().toString().trim();
            final String gasConnectionNo = gasConnectionNoET.getText().toString().trim();
            final String gasAccNo = gasAccountNoET.getText().toString().trim();
            final String gasBankName = gasBankNameET.getText().toString().trim();
            final String accountNo = accNoET.getText().toString().trim();
            final String accountHolderName = accHolderET.getText().toString().trim();
            final String accountIFSC = ifscCodeET.getText().toString().trim();

            if (name.matches(""))
                nameET.setError("Please enter name.");
            if (fatherName.matches(""))
                fatherNameET.setError("Please enter father's name.");
            if (motherName.matches(""))
                motherNameET.setError("Please enter mother's name.");
            if (street.matches(""))
                streetET.setError("Please enter street name.");
            if (pincode.matches(""))
                pincodeET.setError("Please enter pincode.");
            if (city.matches(""))
                cityET.setError("Please enter city.");
            if (district.matches(""))
                districtET.setError("Please enter district");
            if (state.matches(""))
                stateET.setError("Please enter state");
            if (aadhar.matches("")) {
                aadharET.setError("Please enter aadhar number.");
                return;
            }
            int position = occupationSpinner.getSelectedItemPosition();
            if (position == 0 && uploadedSlipUri == null) {
                GenericMethod.showMessage(LoanApplicationActivity.this, "Please upload your salary slip.");
                return;
            } else if(position!=0) {
                if (gasConnectionNo.matches("")) {
                    gasConnectionNoET.setError("Please enter your gas connection number.");
                    return;
                }
                if (gasAccNo.matches("")) {
                    gasAccountNoET.setError("Please enter your gas account number.");
                    return;
                }
                if (gasBankName.matches("")) {
                    gasBankNameET.setError("Please enter your gas bank name.");
                    return;
                }
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationActivity.this);
            builder.setTitle("Apply for Loan");
            builder.setMessage("Clicking on Yes will automatically deduct 20Rs(non-refundable) from your wallet. Are you sure you want to apply for a loan?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getUid()).child("mainBalance");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            float walletBal = dataSnapshot.getValue(float.class);
                            if(walletBal<20){
                                Snackbar.make(parentLayout, "Your wallet balance is less than Rs20. Please recharge before proceeding.", Snackbar.LENGTH_LONG)
                                        .setAction("Recharge", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(LoanApplicationActivity.this,AddMoneyActivity.class));
                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                        .show();
                            }
                            else
                            {
                                databaseReference.setValue(walletBal-20);
                                LoanApplication loanApplication = new LoanApplication(name, fatherName, motherName, new Address(flatNo, street, city, district, state, pincode), aadhar, occupationType, uploadedSlipUri, gasConnectionNo, gasAccNo, gasBankName,new AccountDetails(accountNo,accountHolderName,accountIFSC));
                                FirebaseDatabase.getInstance().getReference().child("loan_application").child(FirebaseAuth.getInstance().getUid()).setValue(loanApplication);
                                GenericMethod.showMessage(LoanApplicationActivity.this, "Your application has been submitted successfully.");
                                final DatabaseReference TransactionRef = FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid());
                                int time = (int)new Date().getTime();
                                String timeCopy = String.valueOf(time).replace("-","");
                                final String transId = FirebaseAuth.getInstance().getUid().substring(0,5)+timeCopy;
                                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                                dateFormatter.setLenient(false);
                                Date today = new Date();
                                transTime = dateFormatter.format(today);
                                final Transaction transaction = new Transaction("Loan Application Fee", 20f, true, "debited", "BucksBin Loan Fee", transId, "", transTime, "Loan Application Fee", walletBal-20);
                                TransactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ArrayList<Transaction> transactionArrayList = new ArrayList<>();
                                        transactionArrayList.clear();
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Transaction transaction1 = snapshot.getValue(Transaction.class);
                                            if (!transaction1.getTransactionReferenceNumber().matches(transId))
                                                transactionArrayList.add(transaction1);
                                        }
                                        transactionArrayList.add(transaction);
                                        TransactionRef.setValue(transactionArrayList);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }

            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(dialog!=null)
                        dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog1) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            });
            dialog.show();
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                finish();
            }

        });
    });

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,MainActivity.class));
            finishAffinity();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED)
        {
            uploadBtn.setEnabled(true);
        }
        if (requestCode == FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            uploadBtn.setEnabled(true);
            Uri uri = data.getData();
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            String filename = FirebaseAuth.getInstance().getUid() + System.currentTimeMillis();
            StorageReference slipRef = FirebaseStorage.getInstance().getReference().child("Salary Slip").child(filename+"."+extension);
            try {
                UploadTask uploadTask = slipRef.putFile(uri);
                btnLoader.show();
                btnLoader.setVisibility(View.VISIBLE);
                btnText.setVisibility(View.INVISIBLE);
                uploadTask.addOnFailureListener(e -> {
                    btnLoader.setVisibility(View.INVISIBLE);
                    btnText.setVisibility(View.VISIBLE);
                    uploadBtn.setEnabled(true);
                    Log.e("FileUploadError",e.getLocalizedMessage());
                }).addOnSuccessListener(taskSnapshot ->
                {
                    btnLoader.setVisibility(View.INVISIBLE);
                    btnText.setVisibility(View.VISIBLE);
                    btnText.setText("File Uploaded Successfully");
                    btnText.setTextColor(getColor(R.color.green));
                    uploadBtn.setEnabled(false);
                    findViewById(R.id.uploadText).setVisibility(View.GONE);
                    uploadBtn.setBackgroundColor(getColor(R.color.white));
                    slipRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        uploadedSlipUri = uri1.toString();
                        Log.e("tag","Upload Successful");
                    }).addOnFailureListener(e -> {
                        uploadedSlipUri = null;
                        GenericMethod.showMessage(LoanApplicationActivity.this, "Sorry the file is not uploaded yet. Please retry.");
                    });
                });
            }
            catch (Exception e)
            {
                GenericMethod.showMessage(this,e.getLocalizedMessage());
            }
        }
    }
}
