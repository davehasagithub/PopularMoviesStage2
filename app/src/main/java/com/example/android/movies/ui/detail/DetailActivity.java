package com.example.android.movies.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;

import com.example.android.movies.R;
import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.data.struct.Video;
import com.example.android.movies.databinding.DetailActivityBinding;
import com.example.android.movies.utilities.Event;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_MOVIE_KEY = "movie";

    private static final String INSTANCE_STATE_KEY_MOVIE = "movie";

    private DetailActivityBinding binding;
    private DetailActivityViewModel viewModel;
    private DetailAdapter adapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DetailActivity.INSTANCE_STATE_KEY_MOVIE, viewModel.getCurrentMovie());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Movie movie;
        if (savedInstanceState == null) {
            movie = getIntent().getParcelableExtra(DetailActivity.INTENT_EXTRA_MOVIE_KEY);
        } else {
            movie = savedInstanceState.getParcelable(DetailActivity.INSTANCE_STATE_KEY_MOVIE);
        }

        viewModel = ViewModelProviders.of(this).get(DetailActivityViewModel.class);
        viewModel.init(movie);

        binding = DataBindingUtil.setContentView(this, R.layout.detail_activity);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            binding.collapsingToolbar.setTitle(Movie.getStringOrNotAvailable(this, movie == null ? null : movie.getTitle()));
        }

        addViewModelObservers();

        adapter = new DetailAdapter(viewModel);
        adapter.setHasStableIds(false);

        binding.recyclerviewDetails.setHasFixedSize(false);
        binding.recyclerviewDetails.setAdapter(adapter);
        binding.recyclerviewDetails.addItemDecoration(new DetailDecoration());
        binding.recyclerviewDetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        SimpleItemAnimator itemAnimator = ((SimpleItemAnimator) binding.recyclerviewDetails.getItemAnimator());
        if (itemAnimator != null) {
            // Prevent the detail row from animating when toggling the Movie.favorite property
            itemAnimator.setSupportsChangeAnimations(false);
        }
    }

    private void addViewModelObservers() {
        viewModel.getItemsLiveData().observe(this, new Observer<List<DetailItem>>() {
            @Override
            public void onChanged(@Nullable List<DetailItem> detailItems) {
                adapter.submitList(detailItems);
                restoreScrollPosition();
            }
        });

        viewModel.doLaunchVideo().observe(this, new Observer<Event<Video>>() {
            @Override
            public void onChanged(@Nullable Event<Video> event) {
                if (event != null) {
                    Video video = event.getContentIfNotHandled();
                    if (video != null) {
                        if ("youtube".equalsIgnoreCase(video.getSite())) {
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
                            DetailActivity.this.startActivity(webIntent);
                        } else {
                            Snackbar.make(binding.getRoot(), R.string.unknown_video_site, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void saveScrollPosition() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.recyclerviewDetails.getLayoutManager());
        if (layoutManager != null) {
            viewModel.setSavedScrollPosition(layoutManager.findFirstVisibleItemPosition());
        }
    }

    private void restoreScrollPosition() {
        final int topVisiblePos = viewModel.getSavedScrollPosition();
        if (topVisiblePos > 0) {
            viewModel.setSavedScrollPosition(-1);
            binding.recyclerviewDetails.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.recyclerviewDetails.getLayoutManager());
                    if (layoutManager != null) {
                        layoutManager.scrollToPositionWithOffset(topVisiblePos, 0);
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScrollPosition();
    }
}
