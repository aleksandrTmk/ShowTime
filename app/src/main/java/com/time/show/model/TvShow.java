package com.time.show.model;

/**
 * Model for a Tv Show object returned by API
 *
 * @author aleksandrTmk
 */
public class TvShow extends Media {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
