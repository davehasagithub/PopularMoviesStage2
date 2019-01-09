package com.example.android.movies.ui.main;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.movies.R;
import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.databinding.MoviesCellBinding;
import com.example.android.movies.ui.main.MovieAdapter.ViewHolder;
import com.example.android.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends ListAdapter<Movie, ViewHolder> {

    private final MainActivityViewModel activityViewModel;

    MovieAdapter(MainActivityViewModel viewModel) {
        super(MovieAdapter.DIFF_CALLBACK);
        activityViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder((MoviesCellBinding) DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.movies_cell, viewGroup, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final MoviesCellBinding binding;

        ViewHolder(MoviesCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Movie movie = getItem(position);
        MoviesCellBinding binding = viewHolder.binding;
        if (movie != null) {
            viewHolder.binding.setMovie(movie);
            viewHolder.binding.setViewModel(activityViewModel);
            binding.ivMoviePoster.setImageDrawable(null);

            Picasso picasso = Picasso.with(binding.tvMovieTitle.getContext());
            Uri posterUri = NetworkUtils.getMoviePosterUri(movie.getPosterPath());
            picasso.load(posterUri)
                    .error(R.drawable.ic_poster_missing)
                    .noPlaceholder()
                    .into(binding.ivMoviePoster);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(Movie oldMovie, Movie newMovie) {
                    return oldMovie.getId() == newMovie.getId();
                }

                @Override
                public boolean areContentsTheSame(Movie oldMovie, @NonNull Movie newMovie) {
                    return oldMovie.equals(newMovie);
                }
            };
}
