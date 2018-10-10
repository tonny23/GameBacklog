package com.example.gamebacklog.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.gamebacklog.data.model.Game;

import java.util.List;

@Dao
public interface GameDao {
    @Query("SELECT * FROM games")
    public List<Game> getAllgames();

    @Insert
    public void insertGame(Game game);

    @Delete
    public void deleteGame(Game game);

    @Update
    public void updateGame(Game game);
}
