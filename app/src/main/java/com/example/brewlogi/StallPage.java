package com.example.brewlogi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class StallPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_page);

        TextView test = findViewById(R.id.stallnametest);

        Intent intent = getIntent();
        if (intent != null) {
            String stallName = intent.getStringExtra("stallName");
            test.setText(stallName);
        }
    }
}