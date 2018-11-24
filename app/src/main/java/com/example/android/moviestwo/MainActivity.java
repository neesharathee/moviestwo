package com.example.android.moviestwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.moviestwo.MovieListRecyclerAdapter.ListItemClickListener;
import com.example.android.moviestwo.data.FavoritesContract;
import com.example.android.moviestwo.utilities.MovieJsonUtils;
import com.example.android.moviestwo.utilities.NetworkUtils;

import java.net.URL;

// Implement MovieListRecyclerAdapter.ListItemClickListener from the MainActivity
public class MainActivity extends AppCompatActivity
        implements ListItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MovieListRecyclerAdapter mAdapter;
    private RecyclerView mMoviesListRecView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private Parcelable savedRecyclerLayoutState;
    private GridLayoutManager layoutManager;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        float dens = getResources().getDisplayMetrics().density;
        // Note, screenHeightDp isn't reliable
        // (it seems to be too small by the height of the status bar),
        // but we assume screenWidthDp is reliable.
        int screenWidthDp = config.screenWidthDp;
        Log.d(TAG, "screenWidthDp: " + screenWidthDp);
        int screenWidthPx = (int) (screenWidthDp * dens);
        Log.d(TAG, "screenWidthPx: " + screenWidthPx);
//        int screenHeightPx = (int) (config.screenHeightDp * dens);
        int itemWidthPx = (int) (getResources().getDimension(R.dimen.movie_tile_width));
        // to get minColumnWidthPx, add poster size (itemWidthPx) from dimens file
        // to minMarginWidthPx of 6dp (*2 because left and right margins)
        int minMarginWidthPx = (int) (6 * dens);
        int minColumnWidthPx = itemWidthPx + (2 * minMarginWidthPx);
        int numberOfColumns = (screenWidthPx / minColumnWidthPx) + 1;
        int movieItemMarginPx = 0;
//        Log.d(TAG, "itemWidthPx: " + itemWidthPx);
//        Log.d(TAG, "minMarginWidthPx: " + minMarginWidthPx);
//        Log.d(TAG, "minColumnWidthPx: " + minColumnWidthPx);
//        Log.d(TAG, "numberOfColumns: " + numberOfColumns);

        while (movieItemMarginPx < minMarginWidthPx) {

            numberOfColumns--;
            movieItemMarginPx = (screenWidthPx - (itemWidthPx * numberOfColumns)) / ((numberOfColumns) * 2);

        }
        int vMarginInPx = (int) (12 * dens);
