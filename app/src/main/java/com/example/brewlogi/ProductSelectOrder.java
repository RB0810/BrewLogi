package com.example.brewlogi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProductSelectOrder extends AppCompatActivity {

    private ArrayList<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    private HashMap<String, Integer> product = new HashMap<>();

    private RecyclerView recyclerView;
    private String productname;
    private Integer cost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_select_order);

        recyclerView = findViewById(R.id.productRV);
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory").child("Total Stock");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String productName = dataSnapshot.getKey();
                    DataSnapshot dataSnapshot1 = dataSnapshot.child("Cost");
                    Integer cost = dataSnapshot1.getValue(Integer.class);
                    product.put(productName, cost);
                }

                DatabaseReference database2 = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Offers");

                database2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String type = dataSnapshot.getKey();
                            DataSnapshot dataSnapshot1 = dataSnapshot.child("Costing");
                            Integer costing = dataSnapshot1.getValue(Integer.class);
                            for (Map.Entry<String, Integer> entry : product.entrySet()) {
                                cost = 0;
                                productname = entry.getKey();
                                cost = product.get(productname);
                                cost = cost * costing;
                                System.out.println(productname + type);
                                productList.add(new Product(productname, type, cost, R.drawable.beer_image));
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}