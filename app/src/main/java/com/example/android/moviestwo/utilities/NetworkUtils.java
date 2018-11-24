package com.example.android.moviestwo.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    //    Working with the themoviedb.org API
    //    request data from the /movie/popular and /movie/top_rated endpoints
    //    URL parameter like so: http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";
    private static final String REVIEWS_PARAM = "reviews";
    private static final String VIDEOS_PARAM = "videos";
    private NetworkUtils() { }

    //    Working with the themoviedb.org API
    //    request data from the now_playing, popular, top_rated, and upcoming endpoints URL  like so:
    //     https://api.themoviedb.org/3/movie/now_playing?api_key=[YOUR_API_KEY]
    //     https://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
    //     https://api.themoviedb.org/3/movie/top_rated?api_key=[YOUR_API_KEY]
    //     https://api.themoviedb.org/3/movie/upcoming?api_key=[YOUR_API_KEY]
    public static URL buildMainUrl(String sortOrder, String apiKey) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //    Working with the themoviedb.org API
    //    request data from the /movie/#/reviews/ endpoint URL like so:
    //     https://api.themoviedb.org/3/movie/269149/reviews?api_key=[YOUR_API_KEY]
    public static URL buildReviewsUrl(String movieID, String apiKey) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(movieID)
                .appendPath(REVIEWS_PARAM)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //    Working with the themoviedb.org API
    //    request data from the /movie/#/videos endpoint URL like so:
    //     https://api.themoviedb.org/3/movie/269149/videos?api_key=[YOUR_API_KEY]
    public static URL buildVideosUrl(String movieID, String apiKey) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(movieID)
                .appendPath(VIDEOS_PARAM)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try (InputStream in = urlConnection.getInputStream()) {
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
