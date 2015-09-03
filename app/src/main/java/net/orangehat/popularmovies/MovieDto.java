package net.orangehat.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by surajadhikari on 8/24/15.
 */
@SuppressWarnings("serial")
public class MovieDto implements Serializable, Parcelable {
    private String moviePosterUrl;
    private String originalTitle;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;

    private MovieDto(Parcel in) {
        moviePosterUrl = in.readString();
        originalTitle = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    public MovieDto() {

    }

    public MovieDto(MovieDto movieDto) {
        moviePosterUrl = movieDto.getMoviePosterUrl();
        originalTitle = movieDto.getOriginalTitle();
        plotSynopsis = movieDto.getPlotSynopsis();
        userRating = movieDto.getUserRating();
        releaseDate = movieDto.getReleaseDate();
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(moviePosterUrl);
        dest.writeString(originalTitle);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieDto> CREATOR = new Parcelable.Creator<MovieDto>(){

        @Override
        public MovieDto createFromParcel(Parcel source) {
            return new MovieDto(source);
        }

        @Override
        public MovieDto[] newArray(int size) {
            return new MovieDto[size];
        }
    };
}
