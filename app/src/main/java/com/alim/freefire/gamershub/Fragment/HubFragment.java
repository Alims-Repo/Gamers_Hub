package com.alim.freefire.gamershub.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alim.freefire.gamershub.Adapter.HubAdapter;
import com.alim.freefire.gamershub.Model.YoutubeDataModel;
import com.alim.freefire.gamershub.PlayerActivity;
import com.alim.freefire.gamershub.R;
import com.alim.freefire.gamershub.interfaces.OnItemClickListener;
import com.alim.freefire.gamershub.interfaces.OnNavClickListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import static com.alim.freefire.gamershub.Config.Config.CHANNLE_GET_URL;
import static com.alim.freefire.gamershub.Config.Config.NEXTPAGE;
import static com.alim.freefire.gamershub.Config.Config.NEXTPAGE2;
import static com.alim.freefire.gamershub.Config.Config.SEARCH_URL;
import static com.alim.freefire.gamershub.Config.Config.SEARCH_URL2;

public class HubFragment extends Fragment {

    private TextView load_text;
    private HubAdapter adapter;
    private ProgressBar loading_pro;
    private static int position = 0;
    private RecyclerView recyclerView;
    private FrameLayout Loading_frame;
    private boolean dataChanged = false;
    private static String nextPageToken = "";
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hub, container, false);

        recyclerView = rootView.findViewById(R.id.recycle_view);
        loading_pro = rootView.findViewById(R.id.loading_por);
        Loading_frame = rootView.findViewById(R.id.loading);
        load_text = rootView.findViewById(R.id.load_text);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        initList();
        new RequestYoutubeAPI().execute();

        adapter = new HubAdapter(mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString(), item);
                startActivity(intent);
            }

            @Override
            public void load() {
                Loading_frame.setVisibility(View.VISIBLE);
                dataChanged = true;
                if (!nextPageToken.equals(""))
                    new RequestYoutubeAPI().execute();
            }
        });

        LoadingColor();
        loading_pro.setVisibility(View.VISIBLE);
        return rootView;
    }

    private void initList() {
        if (dataChanged) {
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter.notifyDataSetChanged();
        } else {
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(position);
        }
        if (mListData.size()>0)
            loading_pro.setVisibility(View.GONE);
    }

    //create an asynctask to get all the data from youtube
    @SuppressLint("StaticFieldLeak")
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String URL = SEARCH_URL+SEARCH_URL2;
                if (!nextPageToken.isEmpty())
                    URL = NEXTPAGE+nextPageToken+NEXTPAGE2;
                Log.println(Log.ASSERT,"ULR",URL);
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(URL);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                Log.println(Log.ASSERT,"Eight","Ok");
                return json;
            } catch (Exception e) {
                Log.println(Log.ASSERT,"Seven",e.toString());
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
                    mListData.addAll(parseVideoListFromResponse(jsonObject));
                    Loading_frame.setVisibility(View.GONE);
                    initList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDataModel> mList = new ArrayList<>();
        if (jsonObject.has("items")) {
            try {
                if (jsonObject.has("nextPageToken")) {
                    nextPageToken = jsonObject.getString("nextPageToken");
                    Log.println(Log.ASSERT,"TOKEN",nextPageToken);
                }
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("id")) {
                        JSONObject jsonID = json.getJSONObject("id");
                        String video_id = "";
                        if (jsonID.has("videoId")) {
                            video_id = jsonID.getString("videoId");
                        }
                        if (jsonID.has("kind")) {
                            if (jsonID.getString("kind").equals("youtube#video")) {

                                YoutubeDataModel youtubeObject = new YoutubeDataModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String channel = jsonSnippet.getString("channelTitle");

                                Log.println(Log.ASSERT,"Name",channel);

                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                                try {
                                    SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                                    publishedAt = "Published on : "+new SimpleDateFormat("MMM d, yyyy").format(Objects.requireNonNull(sd1.parse(publishedAt)));
                                } catch (Exception e) {
                                    Log.println(Log.ASSERT,"Time",e.toString());
                                }
                                youtubeObject.setChannelName(channel);
                                youtubeObject.setTitle(title);
                                youtubeObject.setDescription(description);
                                youtubeObject.setPublishedAt(publishedAt);
                                youtubeObject.setThumbnail(thumbnail);
                                youtubeObject.setVideo_id(video_id);
                                mList.add(youtubeObject);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }

    @Override
    public void onResume() {
        super.onResume();
        dataChanged = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        position = linearLayoutManager.getChildCount()+linearLayoutManager.findFirstVisibleItemPosition()-3;
        dataChanged = false;
    }

    private void LoadingColor() {
        TextPaint paint = load_text.getPaint();
        float width = paint.measureText(load_text.getText().toString());
        Shader textShader = new LinearGradient(0, 0, width, load_text.getTextSize(),
                new int[]{getResources().getColor(R.color.colorGradusBlue)
                        ,getResources().getColor(R.color.colorViolet)},
                null, Shader.TileMode.CLAMP);
        load_text.getPaint().setShader(textShader);
    }
}