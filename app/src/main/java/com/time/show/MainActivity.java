package com.time.show;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.time.show.activity.MediaDetailActivity;
import com.time.show.adapter.MediaPageAdapter;
import com.time.show.api.ShowTimeApiService;
import com.time.show.fragment.MediaFragment;
import com.time.show.model.AppState;
import com.time.show.model.Configuration;
import com.time.show.model.DiscoverMovies;
import com.time.show.model.DiscoverTvShows;
import com.time.show.model.Media;
import com.time.show.model.Movie;
import com.time.show.model.TvShow;
import com.time.show.util.Blog;
import com.time.show.util.Constants;
import com.time.show.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MediaFragment.MediaFragmentInterface, MediaPageAdapter.OnFragmentStateListener{
    @BindView(R.id.container) ViewPager viewPager;

    private int currentPageMovie = ShowTimeApiService.MINIMUM_PAGE;
    private int currentPageTvShow = ShowTimeApiService.MINIMUM_PAGE;
    private String mediaImageBaseUrl;
    private String[] mediaImagePosterSizes;

    private Gson gson;
    private MediaPageAdapter mediaPageAdapter;
    private Map<String, String> apiQueryOptions;
    private ShowTimeApiService showTimeApiService;
    private ArrayList<Movie> movieListCurrentPage;
    private ArrayList<TvShow> tvShowsListCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        gson = getGson();
        setupViewPager();

        if (savedInstanceState == null){
            getMediaConfiguration();
        } else {
            handleSavedInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String appStateJson = getAppStateAsJson();
        outState.putString(Constants.BUNDLE_EXTRA_SAVED_APP_STATE, appStateJson);
    }

    @Override
    public void onNextPageRequested(Media.TYPE mediaType) {
        int nextPage = ShowTimeApiService.MINIMUM_PAGE;
        switch (mediaType){
            case MOVIE:
                nextPage = getNextMoviePage();
                break;
            case TV_SHOW:
                nextPage = getNextTvShowPage();
                break;
            default:
                break;
        }
        getMedia(mediaType, nextPage);
    }

    @Override
    public void onMediaSelected(Media media, ImageView imageView) {
        if (media != null){
            String transitionName = getString(R.string.transition_item_to_detail);
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, transitionName);
            startActivity(getMediaDetailActivityIntent(media), transitionActivityOptions.toBundle());
        }
    }

    @Override
    public void onFragmentInitialized(int pos) {
        switch (pos){
            case MediaPageAdapter.POS_FRAGMENT_MOVIE:
                appendNewMovies(movieListCurrentPage);
                break;
            case MediaPageAdapter.POS_FRAGMENT_TV_SHOW:
                appendNewTvShows(tvShowsListCurrentPage);
                break;
            default:
                break;
        }
    }

    private void setupViewPager(){
        mediaPageAdapter = new MediaPageAdapter(getSupportFragmentManager(), this, this);
        viewPager.setAdapter(mediaPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private Intent getMediaDetailActivityIntent(Media media){
        if (gson == null){
            gson = getGson();
        }
        Intent intent = new Intent(getApplicationContext(), MediaDetailActivity.class);
        if (media instanceof Movie){
            intent.putExtra(Constants.BUNDLE_EXTRA_MOVIE, gson.toJson(media));
        }
        if (media instanceof TvShow){
            intent.putExtra(Constants.BUNDLE_EXTRA_TVSHOW, gson.toJson(media));
        }
        intent.putExtra(Constants.BUNDLE_EXTRA_IMAGE_BASE_URL, getMediaImageBaseUrl());
        return intent;
    }

    //region App state for rotation
    private String getAppStateAsJson(){
        if (gson == null){
            gson = getGson();
        }
        AppState appState = new AppState();

        appState.setMovieList(movieListCurrentPage);
        appState.setTvShowList(tvShowsListCurrentPage);
        appState.setCurrentMoviePage(currentPageMovie);
        appState.setCurrentTvShowPage(currentPageTvShow);
        appState.setImageBaseUrl(mediaImageBaseUrl);
        appState.setPosterSizes(mediaImagePosterSizes);

        return gson.toJson(appState);
    }

    private void handleSavedInstanceState(Bundle savedInstanceState){
        if (savedInstanceState == null){
            return;
        }
        Blog.d(MainActivity.class, "savedInstanceState not null");

        String saveAppStateJson = savedInstanceState.getString(Constants.BUNDLE_EXTRA_SAVED_APP_STATE);
        if (saveAppStateJson == null || saveAppStateJson.isEmpty()){
            return;
        }
        AppState appState = gson.fromJson(saveAppStateJson, AppState.class);
        unzipAllAppStates(appState);
    }

    private void unzipAllAppStates(AppState appState){
        if (appState == null){
            return;
        }
        movieListCurrentPage = appState.getMovieList();
        tvShowsListCurrentPage = appState.getTvShowList();
        currentPageMovie = appState.getCurrentMoviePage();
        currentPageTvShow = appState.getCurrentTvShowPage();
        mediaImageBaseUrl = appState.getImageBaseUrl();
        mediaImagePosterSizes = appState.getPosterSizes();
    }
    //endregion

    //region Helper methods for Media
    private void appendNewMovies(ArrayList<Movie> movies){
        if (mediaPageAdapter == null){
            Blog.d(MainActivity.class, "Cannot update Movies, tabAdapter null");
            return;
        }
        MediaFragment mediaFragment = (MediaFragment) mediaPageAdapter.getRegisteredFragment(MediaPageAdapter.POS_FRAGMENT_MOVIE);
        if (mediaFragment != null && getMediaImageBaseUrl() != null){
            ArrayList<Media> tmp = new ArrayList<>(movies.size());
            tmp.addAll(movies);
            mediaFragment.updateMedia(tmp, Media.TYPE.MOVIE, getMediaImageBaseUrl());
        }
    }

    private void appendNewTvShows(ArrayList<TvShow> tvShows){
        if (mediaPageAdapter == null){
            Blog.d(MainActivity.class, "Cannot update TV Shows, tabAdapter null");
            return;
        }
        MediaFragment mediaFragment = (MediaFragment) mediaPageAdapter.getRegisteredFragment(MediaPageAdapter.POS_FRAGMENT_TV_SHOW);
        if (mediaFragment != null && getMediaImageBaseUrl() != null){
            ArrayList<Media> tmp = new ArrayList<>(tvShows.size());
            tmp.addAll(tvShows);
            mediaFragment.updateMedia(tmp, Media.TYPE.TV_SHOW, getMediaImageBaseUrl());
        }
    }
    //endregion

    //region Getters
    private Gson getGson(){
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }

    /**
     * Returns the next movie page if it doesn't exceed maximum
     *
     * @return
     */
    public int getNextMoviePage(){
        if (currentPageMovie == ShowTimeApiService.MAXIMUM_PAGE){
            return currentPageMovie;
        }
        return ++currentPageMovie;
    }

    /**
     * Returns the next tv show page if it doesn't exceed maximum
     *
     * @return
     */
    public int getNextTvShowPage(){
        if (currentPageTvShow == ShowTimeApiService.MAXIMUM_PAGE){
            return currentPageTvShow;
        }
        return ++currentPageTvShow;
    }

    /**
     * Get the base url for building image paths
     *
     * Full image path will be:
     * {mediaImageBaseUrl + size + imageUrl}
     *
     * Where size will be calculated based on screen density dpi value
     *
     * @return
     */
    public String getMediaImageBaseUrl() {
        if (mediaImagePosterSizes == null){
            return null;
        }
        int screenDensityDpi = Util.getScreenDensity(this);
        String imageSize;

        if (screenDensityDpi >= Constants.SCREEN_DENSITY_LDPI && screenDensityDpi < Constants.SCREEN_DENSITY_MDPI){
            imageSize = mediaImagePosterSizes[1];
        } else if (screenDensityDpi >= Constants.SCREEN_DENSITY_MDPI && screenDensityDpi < Constants.SCREEN_DENSITY_HDPI){
            imageSize = mediaImagePosterSizes[2];
        } else if (screenDensityDpi >= Constants.SCREEN_DENSITY_HDPI && screenDensityDpi < Constants.SCREEN_DENSITY_XHDPI){
            imageSize = mediaImagePosterSizes[3];
        } else if (screenDensityDpi >= Constants.SCREEN_DENSITY_XHDPI && screenDensityDpi < Constants.SCREEN_DENSITY_XXHDPI){
            imageSize = mediaImagePosterSizes[4];
        } else if (screenDensityDpi >= Constants.SCREEN_DENSITY_XXHDPI){
            imageSize = mediaImagePosterSizes[5];
        } else {
            imageSize = mediaImagePosterSizes[4]; // default value for 480dp screens ( most screens seem to fit here )
        }
        return mediaImageBaseUrl + imageSize;
    }

    /**
     * Returns poster sizes by width
     * @return ["w92","w154","w185","w342","w500","w780","original"]
     */
    public String[] getMediaImagePosterSizes() {
        return mediaImagePosterSizes;
    }

    private void getMediaConfiguration(){
        showTimeApiService = getApiClient();
        if (showTimeApiService == null){
            return;
        }
        String key =  ShowTimeApiService.API_AUTH_KEY;
        showTimeApiService.getConfiguration(key).enqueue(new Callback<Configuration>() {
            @Override
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                Configuration configuration = response.body();
                if (configuration == null || configuration.getImages() == null){
                    return;
                }
                mediaImageBaseUrl = configuration.getImages().getBaseUrl(); // todo can change to secure
                mediaImagePosterSizes = configuration.getImages().getPosterSizes();
                getMedia(Media.TYPE.MOVIE, currentPageMovie);
                getMedia(Media.TYPE.TV_SHOW, currentPageTvShow);
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.configuration_failure), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    /**
     * Calls api call to return popular media based on type and page number.
     *
     * Results are stored in #movieListCurrentPage and #tvShowsListCurrentPage lists
     *
     * @param mediaType
     * @param pageNum
     */
    public void getMedia(Media.TYPE mediaType, int pageNum){
        showTimeApiService = getApiClient();
        if (showTimeApiService == null){
            return;
        }
        updateQueryOptions(pageNum);
        switch (mediaType){
            case MOVIE:
                showTimeApiService.getPopularMovies(apiQueryOptions).enqueue(movieCallback);
                break;
            case TV_SHOW:
                showTimeApiService.getPopularTvShows(apiQueryOptions).enqueue(tvShowCallback);
                break;
            default:
                break;
        }
    }

    private void updateQueryOptions(int pageNum){
        if (apiQueryOptions == null){
            apiQueryOptions = new HashMap<>(3);
            apiQueryOptions.put(ShowTimeApiService.API_HEADER_KEY, ShowTimeApiService.API_AUTH_KEY);
            apiQueryOptions.put(ShowTimeApiService.QUERY_SORT_BY, ShowTimeApiService.QUERY_SORT_BY_POPULAR_DESC);
            apiQueryOptions.put(ShowTimeApiService.QUERY_PAGE, Integer.toString(pageNum));
        } else {
            apiQueryOptions.put(ShowTimeApiService.QUERY_PAGE, Integer.toString(pageNum));
        }
    }


    /**
     * Returns the Api client as a singleton
     * @return ShowTimeApiService Api client
     */
    public ShowTimeApiService getApiClient() {
        if (showTimeApiService == null) {
            if (gson == null){
                gson = getGson();
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ShowTimeApiService.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            showTimeApiService = retrofit.create(ShowTimeApiService.class);
        }
        return showTimeApiService;
    }
    //endregion

    //region Callbacks
    /**
     * Callback that stores discovered movies into #movieListCurrentPage list and appends the new movies
     * to existing list of movies
     */
    Callback<DiscoverMovies> movieCallback = new Callback<DiscoverMovies>() {
        @Override
        public void onResponse(Call<DiscoverMovies> call, Response<DiscoverMovies> response) {
            DiscoverMovies discoverMovies = response.body();
            if (discoverMovies == null || discoverMovies.getResults() == null){
                return;
            }
            if (movieListCurrentPage == null){
                movieListCurrentPage = new ArrayList<>(discoverMovies.getResults().length);
            }

            ArrayList<Movie> tmp = new ArrayList<>(discoverMovies.getResults().length);
            Collections.addAll(tmp, discoverMovies.getResults());
            movieListCurrentPage.addAll(tmp);
            appendNewMovies(tmp);
        }

        @Override
        public void onFailure(Call<DiscoverMovies> call, Throwable t) {
            t.printStackTrace();
        }
    };

    /**
     * Callback that stores discovered tv shows into #tvShowsListCurrentPage list and appends new tv shows
     * to existing list of tv shows
     */
    Callback<DiscoverTvShows> tvShowCallback = new Callback<DiscoverTvShows>() {

        @Override
        public void onResponse(Call<DiscoverTvShows> call, Response<DiscoverTvShows> response) {
            DiscoverTvShows discoverTvShows = response.body();
            if (discoverTvShows == null || discoverTvShows.getResults() == null){
                return;
            }
            if (tvShowsListCurrentPage == null){
                tvShowsListCurrentPage = new ArrayList<>(discoverTvShows.getResults().length);
            }

            ArrayList<TvShow> tmp = new ArrayList<>(discoverTvShows.getResults().length);
            Collections.addAll(tmp, discoverTvShows.getResults());
            tvShowsListCurrentPage.addAll(tmp);
            appendNewTvShows(tmp);
        }

        @Override
        public void onFailure(Call<DiscoverTvShows> call, Throwable t) {
            t.printStackTrace();
        }
    };
}

