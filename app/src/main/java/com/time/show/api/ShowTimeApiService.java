package com.time.show.api;

import com.time.show.BuildConfig;
import com.time.show.model.Configuration;
import com.time.show.model.DiscoverMovies;
import com.time.show.model.DiscoverTvShows;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Service interface to define all API methods
 *
 * Proper query format:
 * http://api.themoviedb.org/3/discover/movie?api_key=9c112d12141544324157b2b1e1335d68&page=2&sort_by=popularity.desc
 *
 * @author aleksandrTmk
 */
public interface ShowTimeApiService {
    /**
     * Base url of themoviedb.org API service
     */
    String API_BASE_URL = "http://api.themoviedb.org/3/";
    /**
     * String for api_key header
     */
    String API_HEADER_KEY = "api_key";
    /**
     * Api authorization key for themoviedb.org
     */
    String API_AUTH_KEY = BuildConfig.API_KEY;

    /**
     * Api endpoints
     */
    String API_DISCOVER = "discover";
    String API_DISCOVER_MOVIE = "/movie";
    String API_DISCOVER_TV_SHOW = "/tv";
    String API_CONFIGURATION = "configuration";

    /**
     * Sorting query
     */
    String QUERY_SORT_BY = "sort_by";
    String QUERY_SORT_BY_POPULAR_DESC = "popularity.desc";
    /**
     * Page query
     */
    String QUERY_PAGE = "page";

    /**
     * Maximum query page as defined by the API spec
     */
    int MAXIMUM_PAGE = 10000;
    /**
     * Minimum query page as defined by the API spec
     */
    int MINIMUM_PAGE = 1;


    /**
     * Returns movies
     *
     * @param options Map of options such as sort and page number
     */
    @GET(API_DISCOVER + API_DISCOVER_MOVIE)
    Call<DiscoverMovies> getPopularMovies(@QueryMap Map<String, String> options);


    /**
     * Returns tv shows
     *
     * @param options Map of options such as sort and page number
     */
    @GET(API_DISCOVER + API_DISCOVER_TV_SHOW)
    Call<DiscoverTvShows> getPopularTvShows(@QueryMap Map<String, String> options);

    /**
     * Returns the configuration for media
     *
     * @param authKey Api auth key
     */
    @GET(API_CONFIGURATION)
    Call<Configuration> getConfiguration(@Query(API_HEADER_KEY) String authKey);
}
