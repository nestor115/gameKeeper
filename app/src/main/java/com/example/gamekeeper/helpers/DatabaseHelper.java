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

import com.example.gamekeeper.models.ListElement;

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
    //Tabla jugadores
    public static final String TABLE_PLAYERS = "players";
    public static final String COLUMN_PLAYER_ID = "id";
    public static final String COLUMN_PLAYER_USER_ID = "user_id";
    public static final String COLUMN_PLAYER_NAME = "player_name";
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
    public static final String COLUMN_BOARDGAME_GENRE_BOARDGAME_ID = "boardgame_id";
    public static final String COLUMN_BOARDGAME_GENRE_GENRE_ID = "genre_id";
    // Tabla relación  entre usuarios y juegos de mesa
    public static final String TABLE_USER_BOARDGAME = "user_boardgame";
    public static final String COLUMN_UB_USER_ID = "user_id";
    public static final String COLUMN_UB_BOARDGAME_ID = "boardgame_id";


    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT);";

    private static final String TABLE_CREATE_PLAYERS =
            "CREATE TABLE " + TABLE_PLAYERS + " (" +
                    COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PLAYER_USER_ID + " INTEGER, " +
                    COLUMN_PLAYER_NAME + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_PLAYER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "));";


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
                    COLUMN_BOARDGAME_GENRE_BOARDGAME_ID + " INTEGER, " +
                    COLUMN_BOARDGAME_GENRE_GENRE_ID + " INTEGER, " +
                    "PRIMARY KEY (" + COLUMN_BOARDGAME_GENRE_BOARDGAME_ID + ", " + COLUMN_BOARDGAME_GENRE_GENRE_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_BOARDGAME_GENRE_BOARDGAME_ID + ") REFERENCES " + TABLE_BOARDGAME + "(" + COLUMN_BOARDGAME_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_BOARDGAME_GENRE_GENRE_ID + ") REFERENCES " + TABLE_GENRE + "(" + COLUMN_GENRE_ID + "));";

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
        db.execSQL(TABLE_CREATE_PLAYERS);
        db.execSQL(TABLE_CREATE_GENRE);
        db.execSQL(TABLE_CREATE_BOARDGAME);
        db.execSQL(TABLE_CREATE_BOARDGAME_GENRE);
        db.execSQL(TABLE_CREATE_USER_BOARDGAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
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
    public String getStoredPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String password = cursor.getString(0);
            cursor.close();
            return password;
        }
        cursor.close();
        return null;
    }
    public void addGenre(String genreName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("genre", new String[]{"name"}, "name = ?", new String[]{genreName}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            Log.d("Database", "Género ya existe: " + genreName);
        } else {
            ContentValues values = new ContentValues();
            values.put("name", genreName);
            db.insertWithOnConflict("genre", null, values, SQLiteDatabase.CONFLICT_IGNORE);
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
        return -1;

    }
    public long addBoardgame(String name, byte[] photo, String description, int yearPublished, String numberOfPlayers, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_BOARDGAME, new String[]{COLUMN_BOARDGAME_NAME}, COLUMN_BOARDGAME_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return -1;
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
        return result;
    }

    public void addBoardgameGenre(long gameId, int genreId) {
        if (genreId == -1) {
            Log.e("Database", "No se encontró el género para el juego con ID: " + gameId);
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOARDGAME_GENRE_BOARDGAME_ID, gameId);
        values.put(COLUMN_BOARDGAME_GENRE_GENRE_ID, genreId);
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

        String query = "SELECT boardgame.id, boardgame.name, boardgame.image FROM " + TABLE_BOARDGAME + " boardgame " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " user_boardgame ON boardgame.id = user_boardgame.boardgame_id " +
                "WHERE user_boardgame.user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});


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
        String sql = "SELECT * FROM " + TABLE_BOARDGAME + " boardgame " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " user_boardgame " +
                "ON boardgame." + COLUMN_BOARDGAME_ID + " = user_boardgame." + COLUMN_UB_BOARDGAME_ID +
                " WHERE user_boardgame." + COLUMN_UB_USER_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(userId)};
        return db.rawQuery(sql, selectionArgs);
    }
    public Cursor searchBoardgamesForUserByName(int userId, String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BOARDGAME + " boardgame " +
                "INNER JOIN " + TABLE_USER_BOARDGAME + " user_boardgame " +
                "ON boardgame." + COLUMN_BOARDGAME_ID + " = user_boardgame." + COLUMN_UB_BOARDGAME_ID +
                " WHERE user_boardgame." + COLUMN_UB_USER_ID + " = ? AND boardgame." + COLUMN_BOARDGAME_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{String.valueOf(userId), "%" + query + "%"};
        return db.rawQuery(sql, selectionArgs);
    }


    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
        return stream.toByteArray();
    }
    public Bitmap getBitmapFromAssets(String filename) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            inputStream = assetManager.open(filename);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }
    public Bitmap getBitmapFromBytes(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public boolean addPlayer(int userId, String playerName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_USER_ID, userId);
        values.put(COLUMN_PLAYER_NAME, playerName);
        long result = db.insert(TABLE_PLAYERS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getPlayersByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYER_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    public List<String> getGenres() {
        List<String> genres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_GENRE_NAME + " FROM " + TABLE_GENRE, null);

        if (cursor.moveToFirst()) {
            do {
                genres.add(cursor.getString(0)); // Obtener el nombre del género
            } while (cursor.moveToNext());
        }
        cursor.close();
        return genres;
    }
    public boolean isBoardgameInGenre(int boardgameId, String genreName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_BOARDGAME_GENRE + " boardgame_genre " +
                "JOIN " + TABLE_GENRE + " genre ON boardgame_genre." + COLUMN_BOARDGAME_GENRE_GENRE_ID + " = genre." + COLUMN_GENRE_ID + " " +
                "WHERE boardgame_genre." + COLUMN_BOARDGAME_GENRE_BOARDGAME_ID + " = ? AND genre." + COLUMN_GENRE_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(boardgameId), genreName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}

   /* public Cursor getBoardgameDetailsById(int boardgameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOARDGAME, null, COLUMN_ID + "=?", new String[]{String.valueOf(boardgameId)}, null, null, null);
    }
    *//*public Cursor getBoardgameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_ID + " = ?", new String[]{String.valueOf(id)});
    }*/

/*
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
    }*/

/*public Cursor getAllPlayerNames(int userId) {
    SQLiteDatabase db = this.getReadableDatabase();
    // Filtramos los jugadores por user_id
    return db.rawQuery("SELECT " + COLUMN_PLAYER_NAME + " FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYER_USER_ID + " = ?",
            new String[]{String.valueOf(userId)});
}
public boolean isPlayerExists(String playerName, int userId) {
    SQLiteDatabase db = this.getReadableDatabase();

    // Consultar si existe un jugador con este nombre y el userId dado
    String query = "SELECT COUNT(*) FROM players WHERE player_name = ? AND user_id = ?";
    Cursor cursor = db.rawQuery(query, new String[]{playerName, String.valueOf(userId)});

    cursor.moveToFirst();
    int count = cursor.getInt(0);
    cursor.close();

    return count > 0; // Retorna true si el jugador ya existe
}
public Cursor searchBoardgamesByGenre(String genreName) {
    SQLiteDatabase db = this.getReadableDatabase();
    String sql = "SELECT bg.* FROM " + TABLE_BOARDGAME + " bg " +
            "INNER JOIN " + TABLE_BOARDGAME_GENRE + " bg_g " +
            "ON bg." + COLUMN_BOARDGAME_ID + " = bg_g." + COLUMN_BG_ID + " " +
            "INNER JOIN " + TABLE_GENRE + " g " +
            "ON bg_g." + COLUMN_G_ID + " = g." + COLUMN_GENRE_ID + " " +
            "WHERE g." + COLUMN_GENRE_NAME + " = ?";
    return db.rawQuery(sql, new String[]{genreName});
}

public List<ListElement> getBoardgamesByGenre(int genreId, int userId) {
    List<ListElement> boardgamesList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Consulta SQL para obtener los juegos por género
    String sql = "SELECT bg.id, bg.name, bg.image " +
            "FROM " + TABLE_BOARDGAME + " bg " +
            "INNER JOIN " + TABLE_BOARDGAME_GENRE + " bg_genre ON bg.id = bg_genre.boardgame_id " +
            "WHERE bg_genre.genre_id = ?";

    Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(genreId)});

    if (cursor != null && cursor.moveToFirst()) {
        do {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_NAME));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_PHOTO));  // Asumiendo que tienes una columna de tipo BLOB para la imagen

            // Crea un ListElement con los datos obtenidos
            ListElement listElement = new ListElement(name, id, image);
            boardgamesList.add(listElement);
        } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();

    return boardgamesList;
}
public List<String> getAllGenresList() {
    List<String> genres = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    String query = "SELECT DISTINCT g.name FROM genre g " +
            "JOIN boardgame_genre bg ON g.id = bg.genre_id " +
            "JOIN boardgame b ON bg.boardgame_id = b.id";

    Cursor cursor = db.rawQuery(query, null);

    if (cursor.moveToFirst()) {
        do {
            int indexName = cursor.getColumnIndex("name");
            String genreName = cursor.getString(indexName);
            genres.add(genreName);
        } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return genres;
}
public List<ListElement> getAllBoardgamesList() {
    List<ListElement> genreList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Aquí asumimos que tienes una tabla o columna específica para los géneros
    Cursor cursor = db.rawQuery("SELECT DISTINCT genre FROM " + TABLE_BOARDGAME, null);

    if (cursor.moveToFirst()) {
        do {
            int indexGenre = cursor.getColumnIndex("genre");
            String genre = cursor.getString(indexGenre);
            genreList.add(new ListElement(genre, 0, null)); // Usamos el nombre del género
        } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();

    return genreList;
}
public int getGenreIdByName(String genreName) {
    int genreId = -1;
    SQLiteDatabase db = this.getReadableDatabase();

    // Supón que tienes una tabla para los géneros o un campo de género en la tabla de juegos
    Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_GENRE + " WHERE name = ?", new String[]{genreName});

    if (cursor.moveToFirst()) {
        int indexId = cursor.getColumnIndex("id");
        genreId = cursor.getInt(indexId);
    }
    cursor.close();
    db.close();

    return genreId;
}*/




