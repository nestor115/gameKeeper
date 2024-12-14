package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.gamekeeper.R;

public class MainActivity extends BaseActivity {

    private Button buttonGoRegister, buttonGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGoRegister = findViewById(R.id.buttonGoRegister);
        buttonGoLogin = findViewById(R.id.buttonGoLogin);
        //Boton de registrarse
        buttonGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        //Boton de iniciar sesion
        buttonGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
