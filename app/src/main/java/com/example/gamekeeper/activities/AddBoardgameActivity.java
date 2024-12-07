package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddBoardgameActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private EditText editTextName, editTextDescription, editTextYear, editTextPlayers, editTextTime;
    private Spinner spinnerGenre1, spinnerGenre2;
    private Button buttonAddGame, buttonUploadImage, buttonTakePhoto;
    private ImageView imageViewBoardgame;
    private DatabaseHelper db;
    private Uri imageUri; // Variable para almacenar la URI de la imagen seleccionada o tomada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boardgame);

        db = new DatabaseHelper(this);

        editTextName = findViewById(R.id.et_name);
        editTextDescription = findViewById(R.id.et_description);
        editTextYear = findViewById(R.id.et_year);
        editTextPlayers = findViewById(R.id.et_players);
        editTextTime = findViewById(R.id.et_time);
        spinnerGenre1 = findViewById(R.id.spinner_genre1);
        spinnerGenre2 = findViewById(R.id.spinner_genre2);
        buttonAddGame = findViewById(R.id.btn_add_game);
        buttonUploadImage = findViewById(R.id.btn_upload_image);
        buttonTakePhoto = findViewById(R.id.btn_take_photo);
        FloatingActionButton fabBack = findViewById(R.id.fab_back);

        //imageViewBoardgame = findViewById(R.id.image_view_boardgame);

        setupGenreSpinners();

        // Verificar y solicitar permiso para la cámara
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        // Configuración de los botones para cargar la imagen o tomar la foto
        buttonUploadImage.setOnClickListener(v -> openGallery());
        buttonTakePhoto.setOnClickListener(v -> openCamera());

        buttonAddGame.setOnClickListener(v -> addBoardGame());
        fabBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddBoardgameActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupGenreSpinners(){
        Cursor cursor = db.getAllGenres();
        List<String> genreList = new ArrayList<>();
        genreList.add("Selecciona un género");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int columnIndexName = cursor.getColumnIndex(DatabaseHelper.COLUMN_GENRE_NAME);
                String genreName = cursor.getString(columnIndexName);
                if (genreName != null) {
                    genreList.add(genreName);
                } else {
                    Log.e("SETUP_GENRE_SPINNERS", "Género nulo encontrado");
                }
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Log.e("SETUP_GENRE_SPINNERS", "El cursor es nulo o no contiene datos");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre1.setAdapter(adapter);
        spinnerGenre2.setAdapter(adapter);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imageViewBoardgame.setImageBitmap(bitmap); // Muestra la imagen en el ImageView
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CAPTURE_IMAGE_REQUEST) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageViewBoardgame.setImageBitmap(bitmap); // Muestra la foto tomada en el ImageView
            }
        }
    }

    private void addBoardGame() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String yearString = editTextYear.getText().toString().trim();
        String numberOfPlayers = editTextPlayers.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();

        String genre1 = spinnerGenre1.getSelectedItem().toString();
        String genre2 = spinnerGenre2.getSelectedItem().toString();

        if (name.isEmpty() || description.isEmpty() || yearString.isEmpty() || numberOfPlayers.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        int yearPublished;
        try {
            yearPublished = Integer.parseInt(yearString);
            if (yearPublished <= 0) {
                throw new NumberFormatException("Año inválido");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El año debe ser un número positivo válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (genre1.equals("Selecciona un género") && genre2.equals("Selecciona un género")) {
            Toast.makeText(this, "Debes seleccionar al menos un género", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí puedes guardar la URI de la imagen si la has tomado o cargado
        long boardgameId = db.addBoardgame(name, imageUri.toString(), description, yearPublished, numberOfPlayers, time);
        if (boardgameId != -1) {
            Toast.makeText(this, "Juego añadido con éxito", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al añadir el juego", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso fue concedido
                Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show();
            } else {
                // El permiso fue denegado, puedes manejarlo aquí si es necesario
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
