package com.time.show.util;

/**
 * Class to hold all app related constants
 *
 * @author aleksandrTmk
 */
public class Constants {

    /**
     * Keys to retrieve an extra from an intent bundle
     */
    public static final String BUNDLE_EXTRA_MOVIE = "bundle-extra-movie";
    public static final String BUNDLE_EXTRA_TVSHOW = "bundle-extra-tvshow";
    public static final String BUNDLE_EXTRA_IMAGE_BASE_URL = "bundle-extra-image-base-url";
    public static final String BUNDLE_EXTRA_SAVED_APP_STATE = "bundle-extra-saved-app-state";


    //region Constants screen densities
    public static final int SCREEN_DENSITY_LDPI = 120;
    public static final int SCREEN_DENSITY_MDPI = 160;
    public static final int SCREEN_DENSITY_HDPI = 240;
    public static final int SCREEN_DENSITY_XHDPI = 480;
    public static final int SCREEN_DENSITY_XXHDPI = 640;

    /**
     * Default density used for screen DPI calculations when one is not available
     */
    public static final int SCREEN_DENSITY_DEFAULT = SCREEN_DENSITY_XHDPI;
    //endregion

}
