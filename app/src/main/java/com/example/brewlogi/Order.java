package com.example.brewlogi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    ImageView image;
    TextView beerName;
    TextView OfferType;
    TextView Cost;
    ImageButton PlusButton;
    ImageButton MinusButton;
    TextView Quantity;
    Button PlaceOrder;
    Integer quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        Intent intent = getIntent();
        String beer = intent.getStringExtra("productName");
        String type = intent.getStringExtra("OfferType");
        String cost = intent.getStringExtra("Cost");
        Integer image_src = intent.getIntExtra("Image", R.drawable.beer_image);

        image = findViewById(R.id.productImage);
        beerName = findViewById(R.id.productNameTextView);
        OfferType = findViewById(R.id.type);
        Cost = findViewById(R.id.costTextView);
        PlusButton = findViewById(R.id.plusButton);
        MinusButton = findViewById(R.id.minusButton);
        Quantity = findViewById(R.id.numberTextView);
        PlaceOrder = findViewById(R.id.placeOrderButton);

        image.setImageResource(image_src);
        beerName.setText(beer);
        OfferType.setText(type);
        Cost.setText(cost);


        PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer number = Integer.parseInt(Quantity.getText().toString());
                number++;
                Quantity.setText(String.valueOf(number));

            }
        });

        MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer number = Integer.parseInt(Quantity.getText().toString());
                if(number!=1){
                    number--;
                    Quantity.setText(String.valueOf(number));
                }
            }
        });

        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Offers").child(type);
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot dataSnapshot = snapshot.child("Number");
                        Integer bottles = dataSnapshot.getValue(Integer.class);
                        quantity = Integer.parseInt(Quantity.getText().toString()) * bottles;

                        // Move the intent creation and start inside onDataChange
                        Intent intent1 = new Intent(Order.this, OrderConfirmation.class);
                        intent1.putExtra("ProductName", beer);
                        intent1.putExtra("numberValue", Quantity.getText().toString());
                        intent1.putExtra("Type", type);
                        intent1.putExtra("Quantity", quantity);
                        startActivity(intent1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }


}
