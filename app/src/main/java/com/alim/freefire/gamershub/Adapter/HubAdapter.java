package com.alim.freefire.gamershub.Adapter;

import android.content.Context;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;

import static com.alim.freefire.gamershub.Config.Config.CHANNEL_ID;
import static com.alim.freefire.gamershub.Config.Config.CHANNEL_THUMB;
import static com.alim.freefire.gamershub.Config.Config.CHANNEL_THUMB_KEY;

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.YoutubePostHolder> {

    private Context mContext;
    private final OnItemClickListener listener;
    private ArrayList<YoutubeDataModel> dataSet;


    public HubAdapter(Context mContext, ArrayList<YoutubeDataModel> dataSet,
                      OnItemClickListener listener) {
        this.dataSet = dataSet;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public YoutubePostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hub_layout,parent,false);
        return new YoutubePostHolder(view);
    }

    @Override
    public void onBindViewHolder(YoutubePostHolder holder, int position) {

        //set the views here
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDes = holder.textViewDes;
        ImageView ImageThumb = holder.ImageThumb;
        ImageView ChannelThumb = holder.ChannelThumb;

        YoutubeDataModel object = dataSet.get(position);

        textViewTitle.setText(object.getTitle());
        textViewDes.setText(object.getDescription());
        holder.bind(dataSet.get(position), listener);

        //TODO: image will be downloaded from url
        Picasso.with(mContext).load(object.getThumbnail()).into(ImageThumb);
        Picasso.with(mContext).load("https://yt3.ggpht.com/a/AGF-l7_NdotdswYLM_QPKYq7fQUpl26kf5NiYI3tvQ=s88-c-k-c0xffffffff-no-rj-mo").into(ChannelThumb);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class YoutubePostHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDes;
        TextView textViewDate;
        ImageView ImageThumb;
        ImageView ChannelThumb;

        YoutubePostHolder(View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewDes = itemView.findViewById(R.id.description);
            this.ImageThumb = itemView.findViewById(R.id.ImageThumb);
            this.ChannelThumb = itemView.findViewById(R.id.channel_logo);
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
