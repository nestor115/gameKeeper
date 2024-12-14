package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.utils.IntentExtras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends BaseActivity {
    private DatabaseHelper dB;
    private EditText etEmailInputLogin, etPasswordInputLogin;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailInputLogin = findViewById(R.id.et_EmailInputLogin);
        etPasswordInputLogin = findViewById(R.id.et_PasswordInputLogin);
        buttonLogin = findViewById(R.id.btn_Login);
        FloatingActionButton fabBack = findViewById(R.id.fab_back);
        dB = new DatabaseHelper(this);
        // Botón de inicio de sesión
        buttonLogin.setOnClickListener(v -> {

            String email = etEmailInputLogin.getText().toString().trim();
            String password = etPasswordInputLogin.getText().toString().trim();
            //Encripto la contraseña
            String storedHashedPassword = dB.getStoredPasswordByEmail(email);
            if (storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword)) {
                int userId = dB.checkUser(email, storedHashedPassword);
                if (userId != -1) {
                    // Guardar userId en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", userId);
                    editor.apply();

                    // Verifica si el usuario tiene juegos para llevarle a una view o a otra
                    redirectToProperActivity(userId);
                } else {
                    Toast.makeText(LoginActivity.this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
        //Boton para volver a la activity anterior
        fabBack.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //Verifica si el usuario tiene juegos para llevarle a una view o a otra
    private void redirectToProperActivity(int userId) {
        if (dB.hasGamesForUser(userId)) {
            // Redirige a PlayerActivity si hay juegos
            Intent intent = new Intent(LoginActivity.this, PlayerActivity.class);
            intent.putExtra(IntentExtras.USER_ID, userId);
            startActivity(intent);
        } else {
            // Redirige a HomeActivity si no hay juegos
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        finish();
    }
}