package com.oriensolutions.newtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileBirthdayActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_birthday);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String userId = user.getUid();

        findViewById(R.id.bdayAddButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker getbday = (DatePicker) findViewById(R.id.bdayPicker);

                int date = getbday.getDayOfMonth();
                int month = getbday.getMonth();
                int year = getbday.getYear() - 1900;

                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date(year, month, date);
                String bday = dateFormatter.format(d);

                Map<String, Object> profileBday = new HashMap<>();
                profileBday.put("bday", bday);

                firestore.collection("User").document(userId)
                        .set(profileBday, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                finish();
                                Snackbar.make(v, "Successfully save..", Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });

        findViewById(R.id.profileBdayBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}