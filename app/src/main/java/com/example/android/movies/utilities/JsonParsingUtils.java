package com.example.android.movies.utilities;

import com.example.android.movies.data.struct.Movie;
import com.example.android.movies.data.struct.Review;
import com.example.android.movies.data.struct.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParsingUtils {
    private static final String JSON_KEY_MOVIE_RESULTS = "results";
    private static final String JSON_KEY_MOVIE_POSTER_PATH = "poster_path";
    private static final String JSON_KEY_MOVIE_OVERVIEW = "overview";
    private static final String JSON_KEY_MOVIE_RELEASE_DATE = "release_date";
    private static final String JSON_KEY_MOVIE_ID = "id";
    private static final String JSON_KEY_MOVIE_ORIGINAL_TITLE = "original_title";
    private static final String JSON_KEY_MOVIE_TITLE = "title";
    private static final String JSON_KEY_MOVIE_VOTE_AVERAGE = "vote_average";

    private static final String JSON_KEY_REVIEW_RESULTS = "results";
    private static final String JSON_KEY_REVIEW_ID = "id";
    private static final String JSON_KEY_REVIEW_AUTHOR = "author";
    private static final String JSON_KEY_REVIEW_CONTENT = "content";

    private static final String JSON_KEY_VIDEO_RESULTS = "results";
    private static final String JSON_KEY_VIDEO_ID = "id";
    private static final String JSON_KEY_VIDEO_KEY = "key";
    private static final String JSON_KEY_VIDEO_NAME = "name";
    private static final String JSON_KEY_VIDEO_SITE = "site";
    private static final String JSON_KEY_VIDEO_TYPE = "type";

    public static ArrayList<Movie> getMoviesFromJson(String jsonString) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.optJSONArray(JsonParsingUtils.JSON_KEY_MOVIE_RESULTS);
            if (resultsArray != null) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    String posterPath = resultsArray.getJSONObject(i).optString(JsonParsingUtils.JSON_KEY_MOVIE_POSTER_PATH);
                    String overview = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_MOVIE_OVERVIEW);
                    String releaseDate = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_MOVIE_RELEASE_DATE);
                    int id = resultsArray.getJSONObject(i).getInt(JsonParsingUtils.JSON_KEY_MOVIE_ID);
                    String originalTitle = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_MOVIE_ORIGINAL_TITLE);
                    String title = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_MOVIE_TITLE);
                    Double voteAverage = resultsArray.getJSONObject(i).getDouble(JsonParsingUtils.JSON_KEY_MOVIE_VOTE_AVERAGE);

                    Movie movie = new Movie(id, posterPath, overview, releaseDate, originalTitle, title, voteAverage);
                    movies.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static ArrayList<Review> getReviewsFromJson(String jsonString) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.optJSONArray(JsonParsingUtils.JSON_KEY_REVIEW_RESULTS);
            if (resultsArray != null) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    String id = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_REVIEW_ID);
                    String author = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_REVIEW_AUTHOR);
                    String content = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_REVIEW_CONTENT);

                    Review review = new Review(id, author, content);
                    reviews.add(review);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static ArrayList<Video> getVideosFromJson(String jsonString) {
        ArrayList<Video> videos = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.optJSONArray(JsonParsingUtils.JSON_KEY_VIDEO_RESULTS);
            if (resultsArray != null) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    String id = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_VIDEO_ID);
                    String key = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_VIDEO_KEY);
                    String name = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_VIDEO_NAME);
                    String site = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_VIDEO_SITE);
                    String type = resultsArray.getJSONObject(i).getString(JsonParsingUtils.JSON_KEY_VIDEO_TYPE);

                    Video video = new Video(id, key, name, site, type);
                    videos.add(video);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videos;
    }
}
