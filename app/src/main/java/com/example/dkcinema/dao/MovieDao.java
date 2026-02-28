package com.example.dkcinema.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dkcinema.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Insert
    void insertAll(List<Movie> movies);

    @Update
    void update(Movie movie);

    @Query("SELECT * FROM movies")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movies WHERE genre = :genre")
    List<Movie> getMoviesByGenre(String genre);

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Movie getMovieById(int movieId);
}