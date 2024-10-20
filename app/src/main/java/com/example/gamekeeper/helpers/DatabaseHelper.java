package com.example.gamekeeper.helpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gamekeeper.models.BoardGame;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

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
    public static final String COLUMN_BOARDGAME_PHOTO = "photo";
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
            int userId = cursor.getInt(0); // Suponiendo que la primera columna es el ID
            cursor.close();
            return userId;
        }
        cursor.close();
        return -1; // Indica que el usuario no fue encontrado
    }
    public boolean addGenre(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GENRE_NAME, name);
        long result = db.insert(TABLE_GENRE, null, values);
        db.close();
        return result != -1;
    }
    public boolean addBoardgame(String name, byte[] photo, String description, int yearPublished, String numberOfPlayers, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar si el juego ya existe
        Cursor cursor = db.query(TABLE_BOARDGAME, new String[]{COLUMN_BOARDGAME_NAME}, COLUMN_BOARDGAME_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close(); // Cierra el cursor si el juego ya existe
            return false; // El juego ya existe, no se inserta
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOARDGAME_NAME, name);
        values.put(COLUMN_BOARDGAME_PHOTO, photo);
        values.put(COLUMN_BOARDGAME_DESCRIPTION, description);
        values.put(COLUMN_BOARDGAME_YEAR_PUBLISHED, yearPublished);
        values.put(COLUMN_BOARDGAME_NUMBER_OF_PLAYERS, numberOfPlayers);
        values.put(COLUMN_BOARDGAME_TIME, time);

        long result = db.insert(TABLE_BOARDGAME, null, values);
        cursor.close(); // Asegúrate de cerrar el cursor aquí también
        db.close();
        return result != -1; // Devuelve true si la inserción fue exitosa
    }

    public boolean addBoardgameGenre(int boardgameId, int genreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BG_ID, boardgameId);
        values.put(COLUMN_G_ID, genreId);
        long result = db.insert(TABLE_BOARDGAME_GENRE, null, values);
        db.close();
        return result != -1;
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
        return rowsAffected > 0; // Retorna true si se eliminó al menos una fila
    }


    // Método para obtener todos los juegos de mesa de un usuario
    public List<BoardGame> getUserBoardgames(int userId) {
        List<BoardGame> boardgames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT b.* FROM " + TABLE_BOARDGAME + " b " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " ub ON b.id = ub.boardgame_id " +
                "WHERE ub.user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                try {
                    // Usa getColumnIndexOrThrow para obtener las columnas
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_DESCRIPTION));
                    int yearPublished = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_YEAR_PUBLISHED));
                    String numberOfPlayers = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_NUMBER_OF_PLAYERS));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_TIME));

                    // Crea un objeto Boardgame con los datos obtenidos
                    BoardGame boardgame = new BoardGame(id, name, null, description, yearPublished, numberOfPlayers, time, null);
                    boardgames.add(boardgame);
                } catch (IllegalArgumentException e) {
                    Log.e("DBError", "Una de las columnas no existe: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return boardgames;
    }
    public Cursor searchBoardgamesByName(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        return db.rawQuery(sql, selectionArgs);
    }
    public Cursor getBoardgameDetailsById(int boardgameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOARDGAME, null, COLUMN_ID + "=?", new String[]{String.valueOf(boardgameId)}, null, null, null);
    }
    public Cursor getBoardgameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_ID + " = ?", new String[]{String.valueOf(id)});
    }



}
