package net.orangehat.popularmovies;

import java.util.List;

/**
 * Created by surajadhikari on 9/1/15.
 */
public interface OnFetchCompleted {
    void onTaskCompleted(List<MovieDto> movieDtos);
}
