package com.example.brewlogi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

        loginButton.setEnabled(true);
        loginButton.setBackgroundResource(R.drawable.roundedgreen);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setEnabled(false);
                loginButton.setBackgroundResource(R.drawable.roundedsilver);
                String email = emailget.getText().toString();
                String password = passwordget.getText().toString();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginOrganiser.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                    loginButton.setBackgroundResource(R.drawable.roundedgreen);
                } else {
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
                                    Toast.makeText(LoginOrganiser.this, "Invalid id or password", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Button loginButton = findViewById(R.id.loginbutton);
        loginButton.setEnabled(true);
        loginButton.setBackgroundResource(R.drawable.roundedgreen);
    }
}


