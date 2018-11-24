package com.example.android.moviestwo.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class MovieJsonUtils {
//    private static final String TAG = MovieJsonUtils.class.getSimpleName();

    /**
     * This method parses JSON from a web response and returns an 2d array of Strings
     * <p/>
     *
     * @param movieJsonStr JSON response from server
     * @return Array of Strings describing movie data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static String[][] getMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        // In TMDB's returned json data, all info is an element of the "results" array
        final String TMBD_LIST = "results";

        // keynames used in TMDB's json data
        final String TMBD_ID = "id";
        final String TMBD_TITLE = "title";
        final String TMBD_POSTER = "poster_path";
        final String TMBD_OVERVIEW = "overview";
        final String TMBD_RATING = "vote_average";
        final String TMBD_DATE = "release_date";
        final String TMBD_MESSAGE_CODE = "cod";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(TMBD_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(TMBD_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(TMBD_LIST);

        /* Two dimensional String array to hold each movie's parsed attributes */
        String parsedMovieData[][];
        parsedMovieData = new String[movieArray.length()][6];

        for (int i = 0; i < movieArray.length(); i++) {
            String movieId;
            String title;
            String posterUrl;
            String synopsis;
            String rating;
            String releaseDate;

            /* Get the JSON object representing the individual movie */
            JSONObject movieObject = movieArray.getJSONObject(i);

            movieId = movieObject.getString(TMBD_ID);
            title = movieObject.getString(TMBD_TITLE);
            posterUrl = movieObject.getString(TMBD_POSTER);
            synopsis = movieObject.getString(TMBD_OVERVIEW);
            rating = movieObject.getString(TMBD_RATING);
            releaseDate = movieObject.getString(TMBD_DATE);

            parsedMovieData[i][0] = movieId;
            parsedMovieData[i][1] = title;
            parsedMovieData[i][2] = posterUrl;
            parsedMovieData[i][3] = synopsis;
            parsedMovieData[i][4] = rating;
            parsedMovieData[i][5] = releaseDate;
        }

        return parsedMovieData;
    }

    public static String[][] getReviewStringsFromJson(Context context, String reviewJsonStr)
            throws JSONException {

        // In TMDB's returned json data, all info is an element of the "results" array
        final String TMBD_LIST = "results";

        // keynames used in TMDB's json data
        final String TMBD_REVIEWID = "id";
        final String TMBD_REVIEWAUTHOR = "author";
        final String TMBD_REVIEWCONTENT = "content";
        final String TMBD_REVIEWURL = "url";
        final String TMBD_MESSAGE_CODE = "cod";

        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        /* Is there an error? */
        if (reviewJson.has(TMBD_MESSAGE_CODE)) {
            int errorCode = reviewJson.getInt(TMBD_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray reviewArray = reviewJson.getJSONArray(TMBD_LIST);

        /* Two dimensional String array to hold each review's parsed attributes */
        String parsedReviewData[][];
        parsedReviewData = new String[reviewArray.length()][4];

        for (int i = 0; i < reviewArray.length(); i++) {
            String reviewId;
            String author;
            String content;
            String url;

            /* Get the JSON object representing the individual review */
            JSONObject reviewObject = reviewArray.getJSONObject(i);
            reviewId = reviewObject.getString(TMBD_REVIEWID);
            author = reviewObject.getString(TMBD_REVIEWAUTHOR);
            content = reviewObject.getString(TMBD_REVIEWCONTENT);
            url = reviewObject.getString(TMBD_REVIEWURL);

            parsedReviewData[i][0] = reviewId;
            parsedReviewData[i][1] = author;
            parsedReviewData[i][2] = content;
            parsedReviewData[i][3] = url;

//            Log.d(TAG, "getReviewStringsFromJson: id " + parsedReviewData[i][0]);
//            Log.d(TAG, "getReviewStringsFromJson: au " + parsedReviewData[i][1]);
//            Log.d(TAG, "getReviewStringsFromJson: cn " + parsedReviewData[i][2]);
//            Log.d(TAG, "getReviewStringsFromJson: ur " + parsedReviewData[i][3]);

        }
        return parsedReviewData;
    }

    public static String[][] getVideoStringsFromJson(Context context, String videoJsonStr)
            throws JSONException {

        // In TMDB's returned json data, all info is an element of the "results" array
        final String TMBD_LIST = "results";

        // keynames used in TMDB's json data
        final String TMBD_VIDEOKEY = "key";
        final String TMBD_VIDEOTYPE = "type";
        final String TMBD_VIDEONAME = "name";
        final String TMBD_MESSAGE_CODE = "cod";

        JSONObject videoJson = new JSONObject(videoJsonStr);

        /* Is there an error? */
        if (videoJson.has(TMBD_MESSAGE_CODE)) {
            int errorCode = videoJson.getInt(TMBD_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray videoArray = videoJson.getJSONArray(TMBD_LIST);

        /* Two dimensional String array to hold each video's parsed attributes */
        String parsedVideoData[][];
        parsedVideoData = new String[videoArray.length()][3];

        for (int i = 0; i < videoArray.length(); i++) {
            String ytVideoKey;
            String type;
            String title;

            /* Get the JSON object representing the individual video */
            JSONObject videoObject = videoArray.getJSONObject(i);
            ytVideoKey = videoObject.getString(TMBD_VIDEOKEY);
            type = videoObject.getString(TMBD_VIDEOTYPE);
            title = videoObject.getString(TMBD_VIDEONAME);

            parsedVideoData[i][0] = ytVideoKey;
            parsedVideoData[i][1] = type;
            parsedVideoData[i][2] = title;

//            Log.d(TAG, "getVideoStringsFromJson: id 0 " + parsedVideoData[i][0]);
//            Log.d(TAG, "getVideoStringsFromJson: au 1 " + parsedVideoData[i][1]);
//            Log.d(TAG, "getVideoStringsFromJson: cn 2 " + parsedVideoData[i][2]);

        }
        return parsedVideoData;
    }
}
