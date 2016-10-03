package com.time.show.model;

import com.google.gson.annotations.SerializedName;

/**
 * Base Model for discovering media ( i.e. Movies / TV Shows)
 *
 * Abstract so it is not implemented
 *
 * @author aleksandrTmk
 */
public abstract class DiscoverMedia {
    private int page;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int total_pages) {
        this.totalPages = total_pages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int total_results) {
        this.totalResults = total_results;
    }
}
