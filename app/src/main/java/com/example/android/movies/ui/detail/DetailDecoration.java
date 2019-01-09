package com.example.android.movies.ui.detail;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.movies.R;

class DetailDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager != null) {

            // https://stackoverflow.com/a/49915897
            int position = parent.getChildViewHolder(view).getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                position = parent.getChildViewHolder(view).getOldPosition();
            }

            if (position != RecyclerView.NO_POSITION) {
                int padding = parent.getContext().getResources().getDimensionPixelSize(R.dimen.movie_grid_padding);

                outRect.left = padding;
                outRect.right = padding;
                outRect.bottom = padding;

                outRect.top = (position == 0 ? padding : 0);
            }
        }
    }
}
