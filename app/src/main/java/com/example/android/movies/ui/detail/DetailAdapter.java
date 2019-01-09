package com.example.android.movies.ui.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.movies.R;
import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.databinding.DetailActivityDetailsBinding;
import com.example.android.movies.databinding.DetailActivityFailureBinding;
import com.example.android.movies.databinding.DetailActivityHeadingBinding;
import com.example.android.movies.databinding.DetailActivityReviewBinding;
import com.example.android.movies.databinding.DetailActivityVideoBinding;
import com.example.android.movies.ui.detail.DetailAdapter.ViewHolder;
import com.example.android.movies.ui.detail.DetailItem.DetailItemDetail;
import com.example.android.movies.ui.detail.DetailItem.DetailItemFailure;
import com.example.android.movies.ui.detail.DetailItem.DetailItemHeading;
import com.example.android.movies.ui.detail.DetailItem.DetailItemReview;
import com.example.android.movies.ui.detail.DetailItem.DetailItemVideo;
import com.example.android.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailAdapter extends ListAdapter<DetailItem, ViewHolder> {
    static final int VIEW_TYPE_DETAIL = 1;
    static final int VIEW_TYPE_LOADING = 2;
    static final int VIEW_TYPE_SEPARATOR = 3;
    static final int VIEW_TYPE_HEADING = 4;
    static final int VIEW_TYPE_VIDEO = 5;
    static final int VIEW_TYPE_REVIEW = 6;
    static final int VIEW_TYPE_FAILURE = 7;

    private final DetailActivityViewModel activityViewModel;

    DetailAdapter(DetailActivityViewModel viewModel) {
        super(DetailAdapter.DIFF_CALLBACK);
        this.activityViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ViewDataBinding binding;

        int layoutId;
        if (viewType == VIEW_TYPE_DETAIL) {
            layoutId = R.layout.detail_activity_details;
        } else if (viewType == VIEW_TYPE_LOADING) {
            layoutId = R.layout.detail_activity_loading;
        } else if (viewType == VIEW_TYPE_FAILURE) {
            layoutId = R.layout.detail_activity_failure;
        } else if (viewType == VIEW_TYPE_SEPARATOR) {
            layoutId = R.layout.detail_activity_separator;
        } else if (viewType == VIEW_TYPE_HEADING) {
            layoutId = R.layout.detail_activity_heading;
        } else if (viewType == VIEW_TYPE_VIDEO) {
            layoutId = R.layout.detail_activity_video;
        } else if (viewType == VIEW_TYPE_REVIEW) {
            layoutId = R.layout.detail_activity_review;
        } else {
            throw new IllegalArgumentException("unhandled view type " + viewType);
        }

        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DetailItem item = getItem(position);
        int viewType = getItemViewType(position);
        Context context = viewHolder.binding.getRoot().getContext();
        if (viewType == VIEW_TYPE_DETAIL) {
            Movie movie = ((DetailItemDetail) item).getMovie();
            if (movie != null) {

                Uri posterUri = NetworkUtils.getMoviePosterUri(movie.getPosterPath());
                Picasso.with(context)
                        .load(posterUri)
                        .error(R.drawable.ic_poster_missing)
                        .noPlaceholder()
                        .into(((DetailActivityDetailsBinding) viewHolder.binding).ivMoviePoster);

                ((DetailActivityDetailsBinding) viewHolder.binding).setDetailItem((DetailItemDetail) item);
                ((DetailActivityDetailsBinding) viewHolder.binding).setViewModel(activityViewModel);
            }
        } else if (viewType == VIEW_TYPE_HEADING) {
            ((DetailActivityHeadingBinding) viewHolder.binding).setDetailItem((DetailItemHeading) item);
        } else if (viewType == VIEW_TYPE_FAILURE) {
            ((DetailActivityFailureBinding) viewHolder.binding).setDetailItem((DetailItemFailure) item);
        } else if (viewType == VIEW_TYPE_VIDEO) {
            ((DetailActivityVideoBinding) viewHolder.binding).setViewModel(activityViewModel);
            ((DetailActivityVideoBinding) viewHolder.binding).setDetailItem((DetailItemVideo) item);
        } else if (viewType == VIEW_TYPE_REVIEW) {
            ((DetailActivityReviewBinding) viewHolder.binding).setDetailItem((DetailItemReview) item);
        }
    }

    private static final DiffUtil.ItemCallback<DetailItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DetailItem>() {
                @Override
                public boolean areItemsTheSame(DetailItem oldItem, DetailItem newItem) {
                    return oldItem.getClass().equals(newItem.getClass()) && oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(DetailItem oldItem, @NonNull DetailItem newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
