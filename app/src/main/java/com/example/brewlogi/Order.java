package com.example.brewlogi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity {

    private TextView numberTextView;
    private int number = 1;

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);


        productRecyclerView = findViewById(R.id.productRecyclerView);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory").child("Total Stock");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String productName = dataSnapshot.getKey();
                    DataSnapshot dataSnapshot1 = dataSnapshot.child("Cost");
                    Integer cost  = dataSnapshot1.getValue(Integer.class);
                    DataSnapshot dataSnapshot2 = dataSnapshot.child("Cans distributed");
                    String cansDist = dataSnapshot1.getValue(Integer.class).toString();
                    DataSnapshot dataSnapshot3 = dataSnapshot.child("Cans left");
                    String cansLeft = dataSnapshot1.getValue(Integer.class).toString();
                    productList.add(new Product(productName, cansDist, cansLeft, cost, R.drawable.beer_image));
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }




}
