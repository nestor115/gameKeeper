package com.example.gamekeeper.helpers;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.gamekeeper.activities.ListElement;
import com.example.gamekeeper.models.Boardgame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "gamekeeper.db";
    private static final int DATABASE_VERSION = 1;
    // Tabla de usuarios
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    // Tabla de géneros
    public static final String TABLE_GENRE = "genre";
    public static final String COLUMN_GENRE_ID = "id";
    public static final String COLUMN_GENRE_NAME = "name";
    // Tabla de juegos de mesa
    public static final String TABLE_BOARDGAME = "boardgame";
    public static final String COLUMN_BOARDGAME_ID = "id";
    public static final String COLUMN_BOARDGAME_NAME = "name";
    public static final String COLUMN_BOARDGAME_PHOTO = "image";
    public static final String COLUMN_BOARDGAME_DESCRIPTION = "description";
    public static final String COLUMN_BOARDGAME_YEAR_PUBLISHED = "year_published";
    public static final String COLUMN_BOARDGAME_NUMBER_OF_PLAYERS = "number_of_players";
    public static final String COLUMN_BOARDGAME_TIME = "time";
    // Tabla relación de juegos de mesa y géneros
    public static final String TABLE_BOARDGAME_GENRE = "boardgame_genre";
    public static final String COLUMN_BG_ID = "boardgame_id";
    public static final String COLUMN_G_ID = "genre_id";
    // Tabla relación  entre usuarios y juegos de mesa
    public static final String TABLE_USER_BOARDGAME = "user_boardgame";
    public static final String COLUMN_UB_USER_ID = "user_id";
    public static final String COLUMN_UB_BOARDGAME_ID = "boardgame_id";


    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT);";


    private static final String TABLE_CREATE_GENRE =
            "CREATE TABLE " + TABLE_GENRE + " (" +
                    COLUMN_GENRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GENRE_NAME + " TEXT NOT NULL UNIQUE);";

    private static final String TABLE_CREATE_BOARDGAME =
            "CREATE TABLE " + TABLE_BOARDGAME + " (" +
                    COLUMN_BOARDGAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BOARDGAME_NAME + " TEXT NOT NULL, " +
                    COLUMN_BOARDGAME_PHOTO + " BLOB, " +
                    COLUMN_BOARDGAME_DESCRIPTION + " TEXT, " +
                    COLUMN_BOARDGAME_YEAR_PUBLISHED + " INTEGER, " +
                    COLUMN_BOARDGAME_NUMBER_OF_PLAYERS + " TEXT, " +
                    COLUMN_BOARDGAME_TIME + " TEXT);";

    private static final String TABLE_CREATE_BOARDGAME_GENRE =
            "CREATE TABLE " + TABLE_BOARDGAME_GENRE + " (" +
                    COLUMN_BG_ID + " INTEGER, " +
                    COLUMN_G_ID + " INTEGER, " +
                    "PRIMARY KEY (" + COLUMN_BG_ID + ", " + COLUMN_G_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_BG_ID + ") REFERENCES " + TABLE_BOARDGAME + "(" + COLUMN_BOARDGAME_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_G_ID + ") REFERENCES " + TABLE_GENRE + "(" + COLUMN_GENRE_ID + "));";

    private static final String TABLE_CREATE_USER_BOARDGAME =
            "CREATE TABLE " + TABLE_USER_BOARDGAME + " (" +
                    COLUMN_UB_USER_ID + " INTEGER, " +
                    COLUMN_UB_BOARDGAME_ID + " INTEGER, " +
                    "PRIMARY KEY (" + COLUMN_UB_USER_ID + ", " + COLUMN_UB_BOARDGAME_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_UB_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_UB_BOARDGAME_ID + ") REFERENCES " + TABLE_BOARDGAME + "(" + COLUMN_BOARDGAME_ID + "));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_GENRE);
        db.execSQL(TABLE_CREATE_BOARDGAME);
        db.execSQL(TABLE_CREATE_BOARDGAME_GENRE);
        db.execSQL(TABLE_CREATE_USER_BOARDGAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARDGAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARDGAME_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BOARDGAME);
        onCreate(db);
    }

    public boolean addUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null,values);
        db.close();
        return result != -1;
    }

    public int checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }
        cursor.close();
        return -1;
    }
    public void addGenre(String genreName) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Comprobar si el género ya existe
        Cursor cursor = db.query("genre", new String[]{"name"}, "name = ?", new String[]{genreName}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close(); // Si el género ya existe, cerramos el cursor
            Log.d("Database", "Género ya existe: " + genreName);
        } else {
            ContentValues values = new ContentValues();
            values.put("name", genreName);
            db.insertWithOnConflict("genre", null, values, SQLiteDatabase.CONFLICT_IGNORE); // Usar CONFLICT_IGNORE
            Log.d("Database", "Género insertado: " + genreName);
        }
    }
    public int getGenreId(String genreName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GENRE, new String[]{COLUMN_GENRE_ID},
                COLUMN_GENRE_NAME + "=?", new String[]{genreName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int genreIndex = cursor.getColumnIndex(COLUMN_GENRE_ID);
            int genreId = cursor.getInt(genreIndex);
            cursor.close();
            return genreId;
        }
        cursor.close();
        return -1; // Si no se encuentra el género

    }
    public long addBoardgame(String name, byte[] photo, String description, int yearPublished, String numberOfPlayers, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_BOARDGAME, new String[]{COLUMN_BOARDGAME_NAME}, COLUMN_BOARDGAME_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return -1; // Retorna -1 si ya existe el juego
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOARDGAME_NAME, name);
        values.put(COLUMN_BOARDGAME_PHOTO, photo);
        values.put(COLUMN_BOARDGAME_DESCRIPTION, description);
        values.put(COLUMN_BOARDGAME_YEAR_PUBLISHED, yearPublished);
        values.put(COLUMN_BOARDGAME_NUMBER_OF_PLAYERS, numberOfPlayers);
        values.put(COLUMN_BOARDGAME_TIME, time);

        long result = db.insert(TABLE_BOARDGAME, null, values);
        cursor.close();
        db.close();
        return result; // Retorna el ID del juego añadido, o -1 si falló
    }

    public void addBoardgameGenre(long gameId, int genreId) {
        if (genreId == -1) {
            Log.e("Database", "No se encontró el género para el juego con ID: " + gameId);
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BG_ID, gameId);
        values.put(COLUMN_G_ID, genreId);
        long result = db.insert(TABLE_BOARDGAME_GENRE, null, values);
        if (result == -1) {
            Log.e("Database", "Error al insertar la relación de género para el juego: " + gameId);
        } else {
            Log.d("Database", "Relación de género insertada correctamente para el juego: " + gameId);
        }
        }
    public Cursor getAllGenres() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GENRE, null);
    }
    public Cursor getAllBoardgames() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME, null);
    }

    public boolean addUserBoardgame(int userId, int boardgameId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UB_USER_ID, userId);
        values.put(COLUMN_UB_BOARDGAME_ID, boardgameId);
        long result = db.insert(TABLE_USER_BOARDGAME, null, values);
        db.close();
        return result != -1;
    }
    public boolean removeUserBoardgame(int userId, int boardgameId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USER_BOARDGAME,
                COLUMN_UB_USER_ID + " = ? AND " + COLUMN_UB_BOARDGAME_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(boardgameId)});
        db.close();
        return rowsAffected > 0;
    }


    public List<ListElement> getUserBoardgames(int userId) {
        List<ListElement> listElements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT b.id, b.name, b.image FROM " + TABLE_BOARDGAME + " b " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " ub ON b.id = ub.boardgame_id " +
                "WHERE ub.user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_NAME));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_PHOTO));
                ListElement listElement = new ListElement(name, id, image);
                listElements.add(listElement);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listElements;
    }
    public Cursor searchBoardgamesByName(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        return db.rawQuery(sql, selectionArgs);
    }
    public Cursor getAllBoardgamesForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BOARDGAME + " bg " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " ub " +
                "ON bg." + COLUMN_BOARDGAME_ID + " = ub." + COLUMN_UB_BOARDGAME_ID +
                " WHERE ub." + COLUMN_UB_USER_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(userId)};
        return db.rawQuery(sql, selectionArgs);
    }
    public Cursor searchBoardgamesForUserByName(int userId, String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BOARDGAME + " bg " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " ub " +
                "ON bg." + COLUMN_BOARDGAME_ID + " = ub." + COLUMN_UB_BOARDGAME_ID +
                " WHERE ub." + COLUMN_UB_USER_ID + " = ? AND bg." + COLUMN_BOARDGAME_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{String.valueOf(userId), "%" + query + "%"};
        return db.rawQuery(sql, selectionArgs);
    }


    public Cursor getBoardgameDetailsById(int boardgameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOARDGAME, null, COLUMN_ID + "=?", new String[]{String.valueOf(boardgameId)}, null, null, null);
    }
    /*public Cursor getBoardgameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_ID + " = ?", new String[]{String.valueOf(id)});
    }*/

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
        return stream.toByteArray();
    }
    public Bitmap getBitmapFromAssets(String filename) {
        AssetManager assetManager = context.getAssets(); // Obtiene el gestor de activos de la aplicación
        InputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            // Abre el archivo en assets
            inputStream = assetManager.open(filename);
            // Decodifica el InputStream en un Bitmap
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace(); // Si ocurre un error, lo imprime
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close(); // Cierra el InputStream
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap; // Retorna el Bitmap cargado desde assets
    }




    public Bitmap getBitmapFromBytes(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Boardgame getBoardgameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_ID + " = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_BOARDGAME_NAME);
            int photoIndex = cursor.getColumnIndex(COLUMN_BOARDGAME_PHOTO);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_BOARDGAME_DESCRIPTION);
            int yearPublishedIndex = cursor.getColumnIndex(COLUMN_BOARDGAME_YEAR_PUBLISHED);
            int numberOfPlayersIndex = cursor.getColumnIndex(COLUMN_BOARDGAME_NUMBER_OF_PLAYERS);
            int timeIndex = cursor.getColumnIndex(COLUMN_BOARDGAME_TIME);

            String name = cursor.getString(nameIndex);
            byte[] photo = cursor.getBlob(photoIndex);
            String description = cursor.getString(descriptionIndex);
            int yearPublished = cursor.getInt(yearPublishedIndex);
            String numberOfPlayers = cursor.getString(numberOfPlayersIndex);
            String time = cursor.getString(timeIndex);



            cursor.close();
            return new Boardgame(id, name, photo, description, yearPublished, numberOfPlayers, time);
        }
        cursor.close();
        return null;
    }


    /*public List<ListElement> searchUserBoardgamesHome(int userId, String query) {
        List<ListElement> filteredGames = new ArrayList<>();
        String queryLower = query.toLowerCase();

        SQLiteDatabase db = this.getReadableDatabase();
        // Realizamos una consulta para filtrar por el nombre del juego y asegurarnos de que está asociado al usuario
        String sql = "SELECT b.id, b.name FROM " + TABLE_BOARDGAME + " b " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " ub ON b.id = ub.boardgame_id " +
                "WHERE ub.user_id = ? AND LOWER(b.name) LIKE ?";

        String[] selectionArgs = new String[] {String.valueOf(userId), "%" + queryLower + "%"};

        Cursor cursor = db.rawQuery(sql, selectionArgs);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_NAME));
                    ListElement game = new ListElement(name, id);
                    filteredGames.add(game);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return filteredGames;
    }
    public Cursor searchBoardgamesByNameHome(String query) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Filtrar los resultados con LIKE en la columna de nombre
        String selection = "name LIKE ?";
        String[] selectionArgs = new String[]{"%" + query.toLowerCase() + "%"};  // Insensible a mayúsculas

        return db.query("boardgames", new String[]{"id", "name"}, selection, selectionArgs, null, null, null);
    }*/





}
