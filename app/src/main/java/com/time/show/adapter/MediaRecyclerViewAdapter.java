package com.time.show.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.time.show.R;
import com.time.show.model.Media;
import com.time.show.util.Blog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView holding media items ( Movie and TV Show )
 *
 * @author aleksandrTmk
 */
public class MediaRecyclerViewAdapter extends RecyclerView.Adapter<MediaRecyclerViewAdapter.MediaViewHolder> {
    /**
     * Pass position of clicked recycler view item
     */
    public interface OnMediaItemClickListener {
        void onMediaItemClick(Media media, ImageView imageView);
    }

    private OnMediaItemClickListener onMediaItemClickListener;
    private String baseImageUrl;
    private Media.TYPE mediaType;
    private ArrayList<Media> mediaList;

    public class MediaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.grid_item_image_view) ImageView imageView;

        public MediaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMediaItemClickListener.onMediaItemClick(mediaList.get(getLayoutPosition()), imageView);
        }
    }

    public MediaRecyclerViewAdapter(OnMediaItemClickListener onMediaItemClickListener){
        this.onMediaItemClickListener = onMediaItemClickListener;
        this.mediaList = new ArrayList<>();
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View root = layoutInflater.inflate(R.layout.grid_item, parent, false);
        return new MediaViewHolder(root);
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        if (mediaList.get(position) == null){
            return;
        }
        String imageUrl = mediaList.get(position).getPosterPath();
        Glide.with(holder.imageView.getContext()).load(baseImageUrl + imageUrl)
                .centerCrop()
                .placeholder(R.drawable.poster).error(R.drawable.poster).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (mediaList == null){
            return 0;
        }
        return mediaList.size();
    }

    //region Helper methods
    /**
     * Appends current media list with new media and calls notifyDataSetChanged()
     * @param dataList
     */
    public void setMediaList(ArrayList<Media> dataList){
        if (mediaList == null){
            mediaList = new ArrayList<>(dataList.size());
        }
        mediaList.addAll(dataList);
        Blog.d(MediaRecyclerViewAdapter.class, "Added " + dataList.size() + " " + mediaType.name() + " items. Total " + mediaType.name() + " media: " + mediaList.size());
        notifyDataSetChanged();
    }

    public ArrayList<Media> getMediaList() {
        return mediaList;
    }

    /**
     * Set which type of media is in this adapter
     *
     * @param mediaType
     */
    public void setMediaType(Media.TYPE mediaType){
        this.mediaType = mediaType;
    }

    /**
     * Get the type of media is in this adapter
     * @return
     */
    public Media.TYPE getMediaType(){
        return mediaType;
    }

    /**
     * Set the base image url. Url should be full when we append image url
     * @param url
     */
    public void setBaseImageUrl(String url){
        baseImageUrl = url;
    }
    //endregion
}
