package com.oriensolutions.newtech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oriensolutions.newtech.model.Item;
import com.oriensolutions.newtech.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private static final String TAG = MainActivity.class.getName();
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        TextView email = v.findViewById(R.id.profileEmailView);
        email.setText(user.getEmail());

//        firestore.collection("User").whereEqualTo("uid", user.getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
//
//                                ImageButton imageButton = v.findViewById(R.id.profileImageButton);
//                                User userProfile = snapshot.toObject(User.class);
//                                StorageReference reference = storage.getReference("profile-images/" + userProfile.getProfile_image_id());
//                                reference.getDownloadUrl()
//                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                Picasso.get()
//                                                        .load(uri)
//                                                        .resize(180, 180)
//                                                        .centerCrop()
//                                                        .into(imageButton);
//                                            }
//                                        });
//                                TextView name = v.findViewById(R.id.profileNameView);
//                                TextView gender = v.findViewById(R.id.profileGenderView);
//                                TextView bday = v.findViewById(R.id.profilebdayView);
//
//                                if (userProfile.getFname() != null) {
//
//                                    name.setText(userProfile.getFname() + " " + userProfile.getLname());
//                                }
//
//                                if (userProfile.getGender() != null) {
//
//                                    gender.setText(userProfile.getGender());
//                                }
//
//                                if (userProfile.getBday() != null) {
//
//                                    bday.setText(userProfile.getBday());
//                                }
//
//                            }
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.i(TAG, e.getMessage());
//                    }
//                });


        firestore.collection("User").whereEqualTo("uid", user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange change :value.getDocumentChanges()){

                            ImageButton imageButton = v.findViewById(R.id.profileImageButton);
                            User userProfile = change.getDocument().toObject(User.class);
                            StorageReference reference = storage.getReference("profile-images/" + userProfile.getProfile_image_id());
                            reference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.get()
                                                    .load(uri)
                                                    .resize(180, 180)
                                                    .centerCrop()
                                                    .into(imageButton);
                                        }
                                    });
                            TextView name = v.findViewById(R.id.profileNameView);
                            TextView gender = v.findViewById(R.id.profileGenderView);
                            TextView bday = v.findViewById(R.id.profilebdayView);
                            TextView address = v.findViewById(R.id.profileAddressView);
                            TextView districtProvince = v.findViewById(R.id.profileDistrictProvinceView);

                            if (userProfile.getFname() != null) {

                                name.setText(userProfile.getFname() + " " + userProfile.getLname());
                            }

                            if (userProfile.getGender() != null) {

                                gender.setText(userProfile.getGender());
                            }

                            if (userProfile.getBday() != null) {

                                bday.setText(userProfile.getBday());
                            }

                            if (userProfile.getLine1() != null && userProfile.getLine2() != null) {

                                address.setText(userProfile.getLine1()+", "+userProfile.getLine2());
                            }

                            if (userProfile.getDistrict() != null && userProfile.getProvince() != null) {

                                districtProvince.setText(userProfile.getDistrict()+", "+userProfile.getProvince());
                            }

                        }
                    }
                });


        v.findViewById(R.id.profileImageButton).setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileImageActivity.class);
                startActivity(intent);

            }
        });

        v.findViewById(R.id.signout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), SignInSignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        v.findViewById(R.id.name_arrow_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileNameActivity.class);
                startActivity(intent);
            }
        });

        v.findViewById(R.id.address_arrow_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileAddressActivity.class));
            }
        });

        v.findViewById(R.id.birthday_arrow_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileBirthdayActivity.class));
            }
        });

        v.findViewById(R.id.gender_arrow_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileGenderActivity.class));
            }
        });


        return v;
    }

}