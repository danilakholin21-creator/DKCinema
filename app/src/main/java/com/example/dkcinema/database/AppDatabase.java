package com.example.dkcinema.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.dkcinema.dao.MovieDao;
import com.example.dkcinema.dao.ReviewDao;
import com.example.dkcinema.dao.UserDao;
import com.example.dkcinema.dao.WatchLaterDao;
import com.example.dkcinema.models.Movie;
import com.example.dkcinema.models.Review;
import com.example.dkcinema.models.User;
import com.example.dkcinema.models.WatchLater;

@Database(entities = {User.class, Movie.class, Review.class, WatchLater.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract ReviewDao reviewDao();
    public abstract WatchLaterDao watchLaterDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "dkcinema.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}