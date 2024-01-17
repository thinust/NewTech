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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.oriensolutions.newtech.adapter.ItemAdapter;
import com.oriensolutions.newtech.model.Item;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    private ArrayList<Item> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        firestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();

        items = new ArrayList<>();

        RecyclerView itemView = v.findViewById(R.id.itemView);

        ItemAdapter itemAdapter = new ItemAdapter(items, HomeFragment.this.getActivity(), new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(getContext(), SingleProductActivity.class);
                intent.putExtra("pid",item.getId());
                startActivity(intent);
//                Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        itemView.setLayoutManager(layoutManager);
        itemView.setAdapter(itemAdapter);


        firestore.collection("Items").whereGreaterThanOrEqualTo("qty",1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("MissingInflatedId")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        items.clear();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Item item = snapshot.toObject(Item.class);
                            items.add(item);
//                            item.setId(snapshot.getId());

                        }
                        itemAdapter.notifyDataSetChanged();
                    }
                });
        firestore.collection("Items").whereGreaterThanOrEqualTo("qty",1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                items.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Item item = snapshot.toObject(Item.class);
                    items.add(item);
                }
                for (DocumentChange change : value.getDocumentChanges()) {

                    Item item = change.getDocument().toObject(Item.class);
                    switch (change.getType()) {

                        case ADDED:
                            items.add(item);
                            items.remove(item);
                        case MODIFIED:
                            Item old = items.stream().filter(i -> i.getName().equals(item.getName())).findFirst().orElse(null);

                            if (old != null) {
                                old.setQty(Integer.parseInt(Integer.toString(item.getQty())));
                                old.setPrice(item.getPrice());
                                old.setImage(item.getImage());
                                old.setColor(item.getColor());
                            }

                            break;
                        case REMOVED:
//                            items.clear();
                            items.remove(item);

                    }
                }
                itemAdapter.notifyDataSetChanged();
            }
        });


        return v;

    }

}