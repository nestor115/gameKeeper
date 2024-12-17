package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamekeeper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

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
            // Oculta el menú inferior en las actividades de inicio de sesión
            if (this instanceof LoginActivity || this instanceof RegisterActivity || this instanceof MainActivity) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);

                bottomNavigationView.setOnItemSelectedListener(new OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.opcion1:
                                if (!(BaseActivity.this instanceof HomeActivity)) {
                                    intent = new Intent(BaseActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                return true;
                            case R.id.opcion2:
                                if (!(BaseActivity.this instanceof SearchActivity)) {
                                    intent = new Intent(BaseActivity.this, SearchActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                return true;
                            case R.id.opcion3:
                                if (!(BaseActivity.this instanceof PlayerActivity)) {
                                    intent = new Intent(BaseActivity.this, PlayerActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                return true;
                            default:
                                return false;
                        }
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
    protected void selectBottomNavigationMenuItem(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }
    }