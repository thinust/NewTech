package com.oriensolutions.newtech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oriensolutions.newtech.model.Item;
import com.oriensolutions.newtech.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SingleProductActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    String product_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);
        Bundle pid = getIntent().getExtras();

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (pid != null) {
            product_id = pid.getString("pid");

            DocumentReference reference = firestore.collection("Items").document(product_id.toString());
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();

                        ImageView imageView = findViewById(R.id.singleProductImageView);
                        Item item = document.toObject(Item.class);
                        StorageReference reference = storage.getReference("item-images/" + item.getImage());
                        reference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get()
                                                .load(uri)
                                                .resize(300, 300)
                                                .centerInside()
                                                .into(imageView);
                                    }
                                });
                        TextView price = findViewById(R.id.single_price_view);
                        TextView qty = findViewById(R.id.single_qty_view);
                        TextView title = findViewById(R.id.single_title_view);
                        TextView condition = findViewById(R.id.single_condition_view);
                        TextView brand = findViewById(R.id.single_brand_view);
                        TextView color = findViewById(R.id.single_color_view);
                        TextView model = findViewById(R.id.single_model_view);
                        TextView desc = findViewById(R.id.single_desc_view);

                        price.setText(String.valueOf((int) item.getPrice()));
                        qty.setText(String.valueOf(item.getQty()));
                        title.setText(item.getName());
                        condition.setText(item.getCondition());
                        brand.setText(item.getBrand());
                        color.setText(item.getColor());
                        model.setText(item.getModel());
                        desc.setText(item.getDescription());

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            });
        }

        findViewById(R.id.singleProductBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        findViewById(R.id.buyNowbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference reference = firestore.collection("User").document(user.getUid());
                reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();

                            User user = document.toObject(User.class);

                            if (user.getFname() == null || user.getLname() == null || user.getPhone() == null || user.getLine1() == null || user.getLine2() == null || user.getProvince() == null || user.getDistrict() == null) {
                                Snackbar.make(v, "Please set profile details..", Snackbar.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(SingleProductActivity.this, PlaceOrderActivity.class);
                                intent.putExtra("pid", product_id);
                                startActivity(intent);
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                });


            }
        });

    }
}