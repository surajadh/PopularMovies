package net.orangehat.popularmovies;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailViewActivityFragment extends Fragment {

    public DetailViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailViewFragment = inflater.inflate(R.layout.fragment_detail_view, container, false);

        Intent i = getActivity().getIntent();
        MovieDto movieDto = (MovieDto) i.getSerializableExtra(MovieDto.class.getName());

        TextView titleTextView = (TextView) detailViewFragment.findViewById(R.id.title_text);
        titleTextView.setText(movieDto.getOriginalTitle());

        TextView ratingTextView = (TextView) detailViewFragment.findViewById(R.id.rating_text);
        ratingTextView.setText(movieDto.getUserRating());

        TextView synopsisTextView = (TextView) detailViewFragment.findViewById(R.id.synopsis_text);
        synopsisTextView.setText(movieDto.getPlotSynopsis());

        TextView releaseDateView = (TextView) detailViewFragment.findViewById(R.id.releaseDate_text);
        releaseDateView.setText(movieDto.getReleaseDate());

        ImageView imageView = (ImageView) detailViewFragment.findViewById(R.id.moviePosterView);

        DetailViewActivity detailViewActivity = (DetailViewActivity) this.getActivity();

        Picasso.with(detailViewActivity) //
                .load(movieDto.getMoviePosterUrl()) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(detailViewActivity) //
                .into(imageView);

        return detailViewFragment;
    }
}
