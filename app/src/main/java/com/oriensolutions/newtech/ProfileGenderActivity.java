package com.oriensolutions.newtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileGenderActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    FirebaseAuth firebaseAuth;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gender);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        radioGroup = findViewById(R.id.genderRadioGroup);

        findViewById(R.id.genderAddButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                String gender = null;

                if (selectedRadioButtonId != -1){
                    selectedRadioButton = findViewById(selectedRadioButtonId);
                    gender = selectedRadioButton.getText().toString();

                    String userId = user.getUid();

                    Map<String, Object> profileGender = new HashMap<>();
                    profileGender.put("gender", gender);

                    firestore.collection("User").document(userId)
                            .set(profileGender, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                }else {
                    Snackbar.make(v,"Please select gender..",Snackbar.LENGTH_LONG).show();

                }
            }
        });


        findViewById(R.id.profileGenderBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}