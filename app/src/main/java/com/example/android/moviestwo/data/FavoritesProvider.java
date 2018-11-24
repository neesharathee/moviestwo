package com.example.android.moviestwo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class FavoritesProvider extends ContentProvider {

    private static final int CODE_FAVORITES = 100;
    private static final int CODE_FAVORITE_BY_MOVIEID = 101;
    private static final int CODE_REVIEWS = 200;
    private static final int CODE_REVIEW_BY_MOVIEID = 201;
    private static final int CODE_VIDEOS = 300;
    private static final int CODE_VIDEO_BY_MOVIEID = 301;

    /*
     * leading "s" in this variable name means the UriMatcher is static member variable of FavoritesProvider
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Creates the UriMatcher that will match each URI to the CODE_FAVORITES and
     * CODE_FAVORITE_BY_MOVIEID constants defined above.
     *
     * @return A UriMatcher that correctly matches the constants for CODE_FAVORITES and CODE_FAVORITE_BY_MOVIEID
     */
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoritesContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, FavoritesContract.PATH_FAVORITES + "/*", CODE_FAVORITE_BY_MOVIEID);
        matcher.addURI(authority, FavoritesContract.PATH_REVIEWS, CODE_REVIEWS);
        matcher.addURI(authority, FavoritesContract.PATH_REVIEWS + "/*", CODE_REVIEW_BY_MOVIEID);
        matcher.addURI(authority, FavoritesContract.PATH_VIDEOS, CODE_VIDEOS);
        matcher.addURI(authority, FavoritesContract.PATH_VIDEOS + "/*", CODE_VIDEO_BY_MOVIEID);
        return matcher;
    }

    private FavoritesDbHelper mOpenHelper;
    /**
     * In onCreate, initialize content provider on startup. This method is called for all
     * registered content providers on the application main thread at application launch time.
     * It must not perform lengthy operations, or application startup will be delayed.
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        // Within onCreate, instantiate mOpenHelper
        mOpenHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // URI matching code for the favorites/reviews/videos directories
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case CODE_FAVORITES:
                // Inserting values into favorites table
                long fid = db.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, values);
                if ( fid > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoritesEntry.CONTENT_URI, fid);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CODE_REVIEWS:
                // Inserting values into reviews table
                long rid = db.insert(FavoritesContract.ReviewsEntry.TABLE_NAME, null, values);
                if ( rid > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.ReviewsEntry.CONTENT_URI, rid);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CODE_VIDEOS:
                // Inserting values into videos table
                long vid = db.insert(FavoritesContract.VideosEntry.TABLE_NAME, null, values);
                if ( vid > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.VideosEntry.CONTENT_URI, vid);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    /**
     * Cursor object query from db as determined by switch logic below
     *
     * @param uri           The URI to query
     * @param projection    The list of columns to put into the cursor. If null, all columns are
     *                      included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the
     *                      selection.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return A Cursor containing the results of the query. In our implementation,
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        /*
         * switch statement, given a URI, to determine what kind of query to use
         */
        switch (sUriMatcher.match(uri)) {

            // for URI = content://com.example.android.moviestwo/favorites/
            case CODE_FAVORITES: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // for URI like content://com.example.android.moviestwo/favorites/12345
            case CODE_FAVORITE_BY_MOVIEID: {
                String movieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        FavoritesContract.FavoritesEntry.COLUMN_MV_MOVIEID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // for URI = content://com.example.android.moviestwo/favorites/
            case CODE_REVIEWS: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // for URI like content://com.example.android.moviestwo/reviews/12345
            case CODE_REVIEW_BY_MOVIEID: {
                String movieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        FavoritesContract.ReviewsEntry.COLUMN_RV_MOVIEID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            // for URI = content://com.example.android.moviestwo/videos/
            case CODE_VIDEOS: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.VideosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // for URI like content://com.example.android.moviestwo/videos/12345
            case CODE_VIDEO_BY_MOVIEID: {
                String movieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.VideosEntry.TABLE_NAME,
                        projection,
                        FavoritesContract.VideosEntry.COLUMN_VD_MOVIEID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        // Call setNotificationUri on the cursor and then return the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Deletes data at a given URI with optional arguments for more fine tuned deletions.
     *
     * @param uri           The full URI to query
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs Used in conjunction with the selection statement
     * @return The number of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted = 0;

        if (selectionArgs!=null) {
            switch (sUriMatcher.match(uri)) {

                // for URI like content://com.example.android.moviestwo/favorites/12345
                case CODE_FAVORITE_BY_MOVIEID:
//                String movieId = uri.getPathSegments().get(1);
                    numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                            FavoritesContract.FavoritesEntry.TABLE_NAME,
                            FavoritesContract.FavoritesEntry.COLUMN_MV_MOVIEID + "=?",
                            selectionArgs);
                    break;

                // for URI like content://com.example.android.moviestwo/reviews/12345
                case CODE_REVIEW_BY_MOVIEID:
                    numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                            FavoritesContract.ReviewsEntry.TABLE_NAME,
                            FavoritesContract.ReviewsEntry.COLUMN_RV_MOVIEID + "=?",
                            selectionArgs);
                    break;

                // for URI like content://com.example.android.moviestwo/videos/12345
                case CODE_VIDEO_BY_MOVIEID:
                    numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                            FavoritesContract.VideosEntry.TABLE_NAME,
                            FavoritesContract.VideosEntry.COLUMN_VD_MOVIEID + "=?",
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            /* If we actually deleted any rows, notify that a change has occurred to this URI */
            if (numRowsDeleted != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        // Return the number of rows deleted
        return numRowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("not implementing update");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("not implementing getType");
    }

}
