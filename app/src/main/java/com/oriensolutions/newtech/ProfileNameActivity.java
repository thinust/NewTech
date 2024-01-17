package com.oriensolutions.newtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.oriensolutions.newtech.model.User;

import java.util.HashMap;
import java.util.Map;

public class ProfileNameActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        EditText editTextfname = findViewById(R.id.input_fname);
        EditText editTextlname = findViewById(R.id.input_lname);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        findViewById(R.id.profileNameBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firestore.collection("User").whereEqualTo("uid", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                User userProfile = snapshot.toObject(User.class);

                                editTextfname.setText(userProfile.getFname());
                                editTextlname.setText(userProfile.getLname());
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        findViewById(R.id.nameAddButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextfname.length() == 0) {
                    Snackbar.make(v, "Please Enter Your First Name", Snackbar.LENGTH_SHORT).show();
                } else {
                    String fname = editTextfname.getText().toString();
                    String lname = editTextlname.getText().toString();
                    String userId = user.getUid();


                    Map<String, Object> profileName = new HashMap<>();
                    profileName.put("fname", fname);
                    profileName.put("lname", lname);

                    firestore.collection("User").document(userId)
                            .set(profileName, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }
}