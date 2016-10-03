package com.time.show.model;

import java.util.ArrayList;

/**
 * Model used to save state of application ( during screen rotation )
 *
 * @author aleksandrTmk
 */
public class AppState {
    private int currentMoviePage;
    private int currentTvShowPage;
    private String imageBaseUrl;
    private String[] posterSizes;
    private ArrayList<Movie> movieList;
    private ArrayList<TvShow> tvShowList;

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    public ArrayList<TvShow> getTvShowList() {
        return tvShowList;
    }

    public void setTvShowList(ArrayList<TvShow> tvShowList) {
        this.tvShowList = tvShowList;
    }

    public int getCurrentMoviePage() {
        return currentMoviePage;
    }

    public void setCurrentMoviePage(int currentMoviePage) {
        this.currentMoviePage = currentMoviePage;
    }

    public int getCurrentTvShowPage() {
        return currentTvShowPage;
    }

    public void setCurrentTvShowPage(int currentTvShowPage) {
        this.currentTvShowPage = currentTvShowPage;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    public String[] getPosterSizes() {
        return posterSizes;
    }

    public void setPosterSizes(String[] posterSizes) {
        this.posterSizes = posterSizes;
    }
}
