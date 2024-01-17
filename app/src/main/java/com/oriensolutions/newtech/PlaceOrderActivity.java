package com.oriensolutions.newtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.oriensolutions.newtech.model.Item;
import com.oriensolutions.newtech.model.User;

import java.sql.Time;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class PlaceOrderActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();
    private static final int PAYHERE_REQUEST = 11001;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    String product_id = null;
    String order_id = null;
    Item item;
    User userdata;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getUid();

        Bundle pid = getIntent().getExtras();


        if (pid != null) {
            product_id = pid.getString("pid");
            Log.i(TAG, product_id.toString());

            DocumentReference itemReference = firestore.collection("Items").document(product_id.toString());
            itemReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        item = document.toObject(Item.class);

                        TextView title = findViewById(R.id.placeOrderTitleView);
                        TextView desc = findViewById(R.id.placeOrderDescView);
                        TextView itemTotal = findViewById(R.id.placeOrderitemPriceView);
                        TextView total = findViewById(R.id.placeOrdertotalView);

                        title.setText(item.getName());
                        desc.setText(item.getDescription());
                        itemTotal.setText(String.valueOf((int) item.getPrice()));
                        total.setText(String.valueOf((int) item.getPrice() + 299));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            });


            DocumentReference userReference = firestore.collection("User").document(user.toString());
            userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        userdata = document.toObject(User.class);

                        TextView name = findViewById(R.id.placeOrderNameView);
                        TextView address = findViewById(R.id.placeOrderAddressView);
                        TextView districtProvince = findViewById(R.id.placeOrderDistrctProvinceView);
                        TextView phone = findViewById(R.id.placeOrderPhoneView);

                        name.setText(userdata.getFname() + " " + userdata.getLname());
                        phone.setText("+94" + userdata.getPhone());
                        address.setText(userdata.getLine1() + ", " + userdata.getLine2());
                        districtProvince.setText(userdata.getDistrict() + ", " + userdata.getProvince());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            });


            findViewById(R.id.orderPlaceBackButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            findViewById(R.id.placeOrderButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    long time = System.currentTimeMillis();
                    order_id = time + userdata.getPhone();

                    InitRequest req = new InitRequest();
                    req.setMerchantId("1225224");       // Merchant ID
                    req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                    req.setAmount(1000.00);             // Final Amount to be charged
                    req.setOrderId(order_id);        // Unique Reference ID
                    req.setItemsDescription(item.getName());  // Item description title
                    req.setCustom1("This is the custom message 1");
                    req.setCustom2("This is the custom message 2");
                    req.getCustomer().setFirstName(userdata.getFname());
                    req.getCustomer().setLastName(userdata.getLname());
                    req.getCustomer().setEmail(userdata.getEmail());
                    req.getCustomer().setPhone(userdata.getPhone());
                    req.getCustomer().getAddress().setAddress(userdata.getLine1() + userdata.getLine2());
                    req.getCustomer().getAddress().setCity(userdata.getDistrict());
                    req.getCustomer().getAddress().setCountry(userdata.getProvince());

                    req.setHoldOnCardEnabled(true);

                    Intent intent = new Intent(PlaceOrderActivity.this, PHMainActivity.class);
                    intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                    PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                    startActivityForResult(intent, PAYHERE_REQUEST); //unique request ID e.g. "11001"

                }


            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null)
                    if (response.isSuccess())
                        msg = "Activity result:" + response.getData().toString();
                    else
                        msg = "Result:" + response.toString();
                else
                    msg = "Result: no response";
                Log.d(TAG, msg);

                Map<String, Object> orderdtls = new HashMap<>();
                orderdtls.put("order_id", order_id);
                orderdtls.put("item", product_id.toString());
                orderdtls.put("total", String.valueOf((int) item.getPrice() + 299));
                orderdtls.put("user", user.toString());
                orderdtls.put("qty", 1);

                firestore.collection("Orders").document(order_id.toString())
                        .set(orderdtls, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Map<String, Object> itemupdate = new HashMap<>();
                                itemupdate.put("qty", item.getQty() - 1);

                                firestore.collection("Items").document(product_id.toString())
                                        .set(itemupdate, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null)
                    Log.d(TAG, response.toString());

                else
                    Log.d(TAG, "User canceled the request");

            }
        }
    }
}