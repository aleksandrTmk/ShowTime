package com.time.show.model;

/**
 * Model for discovering movies from API server
 *
 * @author aleksandrTmk
 */
public class DiscoverMovies extends DiscoverMedia {
    private Movie[] results;

    public Movie[] getResults() {
        return results;
    }

    public void setResults(Movie[] results) {
        this.results = results;
    }
}
