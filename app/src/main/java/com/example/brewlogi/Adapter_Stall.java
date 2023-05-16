package com.example.brewlogi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter_Stall extends RecyclerView.Adapter<Adapter_Stall.MyViewHolder>{

    Context context;
    ArrayList<Product> list;
    String stallName;

    public Adapter_Stall(Context context, ArrayList<Product> list, String stallName) {
        this.context = context;
        this.list = list;
        this.stallName = stallName;
    }

    @NonNull
    @Override
    public Adapter_Stall.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_stall,parent,false);
        return new Adapter_Stall.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Stall.MyViewHolder holder, int position) {
        Product product = list.get(position);
        holder.ProductName.setText(product.getProductName());
        holder.CansDistributed.setText(product.getCansDistributed());
        holder.CansLeft.setText(product.getCansLeft());

        holder.Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product beer = list.get(position);
                list.remove(position);
                String beerName = beer.getProductName();
                DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Inventory").child(stallName).child(beerName);
                database.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ProductName;
        TextView CansDistributed;
        TextView CansLeft;
        ImageButton Remove;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductName = itemView.findViewById(R.id.product);
            CansDistributed = itemView.findViewById(R.id.cans_disitributed);
            CansLeft = itemView.findViewById(R.id.cans_left);
            Remove = itemView.findViewById(R.id.remove_product);
        }
    }
}
