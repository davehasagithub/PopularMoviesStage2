package com.example.android.movies.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.example.android.movies.data.repo.db.AppDatabase;
import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.data.struct.ReviewsAndVideos;
import com.example.android.movies.data.struct.SortType;
import com.example.android.movies.utilities.AppExecutors;
import com.example.android.movies.utilities.JsonParsingUtils;
import com.example.android.movies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MoviesRepository {
    public static LiveData<Movie> getMovieLiveData(Context context, int movieId) {
        return AppDatabase.getInstance(context).movieDao().getMovie(movieId);
    }

    public static LiveData<List<Movie>> getFavoriteMoviesLiveData(Context context) {
        return AppDatabase.getInstance(context).movieDao().getFavoriteMovies();
    }

    public static void markFavoriteMovie(final Context context, final Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (movie.isFavorite()) {
                    AppDatabase.getInstance(context).movieDao().delete(movie);
                } else {
                    movie.setFavoritedDate(new Date());
                    AppDatabase.getInstance(context).movieDao().upsertItems(Collections.singletonList(movie));
                }
            }
        });
    }

    public static LiveData<List<Movie>> fetchMovies(final Context context, String apiKey, SortType sortType) {
        if (sortType == SortType.FAVORITES) {
            return AppDatabase.getInstance(context).movieDao().getFavoriteMovies();
        }

        final MediatorLiveData<List<Movie>> moviesLiveData = new MediatorLiveData<>();
        final URL url = NetworkUtils.buildMoviesUrl(sortType, apiKey);

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = NetworkUtils.getResponseFromHttpUrl(url);
                    List<Movie> movies = JsonParsingUtils.getMoviesFromJson(response);
                    moviesLiveData.postValue(movies);
                } catch (IOException e) {
                    e.printStackTrace();
                    moviesLiveData.postValue(null);
                }
            }
        });

        return moviesLiveData;
    }

    public static LiveData<ReviewsAndVideos> fetchReviewsAndVideos(final String apiKey, final int movieId) {
        final MutableLiveData<ReviewsAndVideos> reviewsAndVideosLiveData = new MutableLiveData<>();

        // Send both requests to the executor pool and then block (up to 5s) in this thread until complete
        new Thread(new Runnable() {
            // https://stackoverflow.com/a/1250657
            final CountDownLatch latch = new CountDownLatch(2);
            final ReviewsAndVideos reviewsAndVideos = new ReviewsAndVideos();

            @Override
            public void run() {
                AppExecutors.getInstance().networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String reviewsJsonString = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildReviewsUrl(apiKey, movieId));
                            reviewsAndVideos.setReviews(JsonParsingUtils.getReviewsFromJson(reviewsJsonString));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    }
                });

                AppExecutors.getInstance().networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String videosJsonString = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildVideosUrl(apiKey, movieId));
                            reviewsAndVideos.setVideos(JsonParsingUtils.getVideosFromJson(videosJsonString));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    }
                });

                try {
                    latch.await(5, TimeUnit.SECONDS);
                    reviewsAndVideosLiveData.postValue(reviewsAndVideos);
                } catch (InterruptedException e) {
                    reviewsAndVideosLiveData.postValue(null);
                }
            }
        }).start();

        return reviewsAndVideosLiveData;
    }

}
