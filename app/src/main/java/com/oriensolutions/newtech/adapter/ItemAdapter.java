package com.oriensolutions.newtech.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.oriensolutions.newtech.R;
import com.oriensolutions.newtech.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<Item> items;
    private FirebaseStorage storage;
    private Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ItemAdapter(ArrayList<Item> items, Context context, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Item item = items.get(position);
        holder.textName.setText(item.getName());
        holder.textQty.setText(Integer.toString(item.getQty()));
        holder.textPrice.setText("Rs. " + String.valueOf(item.getPrice()) + "0");
        holder.textColor.setText(item.getColor());

        storage.getReference("item-images/" + item.getImage())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .resize(170, 170)
                                .centerInside()
                                .into(holder.image);
                    }
                });


        holder.bind(items.get(position), listener);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textQty, textPrice, textColor;

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.item_name);
            textQty = itemView.findViewById(R.id.item_qty);
            textPrice = itemView.findViewById(R.id.item_price);
            image = itemView.findViewById(R.id.item_image);
            textColor = itemView.findViewById(R.id.item_color);

        }

        public void bind(final Item item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
