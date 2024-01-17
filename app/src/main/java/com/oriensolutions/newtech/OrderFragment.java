package com.oriensolutions.newtech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.oriensolutions.newtech.adapter.ItemAdapter;
import com.oriensolutions.newtech.adapter.OrderAdapter;
import com.oriensolutions.newtech.model.Item;
import com.oriensolutions.newtech.model.Order;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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

    private final static String TAG = MainActivity.class.getName();
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Order> orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userId = user.getUid().toString();

        orders = new ArrayList<>();

        RecyclerView orderView = v.findViewById(R.id.order_view);

        OrderAdapter orderAdapter = new OrderAdapter(orders, OrderFragment.this.getActivity(), new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                Log.i(TAG, "Okay");


            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        orderView.setLayoutManager(layoutManager);
        orderView.setAdapter(orderAdapter);

        firestore.collection("Orders").whereEqualTo("user", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("MissingInflatedId")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        orders.clear();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Order order = snapshot.toObject(Order.class);
                            firestore.collection("Items").document(order.getItem()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            Order order1 = documentSnapshot.toObject(Order.class);
                                            orders.add(order1);
                                            Log.i(TAG, order1.getImage());

                                            orderAdapter.notifyDataSetChanged();
                                        }
                                    });

                        }


                        orderAdapter.notifyDataSetChanged();
                        Log.i(TAG, String.valueOf(orders));
                    }
                });


        return v;
    }
}