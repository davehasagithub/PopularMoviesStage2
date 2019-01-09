package com.example.android.movies.data.struct;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ObjectsCompat;

public class Review implements Parcelable {
    private final String id;
    private final String author;
    private final String content;

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    // autogenerated parcelable and getter/equals/hashcode methods using android studio

    private Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return ObjectsCompat.equals(id, review.id) &&
                ObjectsCompat.equals(author, review.author) &&
                ObjectsCompat.equals(content, review.content);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(id, author, content);
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}