package com.example.brewlogi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StallPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_page);

        Intent intent = getIntent();
        String stallName = intent.getStringExtra("stallName");

        RecyclerView recyclerView = findViewById(R.id.recycler_view_stall);
        TextView stallname = findViewById(R.id.stall_name);
        stallname.setText(stallName.toUpperCase());

        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory").child(stallName);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Product> list = new ArrayList<>();
        Adapter_Stall adapterStall = new Adapter_Stall(this, list, stallName);
        recyclerView.setAdapter(adapterStall);

        ArrayList<String> repeat = new ArrayList<>();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String product = dataSnapshot.getKey();

                    DataSnapshot cansDistSnapshot = dataSnapshot.child("Cans distributed");
                    String cansDist = "Cans Distributed: " + cansDistSnapshot.getValue(Integer.class).toString();

                    DataSnapshot cansLeftSnapshot = dataSnapshot.child("Cans left");
                    String cansLeft = "Cans Left: " + cansLeftSnapshot.getValue(Integer.class).toString();

                    Product beer = new Product(product, cansDist, cansLeft);
                    if(!repeat.contains(beer.getProductName())){
                        list.add(beer);
                        repeat.add(beer.getProductName());
                    }
                }
                adapterStall.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}