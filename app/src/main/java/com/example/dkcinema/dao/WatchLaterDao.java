package com.example.dkcinema.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dkcinema.models.WatchLater;

import java.util.List;

@Dao
public interface WatchLaterDao {
    @Insert
    void insert(WatchLater watchLater);

    @Query("SELECT * FROM watch_later WHERE userId = :userId")
    List<WatchLater> getWatchLaterByUser(int userId);

    @Query("SELECT movieId FROM watch_later WHERE userId = :userId")
    List<Integer> getWatchLaterMovieIds(int userId);

    @Query("DELETE FROM watch_later WHERE userId = :userId AND movieId = :movieId")
    void delete(int userId, int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM watch_later WHERE userId = :userId AND movieId = :movieId)")
    boolean isInWatchLater(int userId, int movieId);
}