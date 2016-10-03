package com.time.show.activity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.transition.Transition;
import android.view.Display;
import android.widget.EdgeEffect;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.time.show.R;
import com.time.show.model.Media;
import com.time.show.model.Movie;
import com.time.show.model.TvShow;
import com.time.show.util.Constants;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity to show the details of the selected media
 * which should is expected to be passed in as an intent bundle
 *
 * @author aleksandrTmk
 */
public class MediaDetailActivity extends AppCompatActivity {
    @BindView(R.id.media_detail_title) TextView title;
    @BindView(R.id.media_detail_summary) TextView summary;
    @BindView(R.id.media_detail_poster) ImageView posterImage;
    @BindView(R.id.media_detail_popularity) TextView popularity;
    @BindView(R.id.media_detail_scroll_view) ScrollView scrollView;

    private String posterImageBaseUrl;
    private Movie movie;
    private TvShow tvShow;
    private Media.TYPE mediaType;
    private ActivityTransitionListener activityTransitionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);
        ButterKnife.bind(this);

        registerTransitionListener(true);
        if (getIntent() == null || getIntent().getExtras() == null){
            return;
        }
        setupMedia();
        setMediaViewsInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        registerTransitionListener(false);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    private void registerTransitionListener(boolean register){
        if (activityTransitionListener == null){
            activityTransitionListener = new ActivityTransitionListener();
        }
        if (register){
            getWindow().getSharedElementEnterTransition().addListener(activityTransitionListener);
        } else {
            getWindow().getSharedElementEnterTransition().removeListener(activityTransitionListener);
        }
    }

    private void setupMedia(){
        Gson gSon = new GsonBuilder().create();
        String bundleMovie = getIntent().getExtras().getString(Constants.BUNDLE_EXTRA_MOVIE);
        String bundleTvShow = getIntent().getExtras().getString(Constants.BUNDLE_EXTRA_TVSHOW);
        posterImageBaseUrl = getIntent().getExtras().getString(Constants.BUNDLE_EXTRA_IMAGE_BASE_URL);

        if (bundleMovie != null){
            movie = gSon.fromJson(bundleMovie, Movie.class);
            mediaType = Media.TYPE.MOVIE;
        }
        if (bundleTvShow != null){
            tvShow = gSon.fromJson(bundleTvShow, TvShow.class);
            mediaType = Media.TYPE.TV_SHOW;
        }
    }

    private void setMediaViewsInfo(){
        if (mediaType == null){
            return;
        }
        String posterUrl = posterImageBaseUrl;
        String popularityPrefix = getString(R.string.activity_detail_popularity) + " ";

        // shorten the popularity to 2 decimals
        DecimalFormat df = new DecimalFormat("###.##");
        switch (mediaType){
            case MOVIE:
                title.setText(movie.getTitle());
                summary.setText(movie.getOverview());
                popularity.setText(popularityPrefix + df.format(movie.getPopularity()));
                posterUrl += movie.getPosterPath();
                break;
            case TV_SHOW:
                title.setText(tvShow.getName());
                summary.setText(tvShow.getOverview());
                popularity.setText(popularityPrefix + df.format(tvShow.getPopularity()));
                posterUrl += tvShow.getPosterPath();
                break;
            default:
                break;
        }
        setPosterImage(posterUrl);
    }

    private void setPosterImage(String posterUrl){
        Point screenSize = getScreenSize();
        int width = screenSize.x;
        int height = screenSize.y;

        resizePosterView(height, width);

        Glide.with(getApplicationContext()).load(posterUrl).asBitmap()
                .placeholder(R.drawable.poster)
                .override(width, height)
                .into(new PosterTarget(posterImage));
    }

    private Point getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    private void resizePosterView(int height, int width){
        if (posterImage == null){
            return;
        }
        posterImage.requestLayout();
        posterImage.setImageResource(R.drawable.poster);
        posterImage.getLayoutParams().height = height;
        posterImage.getLayoutParams().width = width;
    }

    /**
     * Hacky way to update glow of scroll view. Can't find a correct way.
     *
     * http://stackoverflow.com/questions/27104521/android-lollipop-scrollview-edge-effect-color
     *
     * @param brandColor
     */
    private void updateOverscrollGlow(int brandColor) {
        EdgeEffect edgeEffectTop = new EdgeEffect(this);
        edgeEffectTop.setColor(brandColor);

        EdgeEffect edgeEffectBottom = new EdgeEffect(this);
        edgeEffectBottom.setColor(brandColor);

        try {
            Field f1 = ScrollView.class.getDeclaredField("mEdgeGlowTop");
            Field f2 = ScrollView.class.getDeclaredField("mEdgeGlowBottom");

            setScrollViewField(f1, edgeEffectTop);
            setScrollViewField(f2, edgeEffectBottom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScrollViewField(Field field, EdgeEffect effect){
        if (scrollView == null || field == null || effect == null){
            return;
        }
        field.setAccessible(true);
        try {
            field.set(scrollView, effect);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //region Helper classes
    /**
     * Custom BitMapImageViewTarget class for the media poster. Allows us to detect when resrouce is
     * ready so we can
     * 1) Animate the scrollview down to the end of the page
     * 2) Update status bar color
     * 2) Update glow effects of scroll view
     */
    private class PosterTarget extends BitmapImageViewTarget implements Palette.PaletteAsyncListener{

        public PosterTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            super.onResourceReady(resource, glideAnimation);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Palette.from(resource).generate(this);
            }
        }

        @Override
        public void onGenerated(Palette palette) {
            int defaultColor = ContextCompat.getColor(MediaDetailActivity.this, R.color.colorPrimaryDark);
            int color = palette.getDominantColor(defaultColor);
            getWindow().setStatusBarColor(color);
            updateOverscrollGlow(color);
        }
    }

    /**
     * Class that implements listener for transition state between activities.
     */
    private class ActivityTransitionListener implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
            if (scrollView == null) {
                return;
            }
            Point screenSize = getScreenSize();
            scrollView.smoothScrollTo(0, screenSize.y/4); // scroll down 1/4 of the screen
        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }
}
