package com.example.gamekeeper.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gamekeeper.models.ListElement;
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
    // Tabla relación entre jugadores y juegos con veces jugados
    public static final String TABLE_PLAYER_BOARDGAME = "player_boardgame";
    public static final String COLUMN_PLAYER_BOARDGAME_PLAYER_ID = "player_id";
    public static final String COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID = "boardgame_id";
    public static final String COLUMN_PLAYER_BOARDGAME_HAS_PLAYED = "has_played";


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
                    COLUMN_BOARDGAME_PHOTO + " TEXT, " +
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
                    "FOREIGN KEY (" + COLUMN_UB_BOARDGAME_ID + ") REFERENCES " + TABLE_BOARDGAME + "(" + COLUMN_BOARDGAME_ID + ") ON DELETE CASCADE);";
    private static final String TABLE_CREATE_PLAYER_BOARDGAME =
            "CREATE TABLE " + TABLE_PLAYER_BOARDGAME + " (" +
                    COLUMN_PLAYER_BOARDGAME_PLAYER_ID + " INTEGER, " +
                    COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID + " INTEGER, " +
                    COLUMN_PLAYER_BOARDGAME_HAS_PLAYED + " INTEGER DEFAULT 0, " +
                    "PRIMARY KEY (" + COLUMN_PLAYER_BOARDGAME_PLAYER_ID + ", " + COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_PLAYER_BOARDGAME_PLAYER_ID + ") REFERENCES " + TABLE_PLAYERS + "(" + COLUMN_PLAYER_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID + ") REFERENCES " + TABLE_BOARDGAME + "(" + COLUMN_BOARDGAME_ID + ") ON DELETE CASCADE);";
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
        db.execSQL(TABLE_CREATE_PLAYER_BOARDGAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARDGAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARDGAME_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BOARDGAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER_BOARDGAME);
        onCreate(db);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public boolean addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
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
    public boolean isEmailAlreadyRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_EMAIL},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
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
    public boolean hasGamesForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USER_BOARDGAME + " WHERE " + COLUMN_UB_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        boolean hasGames = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                hasGames = cursor.getInt(0) > 0;
            }
            cursor.close();
        }
        db.close();
        return hasGames;
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

    public long addBoardgame(String name, String photoURl, String description, int yearPublished, String numberOfPlayers, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_BOARDGAME, new String[]{COLUMN_BOARDGAME_NAME}, COLUMN_BOARDGAME_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOARDGAME_NAME, name);
        values.put(COLUMN_BOARDGAME_PHOTO, photoURl);
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

    public Cursor getBoardGameById(int gameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_ID + " = ?", new String[]{String.valueOf(gameId)});
    }



    public Cursor getAllGenres() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GENRE, null);
    }

    public List<ListElement> getAllBoardgames() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ListElement> boardgameList = new ArrayList<>();

        // Realizar la consulta para obtener todos los juegos de mesa
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME, null);

        // Iterar a través del cursor para crear los objetos ListElement
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int indexId = cursor.getColumnIndex(COLUMN_BOARDGAME_ID);
                int indexName = cursor.getColumnIndex(COLUMN_BOARDGAME_NAME);
                int indexPhoto = cursor.getColumnIndex(COLUMN_BOARDGAME_PHOTO);
                int id = cursor.getInt(indexId);
                String name = cursor.getString(indexName);
                String image = cursor.getString(indexPhoto);

                // Crear un nuevo ListElement y añadirlo a la lista
                ListElement boardgame = new ListElement(name, id, image);
                boardgameList.add(boardgame);
            }
            cursor.close(); // Cerrar el cursor después de usarlo
        }

        return boardgameList;
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
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOARDGAME_PHOTO));
                ListElement listElement = new ListElement(name, id, image);
                listElements.add(listElement);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listElements;
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
                genres.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return genres;
    }
    public Cursor getGameData(int gameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOARDGAME + " WHERE " + COLUMN_BOARDGAME_ID + " = ?",
                new String[]{String.valueOf(gameId)});
    }
    public List<String> getBoardGameGenres(int boardGameId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT g." + COLUMN_GENRE_NAME +
                " FROM " + TABLE_GENRE + " g " +
                " JOIN " + TABLE_BOARDGAME_GENRE + " bg " +
                " ON g." + COLUMN_GENRE_ID + " = bg." + COLUMN_BOARDGAME_GENRE_GENRE_ID +
                " WHERE bg." + COLUMN_BOARDGAME_GENRE_BOARDGAME_ID + " = ?", new String[]{String.valueOf(boardGameId)});

        List<String> genres = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int genreIndex = cursor.getColumnIndex(COLUMN_GENRE_NAME);
                if (genreIndex != -1) {
                    genres.add(cursor.getString(genreIndex));
                } else {
                    Log.e("DatabaseHelper", "Column index not found for genre");
                }
            }
            cursor.close();
        }
        return genres;
    }



    // Método para verificar si un género está asociado a un juego
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


    public boolean addPlayerBoardgameOnce(int playerId, int boardgameId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                TABLE_PLAYER_BOARDGAME,
                null,
                COLUMN_PLAYER_BOARDGAME_PLAYER_ID + " = ? AND " + COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID + " = ?",
                new String[]{String.valueOf(playerId), String.valueOf(boardgameId)},
                null,
                null,
                null
        );

        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();

        if (exists) {
            Log.d("DEBUGPlayers", "Relación ya existente para Jugador " + playerId + " y Juego " + boardgameId);
            db.close();
            return false;
        }

        Log.d("DEBUGPlayers", "Insertando relación: Jugador " + playerId + " - Juego " + boardgameId);

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_BOARDGAME_PLAYER_ID, playerId);
        values.put(COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID, boardgameId);
        values.put(COLUMN_PLAYER_BOARDGAME_HAS_PLAYED, 1); // Suponiendo que '1' indica que el jugador ya jugó el juego

        long result = db.insert(TABLE_PLAYER_BOARDGAME, null, values);

        if (result == -1) {
            Log.e("DEBUGPlayers", "Error al insertar relación");
        } else {
            Log.d("DEBUGPlayers", "Relación insertada correctamente");
        }

        db.close();
        return result != -1;
    }

    public boolean hasPlayerAlreadyPlayed(int playerId, int boardgameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PLAYER_BOARDGAME,
                new String[]{COLUMN_PLAYER_BOARDGAME_HAS_PLAYED},
                COLUMN_PLAYER_BOARDGAME_PLAYER_ID + " = ? AND " + COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID + " = ?",
                new String[]{String.valueOf(playerId), String.valueOf(boardgameId)},
                null,
                null,
                null
        );

        boolean alreadyPlayed = false;
        if (cursor != null && cursor.moveToFirst()) {
            int hasPlayed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_BOARDGAME_HAS_PLAYED));
            alreadyPlayed = hasPlayed == 1;
            cursor.close();
        }
        db.close();
        return alreadyPlayed;
    }

    public boolean hasPlayerPlayedGame(int playerId, int gameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PLAYER_BOARDGAME_HAS_PLAYED +
                " FROM " + TABLE_PLAYER_BOARDGAME +
                " WHERE " + COLUMN_PLAYER_BOARDGAME_PLAYER_ID + " = ? AND " +
                COLUMN_PLAYER_BOARDGAME_BOARDGAME_ID + " = ?";
        String[] args = {String.valueOf(playerId), String.valueOf(gameId)};
        Cursor cursor = db.rawQuery(query, args);

        boolean hasPlayed = false;
        if (cursor.moveToFirst()) {
            hasPlayed = cursor.getInt(0) == 1;
        }
        cursor.close();
        db.close();
        return hasPlayed;
    }

    public int getPlayerIdByName(String playerName, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PLAYERS,
                new String[]{COLUMN_PLAYER_ID},
                COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_PLAYER_USER_ID + " = ?",
                new String[]{playerName, String.valueOf(userId)},
                null, null, null
        );

        int playerId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            playerId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_ID));
            cursor.close();
        }
        db.close();
        return playerId;
    }

}


