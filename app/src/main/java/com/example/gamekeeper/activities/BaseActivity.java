package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gamekeeper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    protected boolean useToolbar = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // Asegúrate de estar inflando correctamente el layout
        setupToolbar();

        // Inicializar el BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Establecer el listener para las opciones del BottomNavigationView (usando el nuevo método)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.opcion1:
                    Toast.makeText(BaseActivity.this, "Opción 1", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.opcion2:
                    Toast.makeText(BaseActivity.this, "Opción 2", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.opcion3:
                    Toast.makeText(BaseActivity.this, "Opción 3", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        setupToolbar();

        getLayoutInflater().inflate(layoutResID, findViewById(R.id.container), true);
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
}