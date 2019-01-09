package com.example.android.movies.data.repo.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.android.movies.data.struct.Movie;

import java.util.List;

@Dao
public abstract class MovieDao extends BaseDao<Movie> {

    @Query("SELECT * FROM favoriteMovies WHERE id = :movieId")
    public abstract LiveData<Movie> getMovie(int movieId);

    @Query("SELECT * FROM favoriteMovies order by favoritedDate DESC")
    public abstract LiveData<List<Movie>> getFavoriteMovies();

}
