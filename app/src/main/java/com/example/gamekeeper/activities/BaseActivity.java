package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gamekeeper.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No llamamos a setContentView aquí, lo haremos en las actividades que extiendan BaseActivity
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base); // Establece el layout base
        setupToolbar(); // Configura la Toolbar después de establecer el contenido
        // Ahora inflamos el layout específico
        getLayoutInflater().inflate(layoutResID, findViewById(R.id.container), true);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar); // Establecemos la Toolbar como ActionBar
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu); // Inflamos el menú desde el archivo XML
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
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}