package com.oriensolutions.newtech.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.oriensolutions.newtech.R;
import com.oriensolutions.newtech.model.Item;
import com.oriensolutions.newtech.model.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<Order> orders;
    private FirebaseStorage storage;
    private Context context;
    private final OrderAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Order order);
    }
    public OrderAdapter(ArrayList<Order> orders, Context context, OrderAdapter.OnItemClickListener listener) {
        this.orders = orders;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_order_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        Order order = orders.get(position);
        holder.textName.setText(order.getName());
        holder.textPrice.setText("Rs. " + String.valueOf(order.getPrice() )+ "0");

        storage.getReference("item-images/" + order.getImage())
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

        holder.bind(orders.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textQty, textPrice;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.order_item_name);
            textPrice = itemView.findViewById(R.id.order_item_price);
            image = itemView.findViewById(R.id.order_item_image);

        }

        public void bind(final Order order, final OrderAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(order);
                }
            });
        }

    }
}
