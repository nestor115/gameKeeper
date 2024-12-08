package com.example.gamekeeper.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.sampledata.CloudinaryConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddBoardgameActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;

    private EditText editTextName, editTextDescription, editTextYear, editTextPlayers, editTextTime;
    private Spinner spinnerGenre1, spinnerGenre2;
    private Button buttonAddGame, buttonUploadImage, buttonTakePhoto;
    private ImageView imageViewBoardgame;
    private DatabaseHelper db;
    private Uri imageUri;

    // ActivityResultLaunchers para galería y cámara
    private ActivityResultLauncher<Intent> galleryResultLauncher;
    private ActivityResultLauncher<Uri> cameraResultLauncher;

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

        imageViewBoardgame = findViewById(R.id.iv_selected_image);

        setupGenreSpinners();

        // Verificar y solicitar permisos
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }


        // Verificar si los permisos de almacenamiento son necesarios
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        }


        // Registrar el launcher para la galería
        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imageViewBoardgame.setImageBitmap(bitmap); // Muestra la imagen en el ImageView
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imageViewBoardgame.setImageBitmap(bitmap); // Muestra la foto tomada en el ImageView
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(AddBoardgameActivity.this, "Error al mostrar la foto", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddBoardgameActivity.this, "Error al tomar la foto", Toast.LENGTH_SHORT).show();
                    }
                });

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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, puedes continuar con la cámara
                    openCamera();
                } else {
                    // Permiso denegado, muestra un mensaje o deshabilita la funcionalidad
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, puedes acceder al almacenamiento
                    openGallery();
                } else {
                    // Permiso denegado, muestra un mensaje o deshabilita la funcionalidad
                    Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
        galleryResultLauncher.launch(intent);  // Llamamos al launcher en lugar de startActivityForResult
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Crea una URI para almacenar la imagen tomada
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Nueva Foto");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Foto tomada desde la cámara");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            cameraResultLauncher.launch(imageUri);  // Llamamos al launcher para tomar la foto
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
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

        if (imageUri == null) {
            Toast.makeText(this, "Debes cargar o tomar una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Subir la imagen a Cloudinary
        new Thread(() -> {
            try {
                Cloudinary cloudinary = CloudinaryConfig.getInstance();
                Map uploadResult = cloudinary.uploader().upload(getRealPathFromURI(imageUri), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("secure_url").toString();

                // Guardar en la base de datos
                long boardgameId = db.addBoardgame(name, imageUrl, description, yearPublished, numberOfPlayers, time);
                if (boardgameId != -1) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Juego añadido con éxito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, SearchActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error al añadir el juego", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }
}
