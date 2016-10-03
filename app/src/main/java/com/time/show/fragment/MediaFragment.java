package com.time.show.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.time.show.R;
import com.time.show.adapter.EndlessRecyclerViewScrollListener;
import com.time.show.adapter.MediaRecyclerViewAdapter;
import com.time.show.model.Media;
import com.time.show.util.Blog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Base fragment containing a gridView
 *
 * @author aleksandrTmk
 */
public class MediaFragment extends Fragment implements MediaRecyclerViewAdapter.OnMediaItemClickListener {
    /**
     * Interface for MediaFragment. Requests updates to media item for next page and passes along
     * selected media.
     */
    public interface MediaFragmentInterface {
        void onNextPageRequested(Media.TYPE mediaType);
        void onMediaSelected(Media media, ImageView imageView);
    }

    @BindView(R.id.fragment_media_recycler_view)
    RecyclerView recyclerView;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private MediaRecyclerViewAdapter mediaRecyclerViewAdapter;
    private MediaFragmentInterface mediaFragmentInterface;

    public MediaFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            mediaFragmentInterface = (MediaFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement MediaFragmentInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media, container, false);
        ButterKnife.bind(this, rootView);

        mediaRecyclerViewAdapter = new MediaRecyclerViewAdapter(this);
        GridLayoutManager gridLayoutManager = getGridLayoutManager();
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mediaRecyclerViewAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Media.TYPE mediaType = mediaRecyclerViewAdapter.getMediaType();
                if (mediaType == null){
                    return;
                }
                Blog.d(MediaFragment.class, "Request next page of items for " + mediaType.name());
                mediaFragmentInterface.onNextPageRequested(mediaType);
            }
        });

        return rootView;
    }

    @Override
    public void onMediaItemClick(Media media, ImageView imageView) {
        mediaFragmentInterface.onMediaSelected(media, imageView);
    }

    /**
     * Returns a new instance of a fragment for the given section number and media type
     */
    public static MediaFragment newInstance(int sectionNumber) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateMedia(ArrayList<Media> mediaList, Media.TYPE mediaType, String imageBaseUrl){
        mediaRecyclerViewAdapter.setMediaType(mediaType);
        mediaRecyclerViewAdapter.setBaseImageUrl(imageBaseUrl);
        mediaRecyclerViewAdapter.setMediaList(mediaList);
    }

    private GridLayoutManager getGridLayoutManager(){
        if (getActivity() == null){
            return null;
        }
        int orientation = getActivity().getResources().getConfiguration().orientation;
        GridLayoutManager gridLayoutManager;

        switch (orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
            default:
                gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                break;

        }
        return gridLayoutManager;
    }
}
