package com.example.android.movies.data.repo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.data.struct.MovieTypeConverters;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(MovieTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "poplar_movies";

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract MovieDao movieDao();
}
