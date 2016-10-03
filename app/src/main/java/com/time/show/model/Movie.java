package com.time.show.model;

/**
 * Model to represent a movie object returned by API
 *
 * @author aleksandrTmk
 */
public class Movie extends Media {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
