package com.example.brewlogi;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                double originalCost = Double.parseDouble(cost.replaceAll("[$]", ""));
                double newCost = originalCost * number;
                double roundedCost = Math.round(newCost * 100.0) / 100.0;
                Cost.setText("$" + roundedCost);
                Quantity.setText(String.valueOf(number));

            }
        });

        MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer number = Integer.parseInt(Quantity.getText().toString());
                if (number != 1) {
                    number--;
                    double originalCost = Double.parseDouble(cost.replaceAll("[$]", ""));
                    double newCost = originalCost * number;
                    double roundedCost = Math.round(newCost * 100.0) / 100.0;
                    Cost.setText("$" + roundedCost);
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

                        // Create confirmation dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(Order.this);
                        builder.setTitle("Confirm Order")
                                .setMessage("Are you sure you want to order?\n\n"
                                        + beer + ", " + type + "\n"
                                        + "Quantity: " + Quantity.getText().toString() + "\n"
                                        + "Total Cost: " + Cost.getText().toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Proceed only if user clicks Yes
                                        Intent intent1 = new Intent(Order.this, OrderConfirmation.class);
                                        intent1.putExtra("ProductName", beer);
                                        intent1.putExtra("numberValue", Quantity.getText().toString());
                                        intent1.putExtra("Type", type);
                                        intent1.putExtra("Quantity", quantity);
                                        intent1.putExtra("Total Cost", Cost.getText().toString());
                                        startActivity(intent1);
                                    }
                                })
                                .setNegativeButton("No", null);

                        // Show the AlertDialog
                        AlertDialog confirmationDialog = builder.create();
                        confirmationDialog.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }


}
