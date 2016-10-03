package com.time.show.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for a media result. Can be used by both Movies and Tv Show results.
 *
 * @author aleksandrTmk
 */
public class Media {
    /**
     * Defines the types of media users can see.
     */
    public enum TYPE {MOVIE, TV_SHOW}

    @SerializedName("poster_path")
    private String posterPath;
    private String overview;
    private Double popularity;
    private int id;

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
