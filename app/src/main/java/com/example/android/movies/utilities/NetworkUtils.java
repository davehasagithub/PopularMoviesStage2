package com.example.android.movies.utilities;

import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

import com.example.android.movies.data.struct.SortType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_DEFAULT_POSTER_SIZE = "w342";
    private static final String TMDB_IMAGE_URL_PREFIX = "http://image.tmdb.org/t/p/";
    private static final String TMDB_ENDPOINT_PREFIX = "http://api.themoviedb.org/3/movie";
    private static final String POPULAR_MOVIES_ENDPOINT = "/popular";
    private static final String TOP_RATED_MOVIES_ENDPOINT = "/top_rated";
    private static final String VIDEOS_MOVIES_ENDPOINT = "/videos";
    private static final String REVIEWS_MOVIES_ENDPOINT = "/reviews";

    public static Uri getMoviePosterUri(String path) {
        return Uri.parse(NetworkUtils.TMDB_IMAGE_URL_PREFIX + NetworkUtils.TMDB_DEFAULT_POSTER_SIZE + path);
    }

    public static URL buildMoviesUrl(SortType sortType, String apiKey) {
        return buildURLWithApiKey(sortType == SortType.MOST_POPULAR ? NetworkUtils.POPULAR_MOVIES_ENDPOINT : NetworkUtils.TOP_RATED_MOVIES_ENDPOINT, apiKey);
    }

    public static URL buildReviewsUrl(String apiKey, int movieId) {
        return buildURLWithApiKey("/" + movieId + REVIEWS_MOVIES_ENDPOINT, apiKey);
    }

    public static URL buildVideosUrl(String apiKey, int movieId) {
        return buildURLWithApiKey("/" + movieId + VIDEOS_MOVIES_ENDPOINT, apiKey);
    }

    private static URL buildURLWithApiKey(String endpoint, String apiKey) {
        Builder builder = Uri.parse(NetworkUtils.TMDB_ENDPOINT_PREFIX + endpoint)
                .buildUpon()
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("page", "1");

        Uri builtUri = builder.build();

        URL url = null;
        try {
            String uriString = builtUri.toString();
            url = new URL(uriString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(NetworkUtils.TAG, "Built URL: " + url);

        return url;
    }

    // Borrowed from Udacity exercises
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setUseCaches(false);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}