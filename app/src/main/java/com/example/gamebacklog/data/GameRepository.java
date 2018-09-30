package com.example.gamebacklog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gamebacklog.data.db.DBHelper;
import com.example.gamebacklog.data.db.GameContract;
import com.example.gamebacklog.data.model.Game;

public class GameRepository {

    private SQLiteDatabase database;
    private final DBHelper dbHelper;
    private final String[] GAMES_ALL_COLUMNS = {
            GameContract.COLUMN_NAME_ID,
            GameContract.COLUMN_NAME_TITLE,
            GameContract.COLUMN_NAME_PLATFORM,
            GameContract.COLUMN_NAME_DATE,
            GameContract.COLUMN_NAME_STATUS,
            GameContract.COLUMN_NAME_NOTES };

    public GameRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Save a game entry
     *
     * @param game the entry to be saved.
     */
    public void save(Game game) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameContract.COLUMN_NAME_TITLE, game.getTitle());
        values.put(GameContract.COLUMN_NAME_PLATFORM, game.getPlatform());
        values.put(GameContract.COLUMN_NAME_DATE, game.getDateAdded());
        values.put(GameContract.COLUMN_NAME_STATUS, game.getStatus());
        values.put(GameContract.COLUMN_NAME_NOTES, game.getNotes());
        // Inserting Row
        db.insert(GameContract.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Update a existing game entry
     *
     * @param id   the id of the entry
     * @param game the existing game entry
     */
    public void update(int id, Game game) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameContract.COLUMN_NAME_TITLE, game.getTitle());
        values.put(GameContract.COLUMN_NAME_PLATFORM, game.getPlatform());
        values.put(GameContract.COLUMN_NAME_DATE, game.getDateAdded());
        values.put(GameContract.COLUMN_NAME_STATUS, game.getStatus());
        values.put(GameContract.COLUMN_NAME_NOTES, game.getNotes());

        db.update(GameContract.TABLE_NAME, values, GameContract.COLUMN_NAME_ID + "= ?", new String[]{String.valueOf(id)});
        db.close(); // Closing database connection
    }


    /**
     * Finds all game entries
     *
     * @return a cursor holding the game entries.
     */
    public Cursor findAll() {
        // If we have not yet opened the database, open it
        if (database == null) {
            database = dbHelper.getReadableDatabase();
        }
        return database.query(GameContract.TABLE_NAME, GAMES_ALL_COLUMNS, null, null, null, null, null);
    }

    /**
     * Delete a game entry
     *
     * @param id the id of the entry to be deleted.
     */
    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(GameContract.TABLE_NAME, GameContract.COLUMN_NAME_ID + " =?",
                new String[]{Integer.toString(id)});
        db.close();
    }
}
