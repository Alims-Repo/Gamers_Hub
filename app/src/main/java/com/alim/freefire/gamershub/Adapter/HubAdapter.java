package com.alim.freefire.gamershub.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alim.freefire.gamershub.Model.YoutubeDataModel;
import com.alim.freefire.gamershub.R;
import com.alim.freefire.gamershub.interfaces.OnItemClickListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.YoutubePostHolder> {

    Context context;
    private final OnItemClickListener listener;
    private ArrayList<YoutubeDataModel> dataSet;

    public HubAdapter(ArrayList<YoutubeDataModel> dataSet, OnItemClickListener listener) {
        this.dataSet = dataSet;
        this.listener = listener;
    }

    @NonNull
    @Override
    public YoutubePostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hub_layout,parent,false);
        context = parent.getContext();
        return new YoutubePostHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(YoutubePostHolder holder, int position) {

        //set the views here
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDes = holder.textViewDes;
        //TextView textViewDate = holder.textViewDate;
        ImageView ImageThumb = holder.ImageThumb;

        YoutubeDataModel object = dataSet.get(position);

        textViewTitle.setText(object.getTitle());
        textViewDes.setText(object.getChannelName()+"  ~  "+object.getPublishedAt());
        //textViewDate.setText(object.getPublishedAt());
        holder.bind(dataSet.get(position), listener);

        //TODO: image will be downloaded from url

        Picasso.get().load(object.getThumbnail()).into(ImageThumb);

        if ((position >= getItemCount() - 1)) {
            listener.load();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class YoutubePostHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDes;
        TextView textViewDate;
        ImageView ImageThumb;

        YoutubePostHolder(View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewDes = itemView.findViewById(R.id.channel_name);
            //this.textViewDate = itemView.findViewById(R.id.time);
            this.ImageThumb = itemView.findViewById(R.id.ImageThumb);
        }

        void bind(final YoutubeDataModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
