package com.example.brewlogi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResponsibleDrinking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibledrinking);

        CheckBox check1 = findViewById(R.id.checkBox1);
        Button proceed = findViewById(R.id.button_proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check1.isChecked()){
                    Intent intent = new Intent(ResponsibleDrinking.this, PlaceOrder.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ResponsibleDrinking.this, "Please accept the conditions before proceeding", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}