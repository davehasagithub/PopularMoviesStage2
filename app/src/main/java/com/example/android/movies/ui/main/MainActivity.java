package com.example.android.movies.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.movies.R;
import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.data.struct.SortType;
import com.example.android.movies.databinding.MainActivityBinding;
import com.example.android.movies.ui.detail.DetailActivity;
import com.example.android.movies.utilities.Event;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String INSTANCE_STATE_KEY_SORT_TYPE = "sort_type";

    private MainActivityBinding binding;
    private MainActivityViewModel viewModel;

    private MenuItem menuItemSortPopular;
    private MenuItem menuItemSortTopRated;
    private MenuItem menuItemSortFavorites;
    private MovieAdapter adapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(MainActivity.INSTANCE_STATE_KEY_SORT_TYPE, viewModel.getSortType());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setSupportActionBar(binding.toolbar);

        addViewModelObservers();

        adapter = new MovieAdapter(viewModel);
        adapter.setHasStableIds(true);

        binding.recyclerviewMovies.setHasFixedSize(true);
        binding.recyclerviewMovies.setAdapter(adapter);
        binding.recyclerviewMovies.addItemDecoration(new MovieDecoration());
        binding.recyclerviewMovies.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.movie_grid_columns)));

        if (savedInstanceState == null) {
            viewModel.init(SortType.MOST_POPULAR);
        } else {
            viewModel.init((SortType) savedInstanceState.getSerializable(MainActivity.INSTANCE_STATE_KEY_SORT_TYPE));
        }

        updateSubtitle();
    }

    private void addViewModelObservers() {
        viewModel.doReloadMovies().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(@Nullable Event<Boolean> event) {
                if (event != null && event.getContentIfNotHandled() != null) {
                    fetchMovies();
                }
            }
        });

        viewModel.doMovieSelected().observe(this, new Observer<Event<Movie>>() {
            @Override
            public void onChanged(@Nullable Event<Movie> event) {
                if (event != null) {
                    Movie movie = event.getContentIfNotHandled();
                    if (movie != null) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.INTENT_EXTRA_MOVIE_KEY, movie);
                        startActivity(intent);
                    }
                }
            }
        });

        viewModel.getMoviesLiveData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    ArrayList<Movie> newMovies = new ArrayList<>();
                    for (Movie movie : movies) {
                        newMovies.add(movie.cloneMovie());
                    }
                    adapter.submitList(newMovies);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menuItemSortPopular = menu.findItem(R.id.action_sort_by_popular);
        menuItemSortTopRated = menu.findItem(R.id.action_sort_by_top_rated);
        menuItemSortFavorites = menu.findItem(R.id.action_sort_by_favorites);
        selectCurrentSortMenuItem();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SortType sortType = null;
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort_by_popular) sortType = SortType.MOST_POPULAR;
        else if (itemId == R.id.action_sort_by_top_rated) sortType = SortType.TOP_RATED;
        else if (itemId == R.id.action_sort_by_favorites) sortType = SortType.FAVORITES;

        if (sortType != null) {
            changeSort(sortType);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSubtitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int subtitleResId = 0;
            SortType sortType = viewModel.getSortType();
            if (sortType != null) {
                if (sortType == SortType.MOST_POPULAR) subtitleResId = R.string.title_most_popular;
                else if (sortType == SortType.TOP_RATED) subtitleResId = R.string.title_top_rated;
                else if (sortType == SortType.FAVORITES) subtitleResId = R.string.title_favorites;
                actionBar.setSubtitle(getString(subtitleResId));
            }
        }
    }

    private void selectCurrentSortMenuItem() {
        MenuItem menuItem = null;
        SortType sortType = viewModel.getSortType();
        if (sortType != null) {
            if (sortType == SortType.MOST_POPULAR) menuItem = menuItemSortPopular;
            else if (sortType == SortType.TOP_RATED) menuItem = menuItemSortTopRated;
            else if (sortType == SortType.FAVORITES) menuItem = menuItemSortFavorites;
            if (menuItem != null) {
                menuItem.setChecked(true);
            }
        }
    }

    private void changeSort(SortType newSortType) {
        viewModel.updateSortType(newSortType);
        selectCurrentSortMenuItem();
        updateSubtitle();
        fetchMovies();
    }

    private void fetchMovies() {
        adapter.submitList(new ArrayList<Movie>());
        binding.btRetry.setVisibility(View.GONE);
        binding.appbar.setExpanded(true);
        viewModel.fetchMovies();
    }
}
