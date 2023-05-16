package com.example.brewlogi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brewlogi.Adapter_Stall;
import com.example.brewlogi.Product;
import com.example.brewlogi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StallPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_Stall adapterStall;
    private ArrayList<Product> productList;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_page);

        Intent intent = getIntent();
        String stallName = intent.getStringExtra("stallName");

        recyclerView = findViewById(R.id.recycler_view_stall);
        ImageButton addProductButton = findViewById(R.id.add_button_product);
        TextView stall = findViewById(R.id.stall_name);
        stall.setText(stallName);

        database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory").child(stallName);

        productList = new ArrayList<>();
        adapterStall = new Adapter_Stall(this, productList, stallName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterStall);

        // Retrieve existing products from the database
        retrieveProducts();

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });
    }

    private void retrieveProducts() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String lastUpdatedTimestamp = String.valueOf(System.currentTimeMillis());

                // Convert the timestamp to your desired format
                // For example, you can use SimpleDateFormat to format the timestamp
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String lastUpdatedDate = sdf.format(new Date(Long.parseLong(lastUpdatedTimestamp)));

                // Set the last updated date and time on the TextView

                TextView lastUpdated = findViewById(R.id.lastupdated);
                lastUpdated.setText("Last Updated: " + lastUpdatedDate);
                productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String productName = dataSnapshot.getKey();

                    DataSnapshot cansDistSnapshot = dataSnapshot.child("Cans distributed");
                    Integer cansDistValue = cansDistSnapshot.getValue(Integer.class);
                    String cd = (cansDistValue != null) ? cansDistValue.toString() : "";
                    String cansDistributed = "Cans distributed: " + cd;

                    DataSnapshot cansLeftSnapshot = dataSnapshot.child("Cans left");
                    Integer cansLeftValue = cansLeftSnapshot.getValue(Integer.class);
                    String cl = (cansLeftValue != null) ? cansLeftValue.toString() : "";
                    String cansLeft = "Cans left: " + cl;

                    Product product = new Product(productName, cansDistributed, cansLeft);
                    productList.add(product);
                }

                adapterStall.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        dialogBuilder.setView(dialogView);

        EditText productNameEditText = dialogView.findViewById(R.id.edit_text_product_name);
        EditText cansDistributedEditText = dialogView.findViewById(R.id.edit_text_cans_distributed);
        EditText cansLeftEditText = dialogView.findViewById(R.id.edit_text_cans_left);

        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productName = productNameEditText.getText().toString().trim();
                String cansDistributedStr = cansDistributedEditText.getText().toString().trim();
                String cansLeftStr = cansLeftEditText.getText().toString().trim();

                if (productName.isEmpty() || cansDistributedStr.isEmpty() || cansLeftStr.isEmpty()) {
                    Toast.makeText(StallPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int cansDistributed = Integer.parseInt(cansDistributedStr);
                    int cansLeft = Integer.parseInt(cansLeftStr);
                    // Save the new product to the database
                    saveProduct(productName, cansDistributed, cansLeft);
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void saveProduct(String productName, int cansDistributed, int cansLeft) {
        // Replace invalid characters in the product name
        String sanitizedProductName = productName.replaceAll("[.#$\\[\\]]", "-");

        // Save the product to the database
        database.child(sanitizedProductName).child("Cans distributed").setValue(cansDistributed);
        database.child(sanitizedProductName).child("Cans left").setValue(cansLeft);

        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
    }

}

