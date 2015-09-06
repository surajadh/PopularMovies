package net.orangehat.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MovieGrid extends AppCompatActivity implements OnFetchCompleted {

    private FetchMoviesTask fetchMoviesTask;
    private List<MovieDto> movieDtoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        if(savedInstanceState == null || !savedInstanceState.containsKey("fetchedMovies")) {
            if(isNetworkAvailable()){
                fetchMoviesTask = new FetchMoviesTask(this, Constants.BYPOPULARITY);
                fetchMoviesTask.execute();
            }
        }
        else{
            movieDtoList = savedInstanceState.getParcelableArrayList("fetchedMovies");
            onTaskCompleted(movieDtoList);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("fetchedMovies", (ArrayList<MovieDto>) movieDtoList);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String changeSortingTo;
        int id = item.getItemId();

        if(id == R.id.action_settings_sortbyrating)
            changeSortingTo = Constants.BYREVENUE;
        else
            changeSortingTo = Constants.BYPOPULARITY;

        if(fetchMoviesTask != null){
            if(changeSortingTo != fetchMoviesTask.getCurrentSortBy()){
                fetchMoviesTask = new FetchMoviesTask(this, changeSortingTo);
                fetchMoviesTask.execute();
            }
        }
        else{
            fetchMoviesTask = new FetchMoviesTask(this, changeSortingTo);
            fetchMoviesTask.execute();
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(List<MovieDto> movieDtos) {
        movieDtoList = movieDtos;
        ImageAdapter imageAdapter = new ImageAdapter(this, movieDtoList);
        GridView gridView = (GridView) findViewById(R.id.movieView);
        gridView.setAdapter(imageAdapter);
        final MovieGrid thisActivity = this;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(thisActivity, DetailViewActivity.class).putExtra(MovieDto.class.getName(), (Serializable) movieDtoList.get(position));
                startActivity(intent);
            }
        });

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
