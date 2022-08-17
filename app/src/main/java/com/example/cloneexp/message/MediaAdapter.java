package com.example.cloneexp.message;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cloneexp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {
    private Context context;
    private ArrayList<String> mediaList;

    public MediaAdapter(Context context, ArrayList<String> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }
    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View LayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.media,null,false);
        MediaViewHolder mediaStuff=new MediaViewHolder(LayoutView);
        return mediaStuff;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Glide.with(context).load(Uri.parse(mediaList.get(position))).into(holder.mediaItem);

    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mediaItem;
        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaItem=itemView.findViewById(R.id.mediaImage);
        }
    }

}
