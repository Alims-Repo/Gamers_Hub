package com.alim.freefire.gamershub;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alim.freefire.gamershub.Adapter.CommentAdapter;
import com.alim.freefire.gamershub.DataBase.AppSettings;
import com.alim.freefire.gamershub.Model.YoutubeCommentModel;
import com.alim.freefire.gamershub.Model.YoutubeDataModel;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    AppSettings appSettings;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static String GOOGLE_YOUTUBE_API = "AIzaSyBH8szUCt1ctKQabVeQuvWgowaKxHVjn8E";
    private YoutubeDataModel youtubeDataModel = null;
    TextView textViewName;
    TextView textViewDes;
    TextView textViewDate;
    // ImageView imageViewIcon;
    public static final String VIDEO_ID = "c2UNv38V6y4";
    private YouTubePlayerView mYoutubePlayerView = null;
    private YouTubePlayer mYoutubePlayer = null;
    private ArrayList<YoutubeCommentModel> mListData = new ArrayList<>();
    private CommentAdapter mAdapter = null;
    private RecyclerView mList_videos = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appSettings = new AppSettings(this);
        if (appSettings.getTheme()==1) {
            setTheme(R.style.AppThemeDark);
        } else if (appSettings.getTheme()==2) {
            setTheme(R.style.AppTheme);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.AppThemeDark);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    setTheme(R.style.AppTheme);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        youtubeDataModel = getIntent().getParcelableExtra(YoutubeDataModel.class.toString());
        Log.e("", youtubeDataModel.getDescription());

        mYoutubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        mYoutubePlayerView.initialize(GOOGLE_YOUTUBE_API, this);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDes = (TextView) findViewById(R.id.textViewDes);
        // imageViewIcon = (ImageView) findViewById(R.id.imageView);
        textViewDate = (TextView) findViewById(R.id.textViewDate);

        textViewName.setText(youtubeDataModel.getTitle());
        textViewDes.setText(youtubeDataModel.getDescription());
        textViewDate.setText(youtubeDataModel.getPublishedAt());

        mList_videos = (RecyclerView) findViewById(R.id.mList_videos);
        new RequestYoutubeCommentAPI().execute();
//        try {
//            if (youtubeDataModel.getThumbnail() != null) {
//                if (youtubeDataModel.getThumbnail().startsWith("http")) {
//                    Picasso.with(this)
//                            .load(youtubeDataModel.getThumbnail())
//                            .into(imageViewIcon);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (!checkPermissionForReadExtertalStorage()) {
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final ExpandableLinearLayout content = findViewById(R.id.content);
        RelativeLayout header = findViewById(R.id.accordian_header);
        final ImageView ExpandIcon = findViewById(R.id.expand_icon);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {content.toggle();
                if (content.isExpanded()) {
                    ExpandIcon.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textViewName.setMaxLines(2);
                        }
                    },200);
                } else {
                    textViewName.setMaxLines(10);
                    ExpandIcon.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
            }
        });
        ///

    }

    public void back_btn_pressed(View view) {
        finish();
    }

//    public void playVideo(View view) {
//        if (mYoutubePlayer != null) {
//            if (mYoutubePlayer.isPlaying())
//                mYoutubePlayer.pause();
//            else
//                mYoutubePlayer.play();
//        }
//    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!wasRestored) {
            youTubePlayer.cueVideo(youtubeDataModel.getVideo_id());
        }
        youTubePlayer.play();
        mYoutubePlayer = youTubePlayer;
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private class RequestYoutubeCommentAPI extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String VIDEO_COMMENT_URL = "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&maxResults=100&videoId=" + youtubeDataModel.getVideo_id() + "&key=" + GOOGLE_YOUTUBE_API;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(VIDEO_COMMENT_URL);
            Log.e("url: ", VIDEO_COMMENT_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseJson(jsonObject);
                    initVideoList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initVideoList(ArrayList<YoutubeCommentModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentAdapter(this, mListData);
        mList_videos.setAdapter(mAdapter);
    }

    public ArrayList<YoutubeCommentModel> parseJson(JSONObject jsonObject) {
        ArrayList<YoutubeCommentModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);

                    YoutubeCommentModel youtubeObject = new YoutubeCommentModel();
                    JSONObject jsonTopLevelComment = json.getJSONObject("snippet").getJSONObject("topLevelComment");
                    JSONObject jsonSnippet = jsonTopLevelComment.getJSONObject("snippet");

                    String title = jsonSnippet.getString("authorDisplayName");
                    String thumbnail = jsonSnippet.getString("authorProfileImageUrl");
                    String comment = jsonSnippet.getString("textDisplay");

                    youtubeObject.setTitle(title);
                    youtubeObject.setComment(comment);
                    youtubeObject.setThumbnail(thumbnail);
                    mList.add(youtubeObject);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int result2 = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            return (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }
}