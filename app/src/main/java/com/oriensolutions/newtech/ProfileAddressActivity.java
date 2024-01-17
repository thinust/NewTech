package com.oriensolutions.newtech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileAddressActivity extends AppCompatActivity {

    ListenerRegistration listenerRegistration;

    FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_address);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        EditText editTextfphone = findViewById(R.id.input_phone);
        EditText editTextline1 = findViewById(R.id.input_line1);
        EditText editTextline2 = findViewById(R.id.input_line2);

        // Province Spinner
        final List<String> provincelist = new ArrayList<String>();
        final AutoCompleteTextView provinceSelecter = (AutoCompleteTextView) findViewById(R.id.select_province);

        provinceSelecter.setText("Select");
        listenerRegistration = firestore.collection("Provinces").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    List<DocumentSnapshot> document = value.getDocuments();
                    document.forEach(d -> {
                        provincelist.add(d.getString("name"));

                    });
                }
            }
        });
        ArrayAdapter<String> adProvince = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, provincelist);
        adProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSelecter.setAdapter(adProvince);
        //Province Spinner


        // District Spinner
        final List<String> districtlist = new ArrayList<String>();
        final AutoCompleteTextView districtSelecter = (AutoCompleteTextView) findViewById(R.id.select_district);

        districtSelecter.setText("Select");
        listenerRegistration = firestore.collection("District").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    List<DocumentSnapshot> document = value.getDocuments();
                    document.forEach(d -> {
                        districtlist.add(d.getString("name"));

                    });
                }
            }
        });
        ArrayAdapter<String> adDistrict = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, districtlist);
        adDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSelecter.setAdapter(adDistrict);
        //District Spinner


        findViewById(R.id.addressAddButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextfphone.length() == 0) {
                    Snackbar.make(v, "Please Enter Phone number..", Snackbar.LENGTH_SHORT).show();
                } else if (editTextline1.length() == 0) {
                    Snackbar.make(v, "Please Enter Address Line 1..", Snackbar.LENGTH_SHORT).show();
                } else if (editTextline2.length() == 0) {
                    Snackbar.make(v, "Please Enter Address Line 2..", Snackbar.LENGTH_SHORT).show();
                } else if (provinceSelecter.getText().toString().equals("Select")) {
                    Snackbar.make(v, "Please Select Province..", Snackbar.LENGTH_SHORT).show();
                } else if (districtSelecter.getText().toString().equals("Select")) {
                    Snackbar.make(v, "Please Select District..", Snackbar.LENGTH_SHORT).show();
                } else {

                    String phone = editTextfphone.getText().toString();
                    String line1 = editTextline1.getText().toString();
                    String line2 = editTextline2.getText().toString();
                    String province = provinceSelecter.getText().toString();
                    String district = districtSelecter.getText().toString();

                    String userId = user.getUid();


                    Map<String, Object> profileAddress = new HashMap<>();
                    profileAddress.put("line1", line1);
                    profileAddress.put("line2", line2);
                    profileAddress.put("phone", phone);
                    profileAddress.put("province", province);
                    profileAddress.put("district", district);

                    firestore.collection("User").document(userId)
                            .set(profileAddress, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Snackbar.make(v, "Successfully save..", Snackbar.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });


        findViewById(R.id.profileContactBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}