//        Log.d(TAG, "movieItemMarginPx: " + movieItemMarginPx);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mMoviesListRecView = findViewById(R.id.rv_posters);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        // Set margins on recycle view elements to spread out the gaps evenly
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) mMoviesListRecView.getLayoutParams();
        marginLayoutParams.setMargins(movieItemMarginPx, vMarginInPx, movieItemMarginPx, vMarginInPx);
        mMoviesListRecView.setLayoutParams(marginLayoutParams);

        layoutManager = new GridLayoutManager(this, numberOfColumns);
        mMoviesListRecView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mMoviesListRecView.setHasFixedSize(true);

        // Pass in this as the ListItemClickListener to the MovieListRecyclerAdapter constructor
        /*
         * The MovieListRecyclerAdapter is responsible for displaying each item in the list.
         */
        mAdapter = new MovieListRecyclerAdapter(this);
        mMoviesListRecView.setAdapter(mAdapter);

        // "progress bar" circle
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String savedPrefSortOder =
                sharedPreferences.getString(getResources().getString(R.string.pref_sort_key),
                        getResources().getString(R.string.pref_sort_default));

        loadMoviesData(savedPrefSortOder);
    }

    /**
     * Query the Content Provider
     * @return Cursor containing the list of favorite moview
     */
    private Cursor getFavoriteMovies() {
        // query content provider for whole favorites table
        return getContentResolver().query(
                FavoritesContract.FavoritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            String savedPrefSortOder =
                    sharedPreferences.getString(getResources().getString(R.string.pref_sort_key),
                            getResources().getString(R.string.pref_sort_default));
            loadMoviesData(savedPrefSortOder);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                layoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore recycler view at same position
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String savedPrefSortOder =
                sharedPreferences.getString(getResources().getString(R.string.pref_sort_key),
                        getResources().getString(R.string.pref_sort_default));
        loadMoviesData(savedPrefSortOder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This method gets the movie data in the background.
     */
    private void loadMoviesData(String sortOrder) {
        showMoviesDataView();
        new FetchMoviesTask().execute(sortOrder);
    }

    // Override ListItemClickListener's onListItemClick method
    /**
     * This is where we receive our callback from
     * {@link com.example.android.moviestwo.MovieListRecyclerAdapter.ListItemClickListener}
     * <p>
     * This callback is invoked when you click on an item in the list.
     *
     * @param movieInfo Index in the array of movie info for the item that was clicked.
     */
    @Override
    public void onListItemClick(String[] movieInfo) {
        Context context = this;

        // Now that I've added the parselables, can I use them here? Not completely sure how they work just yet.
        String movieId = movieInfo[0];
        String title = movieInfo[1];
        String posterUrl = movieInfo[2];
        String synopsis = movieInfo[3];
        String rating = movieInfo[4];
        String releaseDate = movieInfo[5];

        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("movieentry",
                new MovieEntry(movieId, title, posterUrl, synopsis , rating, releaseDate));
        startActivity(intentToStartDetailActivity);
    }

    // Make the View for the movie data visible and hide the error message.
    private void showMoviesDataView() {
        /* hide the error message text view */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* show the movie data is recycler view */
        mMoviesListRecView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* hide  the movie data is recycler view */
        mMoviesListRecView.setVisibility(View.INVISIBLE);
        /* show the error message text view */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_sort:
                // Pass in this as the ListItemClickListener to the MovieListRecyclerAdapter constructor
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, String[][]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground(String... params) {

            String apiKey = BuildConfig.TMDB_API_KEY;
            String sortMethod = params[0];
//            Log.d(TAG, "doInBackground: sortMethod " + sortMethod);
            // IF THE SORT METHOD IS FAVORITES, GET INFO FROM CONTENT PROVIDER
            if ("favorites".equals(sortMethod)) {

                int cPos;
                Cursor cursor = getFavoriteMovies();
                if (cursor.getCount() > 0) { // If cursor has atleast one row
                    String[][] dbMoviesData = new String[cursor.getCount()][6];
//                    Log.d(TAG, "doInBackground: cursor.getCount() " + cursor.getCount());

                    cursor.moveToFirst();
                    do {
                        cPos = cursor.getPosition();
//                        Log.d(TAG, "doInBackground: cPos " + cPos);
//                        Log.d(TAG, "doInBackground: dbMovieData[cPos][0] " + dbMoviesData[cPos][0]);
//                        Log.d(TAG, "doInBackground: dbMovieData[cPos][1] " + dbMoviesData[cPos][1]);
//                        Log.d(TAG, "doInBackground: dbMovieData[cPos][2] " + dbMoviesData[cPos][2]);
//                        Log.d(TAG, "doInBackground: dbMovieData[cPos][3] " + dbMoviesData[cPos][3]);
//                        Log.d(TAG, "doInBackground: dbMovieData[cPos][4] " + dbMoviesData[cPos][4]);
//                        Log.d(TAG, "doInBackground: dbMovieData[cPos][5] " + dbMoviesData[cPos][5]);
                        dbMoviesData[cPos][0] = cursor.getString(cursor.getColumnIndex("movieId"));
                        dbMoviesData[cPos][1] = cursor.getString(cursor.getColumnIndex("title"));
                        dbMoviesData[cPos][2] = cursor.getString(cursor.getColumnIndex("posterUrl"));
                        dbMoviesData[cPos][3] = cursor.getString(cursor.getColumnIndex("synopsis"));
                        dbMoviesData[cPos][4] = cursor.getString(cursor.getColumnIndex("rating"));
                        dbMoviesData[cPos][5] = cursor.getString(cursor.getColumnIndex("releaseDate"));
                        cursor.moveToNext();
                    } while (!cursor.isAfterLast());

                    return dbMoviesData;

                } else {
                    String[][] phMoviesData = new String[1][6]; // Dynamic string array

                    Log.e("FetchMovies: Content Provider", "Cursor has no data");
//                    phMoviesData[0][0] = null;
//                    phMoviesData[0][1] = getString(R.string.no_favorites_indb_message);
//                    phMoviesData[0][2] = null;
//                    phMoviesData[0][3] = getString(R.string.no_favs_placeholder_message);
//                    phMoviesData[0][4] = null;
//                    phMoviesData[0][5] = null;
//                    return phMoviesData;
                    return null;

                }
            } else {
                URL MoviesRequestUrl = NetworkUtils.buildMainUrl(sortMethod, apiKey);
                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(MoviesRequestUrl);
                    String[][] JsonMoviesData = MovieJsonUtils
                            .getMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                    return JsonMoviesData;

                } catch (Exception e) {
                    Log.e("FetchMovies jsonfailed", "No results from MoviesRequestUrl" + MoviesRequestUrl);
                    e.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(String[][] moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesDataView();
                mAdapter.setMovieData(moviesData);
            } else {
                showErrorMessage();
            }
            if (savedRecyclerLayoutState!=null) {
                layoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }
    }
}