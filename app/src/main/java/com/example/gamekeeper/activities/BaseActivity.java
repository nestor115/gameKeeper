package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamekeeper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.container), true);
        setupBottomNavigation();

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        getLayoutInflater().inflate(layoutResID, findViewById(R.id.container), true);

        setupBottomNavigation();
    }


    //Método para gestionar la navegacion del menu inferior
    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            //Oculta el menu inferior en las actividades de inicio de sesión
            if (this instanceof LoginActivity || this instanceof RegisterActivity || this instanceof MainActivity) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.setOnItemSelectedListener(item -> {
                    Log.d("MenuInferior", "Evento disparado con ID: " + item.getItemId());

                    switch (item.getItemId()) {
                        case R.id.opcion1:
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                            return true;
                        case R.id.opcion2:
                            startActivity(new Intent(this, SearchActivity.class));
                            finish();
                            return true;
                        case R.id.opcion3:
                            startActivity(new Intent(this, PlayerActivity.class));
                            finish();
                            return true;
                        default:
                            return false;
                    }
                });
            }

            bottomNavigationView.post(() -> {
                Log.d("MenuInferior", "Tamaño final: ancho=" + bottomNavigationView.getWidth() + ", alto=" + bottomNavigationView.getHeight());
            });
        } else {
            Log.e("MenuInferior", "El BottomNavigationView es null");
        }
    }
}