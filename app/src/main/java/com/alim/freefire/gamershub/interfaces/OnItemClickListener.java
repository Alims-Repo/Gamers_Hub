package com.alim.freefire.gamershub.interfaces;

import android.widget.ImageView;

import com.alim.freefire.gamershub.Model.YoutubeDataModel;

public interface OnItemClickListener {
    void load();
    void onItemClick(YoutubeDataModel item);
}
