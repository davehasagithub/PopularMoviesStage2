package com.example.android.movies.ui.detail;

import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;

import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.data.struct.Review;
import com.example.android.movies.data.struct.Video;
import com.example.android.movies.ui.detail.DetailAdapter.ViewHolder;

/**
 * Data structures for each view type shown in the DetailActivity Recyclerview and bound to their respective layouts.
 * For example: a review, heading, spinner or line separator. Subclasses are consolidated into here to reduce # of files.
 *
 * @see DetailActivityViewModel#createDetailItems()
 * @see DetailAdapter#onBindViewHolder(ViewHolder, int)
 */

public abstract class DetailItem {
    private final int viewType;

    DetailItem(int viewType) {
        this.viewType = viewType;
    }

    int getViewType() {
        return viewType;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }

    public long getId() {
        return 0;
    }

    static class DetailItemLoading extends DetailItem {
        DetailItemLoading() {
            super(DetailAdapter.VIEW_TYPE_LOADING);
        }
    }

    static class DetailItemSeparator extends DetailItem {
        DetailItemSeparator() {
            super(DetailAdapter.VIEW_TYPE_SEPARATOR);
        }
    }

    public static class DetailItemDetail extends DetailItem {
        private final Movie movie;

        DetailItemDetail(Movie movie) {
            super(DetailAdapter.VIEW_TYPE_DETAIL);
            this.movie = movie;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailItemDetail that = (DetailItemDetail) o;
            return ObjectsCompat.equals(movie, that.movie);
        }

        @Override
        public int hashCode() {
            return ObjectsCompat.hash(movie);
        }

        public Movie getMovie() {
            return movie;
        }

        public long getId() {
            return 0;
        }
    }

    public static class DetailItemFailure extends DetailItem {
        private final String text;

        DetailItemFailure(String text) {
            super(DetailAdapter.VIEW_TYPE_FAILURE);
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailItemFailure that = (DetailItemFailure) o;
            return ObjectsCompat.equals(text, that.text);
        }

        @Override
        public int hashCode() {
            return ObjectsCompat.hash(text);
        }

        public String getText() {
            return text;
        }

        public long getId() {
            return 0;
        }
    }

    public static class DetailItemHeading extends DetailItem {
        private final String text;

        DetailItemHeading(String text) {
            super(DetailAdapter.VIEW_TYPE_HEADING);
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailItemHeading that = (DetailItemHeading) o;
            return ObjectsCompat.equals(text, that.text);
        }

        @Override
        public int hashCode() {
            return ObjectsCompat.hash(text);
        }

        public String getText() {
            return text;
        }

        public long getId() {
            return 0;
        }
    }

    public static class DetailItemReview extends DetailItem {
        private final Review review;

        DetailItemReview(Review review) {
            super(DetailAdapter.VIEW_TYPE_REVIEW);
            this.review = review;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailItemReview that = (DetailItemReview) o;
            return ObjectsCompat.equals(review, that.review);
        }

        @Override
        public int hashCode() {
            return ObjectsCompat.hash(review);
        }

        public Review getReview() {
            return review;
        }

        public long getId() {
            return TextUtils.isEmpty(review.getId()) ? 0 : review.getId().hashCode();
        }
    }

    public static class DetailItemVideo extends DetailItem {
        private final Video video;

        DetailItemVideo(Video video) {
            super(DetailAdapter.VIEW_TYPE_VIDEO);
            this.video = video;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetailItemVideo that = (DetailItemVideo) o;
            return ObjectsCompat.equals(video, that.video);
        }

        @Override
        public int hashCode() {
            return ObjectsCompat.hash(video);
        }

        public Video getVideo() {
            return video;
        }

        public long getId() {
            return TextUtils.isEmpty(video.getId()) ? 0 : video.getId().hashCode();
        }
    }
}
