package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gamekeeper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    protected boolean useToolbar = true;
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // Inflar la vista hija después de la base
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.container), true);
        setupBottomNavigation(); // Configurar el BottomNavigationView aquí

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base); // Esto carga el layout base
        getLayoutInflater().inflate(layoutResID, findViewById(R.id.container), true); // Aquí se infla el layout de la actividad hija

        // Asegúrate de que el BottomNavigationView se configure después de inflar
        setupBottomNavigation();
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcion1:
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            case R.id.opcion2:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.opcion3:
                startActivity(new Intent(this, AddBoardgameActivity.class));
                return true;
            case R.id.opcion4:
                startActivity(new Intent(this, PlayerActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE); // Hacer visible el menú

            // Usa post() para asegurarte de que el diseño se ha inflado completamente antes de agregar listeners
            bottomNavigationView.post(() -> {
                Log.d("MenuInferior", "Tamaño final: ancho=" + bottomNavigationView.getWidth() + ", alto=" + bottomNavigationView.getHeight());
            });

            bottomNavigationView.setOnItemSelectedListener(item -> {
                Log.d("MenuInferior", "Evento disparado con ID: " + item.getItemId());

                switch (item.getItemId()) {
                    case R.id.opcion1:
                        startActivity(new Intent(this, HomeActivity.class));
                        return true;
                    case R.id.opcion2:
                        startActivity(new Intent(this, SearchActivity.class));
                        return true;
                    case R.id.opcion3:
                        startActivity(new Intent(this, PlayerActivity.class));
                        return true;
                    default:
                        return false;
                }
            });
        } else {
            Log.e("MenuInferior", "El BottomNavigationView es null");
        }
    }



    // Método para ocultar el BottomNavigationView
    /*protected void hideBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    // Método para mostrar el BottomNavigationView
    protected void showBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }*/
}