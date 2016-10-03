package com.time.show.model;

/**
 * Model for discovering tv shows from API server
 *
 * @author aleksandrTmk
 */
public class DiscoverTvShows extends DiscoverMedia {
    private TvShow[] results;

    public TvShow[] getResults() {
        return results;
    }

    public void setResults(TvShow[] results) {
        this.results = results;
    }
}
