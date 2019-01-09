package com.example.android.movies.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.movies.R;
import com.example.android.movies.data.repo.MoviesRepository;
import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.data.struct.SortType;
import com.example.android.movies.utilities.Event;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class MainActivityViewModel extends AndroidViewModel {

    private SortType sortType;
    private List<Movie> currentMovies;
    private LiveData<List<Movie>> favoriteMoviesLiveData;

    private final MutableLiveData<Boolean> failedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> emptyLiveData = new MutableLiveData<>();
    private final MediatorLiveData<List<Movie>> moviesLiveData = new MediatorLiveData<>();
    private final MutableLiveData<Event<Boolean>> doReloadMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<Movie>> doMovieSelected = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    void init(SortType sortType) {
        Context context = getApplication().getApplicationContext();
        this.sortType = sortType;
        if (currentMovies == null) {
            failedLiveData.setValue(false);
            emptyLiveData.setValue(false);
            currentMovies = new ArrayList<>();
            moviesLiveData.setValue(currentMovies);
            favoriteMoviesLiveData = MoviesRepository.getFavoriteMoviesLiveData(context);
            moviesLiveData.addSource(favoriteMoviesLiveData, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> favoriteMovies) {
                    if (MainActivityViewModel.this.sortType == SortType.FAVORITES) {
                        clearAndAddMovies(favoriteMovies);
                    } else {
                        updateFavorites();
                    }
                }
            });
            onRetryButtonClick();
        }
    }

    private void updateFavorites() {
        List<Movie> favoriteMovies = favoriteMoviesLiveData.getValue();
        if (favoriteMovies != null) {
            for (Movie movie : currentMovies) {
                boolean matched = false;
                for (Movie m : favoriteMovies) {
                    if (m.getId() == movie.getId()) {
                        matched = true;
                        break;
                    }
                }
                movie.setFavorite(matched);
            }
            moviesLiveData.setValue(currentMovies);
        }
    }

    void updateSortType(SortType newSortType) {
        sortType = newSortType;
    }

    private void clearAndAddMovies(List<Movie> newMovies) {
        if (currentMovies != null) {
            if (newMovies != null) {
                currentMovies.clear();
                currentMovies.addAll(newMovies);
                failedLiveData.setValue(false);
                emptyLiveData.setValue(newMovies.size() == 0);
                updateFavorites();
            } else {
                failedLiveData.setValue(true);
                emptyLiveData.setValue(false);
            }
        }
    }

    public void onRetryButtonClick() {
        doReloadMoviesLiveData.setValue(new Event<>(true));
    }

    void fetchMovies() {
        Context context = getApplication().getApplicationContext();
        String apiKey = context.getString(R.string.movie_api_key);

        failedLiveData.setValue(false);
        emptyLiveData.setValue(false);
        final LiveData<List<Movie>> result = MoviesRepository.fetchMovies(context, apiKey, sortType);
        moviesLiveData.addSource(result, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                moviesLiveData.removeSource(result);
                clearAndAddMovies(movies);
            }
        });
    }

    public void onClickMovie(Movie movie) {
        doMovieSelected.setValue(new Event<>(movie));
    }

    public LiveData<Boolean> getFailedLiveData() {
        return failedLiveData;
    }

    public LiveData<Boolean> getEmptyLiveData() {
        return emptyLiveData;
    }

    LiveData<List<Movie>> getMoviesLiveData() {
        return moviesLiveData;
    }

    SortType getSortType() {
        return sortType;
    }

    LiveData<Event<Boolean>> doReloadMovies() {
        return doReloadMoviesLiveData;
    }

    LiveData<Event<Movie>> doMovieSelected() {
        return doMovieSelected;
    }
}
