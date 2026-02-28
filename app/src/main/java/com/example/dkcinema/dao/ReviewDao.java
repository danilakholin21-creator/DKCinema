package com.example.dkcinema.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dkcinema.models.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    void insert(Review review);

    @Update
    void update(Review review);

    @Query("SELECT * FROM reviews WHERE movieId = :movieId ORDER BY timestamp DESC")
    List<Review> getReviewsForMovie(int movieId);

    @Query("SELECT * FROM reviews WHERE userId = :userId")
    List<Review> getReviewsByUser(int userId);

    @Query("SELECT * FROM reviews WHERE userId = :userId AND movieId = :movieId")
    Review getUserReviewForMovie(int userId, int movieId);

    @Query("SELECT AVG(rating) FROM reviews WHERE movieId = :movieId")
    double getAverageRatingForMovie(int movieId);
}