package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dB;

    private EditText etEmailInputLogin, etPasswordInputLogin;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailInputLogin = findViewById(R.id.etEmailInputLogin);
        etPasswordInputLogin = findViewById(R.id.etPasswordInputLogin);
        buttonLogin= findViewById(R.id.buttonLogin);
        dB = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(v-> {

            String email = etEmailInputLogin.getText().toString().trim();
            String password = etPasswordInputLogin.getText().toString().trim();

            if (dB.checkUser(email,password)){
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LoginActivity.this, "Email o contraseña incorrectos",Toast.LENGTH_SHORT).show();
            }
        });

    }
}