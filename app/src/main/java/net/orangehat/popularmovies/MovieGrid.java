package net.orangehat.popularmovies;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


public class MovieGrid extends AppCompatActivity {

    private FetchMoviesTask fetchMoviesTask;

    public enum SortBy {popularity, revenue}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        fetchMoviesTask = new FetchMoviesTask(this, Constants.BYPOPULARITY);
        fetchMoviesTask.execute();
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

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
