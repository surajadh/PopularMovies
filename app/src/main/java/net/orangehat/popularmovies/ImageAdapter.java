package net.orangehat.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surajadhikari on 8/16/15.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieDto> movies = new ArrayList<>();

    public ImageAdapter(Context c, List<MovieDto> movieDtos) {
        mContext = c;
        movies = movieDtos;
    }


    @Override
    public int getCount() {

        return movies.size();
    }

    @Override
    public MovieDto getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        if(imageView == null){
            imageView = new ImageView(mContext);
        }

        String url = getMovieUrl(getItem(position));

        Picasso.with(mContext) //
                .load(url) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(mContext) //
                .into(imageView);

      return imageView;
    }

    private String getMovieUrl(MovieDto movieDto){
        return movieDto.getMoviePosterUrl();
    }
}
