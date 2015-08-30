package net.orangehat.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by surajadhikari on 8/22/15.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, List<MovieDto>> {

    private MovieGrid movieGrid;
    private String sortBy;

    public FetchMoviesTask(MovieGrid movieGridActivity, String sortRequirement) {
        movieGrid = movieGridActivity;
//        sortBy = (sortRequirement == null) : R.string.popularity ? sortRequirement;
        if(sortRequirement == null){
            sortBy = Constants.BYPOPULARITY;
        }
        sortBy = sortRequirement;
    }

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


    private List<MovieDto> getMovies(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(Constants.TMDB_RESULTS);

        List<MovieDto> movies = new ArrayList<>();
        for(int i = 0; i < movieArray.length(); i++) {

            String imageUrl;

            JSONObject movie = movieArray.getJSONObject(i);

            MovieDto movieDto = new MovieDto();

            imageUrl = Constants.TMDB_IMAGE_BASEURL + movie.getString(Constants.TMDB_IMAGEPATH);
            movieDto.setMoviePosterUrl(imageUrl);
            movieDto.setOriginalTitle(movie.getString(Constants.TMDB_ORIGINALTITLE));
            movieDto.setPlotSynopsis(movie.getString(Constants.TMDB_OVERVIEW));
            movieDto.setReleaseDate(movie.getString(Constants.TMDB_RELEASEDATE));
            movieDto.setUserRating(movie.getString(Constants.TMDB_AVERAGERATING));
            movies.add(movieDto);
        }

        return movies;

    }

    @Override
    protected List<MovieDto> doInBackground(Void... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;



        try {
            // Construct the URL for the OpenMovie

            final String API_KEY = "api_key";
            final String SORT_BY = "sort_by";

            Uri builtUri = Uri.parse(Constants.MOVIEAPI_BASEURL).buildUpon()
                    .appendQueryParameter(API_KEY, Constants.APIKEY)
                    .appendQueryParameter(SORT_BY, sortBy)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movie string: " + movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovies(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final List<MovieDto> result) {
        if (result != null) {

            //I probably need to find a way to do all this in mainactivity and just update data
            //here but not sure how?
            ImageAdapter imageAdapter = new ImageAdapter(movieGrid, result);
            imageAdapter.notifyDataSetChanged();
            GridView gridView = (GridView) movieGrid.findViewById(R.id.movieView);
            gridView.setAdapter(imageAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(movieGrid, DetailViewActivity.class).putExtra(MovieDto.class.getName(), result.get(position));
                    movieGrid.startActivity(intent);

                    //Intent intent = new Intent(this, DetailViewActivity.class); //this is not an activity
                }
            });
        }
    }

    protected String getCurrentSortBy(){
        return sortBy;
    }

}
