package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dB;
    private EditText etEmailInput,etPasswordIput;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dB = new DatabaseHelper(this);
        etEmailInput =findViewById(R.id.etEmailInputRegister);
        etPasswordIput =findViewById(R.id.etPasswordInputRegister);
        buttonRegister =findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> {


                String email = etEmailInput.getText().toString().trim();
                String password = etPasswordIput.getText().toString().trim();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this,"todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else{
                    boolean isAdded = dB.addUser(email,password);
                    if(isAdded){
                        Toast.makeText(RegisterActivity.this, "Registro exitoso",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Error al registrar",Toast.LENGTH_SHORT).show();
                    }

                }

        });
    }
}