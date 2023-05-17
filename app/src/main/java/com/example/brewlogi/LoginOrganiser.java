package com.example.brewlogi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginOrganiser extends AppCompatActivity {

    EditText emailget;
    EditText passwordget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailget = findViewById(R.id.login);
        passwordget = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(v -> {
            String email = emailget.getText().toString();
            String password = passwordget.getText().toString();

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Authentication successful
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(LoginOrganiser.this, Organiser.class);
                            startActivity(intent);
                        } else {
                            // Authentication failed
                            Exception exception = task.getException();
                            Toast.makeText(this, "Invalid id or password", Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

}
