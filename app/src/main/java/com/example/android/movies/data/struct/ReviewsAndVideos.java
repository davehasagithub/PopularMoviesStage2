package com.example.android.movies.data.struct;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ObjectsCompat;

import java.util.List;

public class ReviewsAndVideos implements Parcelable {
    private List<Review> reviews;
    private List<Video> videos;

    public ReviewsAndVideos() {
    }

    // autogenerated parcelable and getter/equals/hashcode methods using android studio

    private ReviewsAndVideos(Parcel in) {
        reviews = in.createTypedArrayList(Review.CREATOR);
        videos = in.createTypedArrayList(Video.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(reviews);
        dest.writeTypedList(videos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReviewsAndVideos> CREATOR = new Creator<ReviewsAndVideos>() {
        @Override
        public ReviewsAndVideos createFromParcel(Parcel in) {
            return new ReviewsAndVideos(in);
        }

        @Override
        public ReviewsAndVideos[] newArray(int size) {
            return new ReviewsAndVideos[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewsAndVideos that = (ReviewsAndVideos) o;
        return ObjectsCompat.equals(reviews, that.reviews) &&
                ObjectsCompat.equals(videos, that.videos);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(reviews, videos);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
