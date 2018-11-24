package com.example.android.moviestwo.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.moviestwo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";

    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MV_MOVIEID = "movieId";
        public static final String COLUMN_MV_TITLE = "title";
        public static final String COLUMN_MV_POSTERURL = "posterUrl";
        public static final String COLUMN_MV_SYNOPSIS = "synopsis";
        public static final String COLUMN_MV_RATING = "rating";
        public static final String COLUMN_MV_RELEASEDATE = "releaseDate";

    }

    public static final class ReviewsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_RV_MOVIEID = "movieId";
        public static final String COLUMN_RV_REVIEWID = "reviewId";
        public static final String COLUMN_RV_AUTHOR = "author";
        public static final String COLUMN_RV_CONTENT = "content";
        public static final String COLUMN_RV_URL = "url";

    }

    public static final class VideosEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEOS)
                .build();
        public static final String TABLE_NAME = "videos";

        public static final String COLUMN_VD_MOVIEID = "movieId";
        public static final String COLUMN_VD_YTVIDEOKEY = "ytVideoKey";
        public static final String COLUMN_VD_TYPE = "type";
        public static final String COLUMN_VD_TITLE = "title";

    }

}
