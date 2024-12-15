package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;


import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegisterActivity extends BaseActivity {

    private DatabaseHelper dB;
    private EditText etEmailInput, etPasswordIput;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dB = new DatabaseHelper(this);
        etEmailInput = findViewById(R.id.etEmailInputRegister);
        etPasswordIput = findViewById(R.id.etPasswordInputRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
        FloatingActionButton fabBack = findViewById(R.id.fab_back);
        buttonRegister.setOnClickListener(v -> {
            String email = etEmailInput.getText().toString().trim();
            String password = etPasswordIput.getText().toString().trim();
            // Validar campos
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterActivity.this, "todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }

            // Validar email
            if (!isValidEmail(email)) {
                Toast.makeText(RegisterActivity.this, "Por favor ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar contraseña
            String passwordValidationResult = validatePassword(password);
            if (passwordValidationResult != null) {
                Toast.makeText(RegisterActivity.this, passwordValidationResult, Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si el correo ya está registrado
            if (dB.isEmailAlreadyRegistered(email)) {
                Toast.makeText(RegisterActivity.this, "El correo electrónico ya está registrado.", Toast.LENGTH_SHORT).show();
                return;
            }
            //Encripta la contraseña
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            boolean isAdded = dB.addUser(email, hashedPassword);
            if (isAdded) {
                Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }


        });
        //Boton para volver a la vista anterior
        fabBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //Valida el email
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Metodo para validar la contraseña
    private String validatePassword(String password) {
        // Verificar si la contraseña está vacía
        if (password.isEmpty()) {
            return "La contraseña no puede estar vacía.";
        }
        // Tiene al menos una letra minúscula
        if (!password.matches(".*[a-z].*")) {
            return "La contraseña debe contener al menos una letra minúscula.";
        }

        // Tiene al menos una letra mayúscula
        if (!password.matches(".*[A-Z].*")) {
            return "La contraseña debe contener al menos una letra mayúscula.";
        }

        // Tiene al menos un número
        if (!password.matches(".*[0-9].*")) {
            return "La contraseña debe contener al menos un número.";
        }

        // Tiene al menos un carácter especial
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "La contraseña debe contener al menos un carácter especial.";
        }

        // Tiene al menos 8 caracteres
        if (password.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres.";
        }

        return null;
    }
}