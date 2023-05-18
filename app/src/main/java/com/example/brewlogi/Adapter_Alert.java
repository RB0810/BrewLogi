package com.example.brewlogi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Alert extends RecyclerView.Adapter<Adapter_Alert.MyViewHolder>{

    Context context;
    ArrayList<Product> list;
    int var = 150;

    public Adapter_Alert(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Alert.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_alert,parent,false);
        return new Adapter_Alert.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product prod1 = list.get(position);
        String stall = prod1.getStallname();
        String prod = prod1.getProductName();
        holder.alert.setText(stall+" "+prod+" is low on stock");
        holder.restockno.setText(String.valueOf(var));

        holder.restock.setOnCheckedChangeListener(null); // Remove previous listener before setting the state

        // Set the toggle state based on the isRestocked field of the Product
        holder.restock.setChecked(prod1.getRestocked());

        holder.restock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    prod1.setRestocked(isChecked);
                    Product product = list.get(position);
                    String stall = product.getStallname();
                    String prod = product.getProductName();
                    DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("Inventory").child(stall).child(prod);
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot dataSnapshot = snapshot.child("Cans left");
                            Integer cans = dataSnapshot.getValue(Integer.class);
                            cans=cans+var;
                            database.child("Cans left").setValue(cans);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference database2 = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("Inventory").child("Total Stock").child(prod);
                    database2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot dataSnapshot = snapshot.child("Cans distributed");
                            Integer cans = dataSnapshot.getValue(Integer.class);
                            cans=cans-var;
                            database2.child("Cans distributed").setValue(cans);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        Switch restock;
        TextView alert;
        TextView restockno;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            alert = itemView.findViewById(R.id.alert);
            restock = itemView.findViewById(R.id.restock);
            restockno = itemView.findViewById(R.id.restocknumber);
        }
    }

}


