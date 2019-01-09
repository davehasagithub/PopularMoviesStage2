package com.example.android.movies.ui.detail;

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
import com.example.android.movies.data.struct.Review;
import com.example.android.movies.data.struct.ReviewsAndVideos;
import com.example.android.movies.data.struct.Video;
import com.example.android.movies.ui.detail.DetailItem.DetailItemFailure;
import com.example.android.movies.utilities.Event;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DetailActivityViewModel extends AndroidViewModel {

    private Movie currentMovie;
    private ReviewsAndVideos reviewsAndVideos;
    private final MutableLiveData<Event<Video>> doVideoClick = new MutableLiveData<>();
    private final MediatorLiveData<List<DetailItem>> itemsLiveData = new MediatorLiveData<>();
    private int savedScrollPosition = -1;

    public DetailActivityViewModel(@NonNull Application application) {
        super(application);
    }

    void init(Movie movie) {
        if (currentMovie == null && movie != null) {
            this.currentMovie = movie;

            String apiKey = getApplication().getApplicationContext().getString(R.string.movie_api_key);
            final LiveData<ReviewsAndVideos> result = MoviesRepository.fetchReviewsAndVideos(apiKey, movie.getId());

            itemsLiveData.addSource(result, new Observer<ReviewsAndVideos>() {
                @Override
                public void onChanged(@Nullable ReviewsAndVideos newReviewsAndVideos) {
                    itemsLiveData.removeSource(result);
                    reviewsAndVideos = newReviewsAndVideos;
                    itemsLiveData.setValue(createDetailItems());
                }
            });


            LiveData<Movie> favoriteMovieLiveData = MoviesRepository.getMovieLiveData(getApplication().getApplicationContext(), movie.getId());
            itemsLiveData.addSource(favoriteMovieLiveData, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    currentMovie.setFavorite(movie != null);
                    itemsLiveData.setValue(createDetailItems());
                }
            });
        }
    }

    public List<DetailItem> createDetailItems() {
        List<DetailItem> items = new ArrayList<>();

        if (currentMovie != null) {
            items.add(new DetailItem.DetailItemDetail(currentMovie.cloneMovie()));

            if (reviewsAndVideos == null) {
                items.add(new DetailItem.DetailItemLoading());
            } else {
                Context context = getApplication().getApplicationContext();

                boolean hasVideos = false;
                items.add(new DetailItem.DetailItemSeparator());

                if (reviewsAndVideos.getVideos() == null || reviewsAndVideos.getVideos().size() > 0) {
                    hasVideos = true;
                    items.add(new DetailItem.DetailItemHeading(context.getString(R.string.details_heading_videos)));
                    if (reviewsAndVideos.getVideos() == null) {
                        items.add(new DetailItemFailure(context.getString(R.string.failed_to_load_videos)));
                    } else {
                        for (Video video : reviewsAndVideos.getVideos()) {
                            items.add(new DetailItem.DetailItemVideo(video));
                        }
                    }
                }

                if (reviewsAndVideos.getReviews() == null || reviewsAndVideos.getReviews().size() > 0) {
                    if (hasVideos) {
                        items.add(new DetailItem.DetailItemSeparator());
                    }
                    items.add(new DetailItem.DetailItemHeading(context.getString(R.string.details_heading_reviews)));
                    if (reviewsAndVideos.getReviews() == null) {
                        items.add(new DetailItemFailure(context.getString(R.string.failed_to_load_reviews)));
                    } else {
                        for (Review review : reviewsAndVideos.getReviews()) {
                            items.add(new DetailItem.DetailItemReview(review));
                        }
                    }
                }
            }
        }

        return items;
    }

    public void onFavorite(Movie movie) {
        MoviesRepository.markFavoriteMovie(getApplication().getApplicationContext(), movie);
    }

    public void onClickVideo(Video video) {
        doVideoClick.setValue(new Event<>(video));
    }

    LiveData<Event<Video>> doLaunchVideo() {
        return doVideoClick;
    }

    public MediatorLiveData<List<DetailItem>> getItemsLiveData() {
        return itemsLiveData;
    }

    public Movie getCurrentMovie() {
        return currentMovie;
    }

    public int getSavedScrollPosition() {
        return savedScrollPosition;
    }

    public void setSavedScrollPosition(int savedScrollPosition) {
        this.savedScrollPosition = savedScrollPosition;
    }
}
