package com.example.android.movies.data.struct;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ObjectsCompat;

public class Video implements Parcelable {
    private final String id;
    private final String key;
    private final String name;
    private final String site;
    private final String type;

    public Video(String id, String key, String name, String site, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    // autogenerated parcelable and getter/equals/hashcode methods using android studio

    private Video(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return ObjectsCompat.equals(id, video.id) &&
                ObjectsCompat.equals(key, video.key) &&
                ObjectsCompat.equals(name, video.name) &&
                ObjectsCompat.equals(site, video.site) &&
                ObjectsCompat.equals(type, video.type);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(id, key, name, site, type);
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }
}
