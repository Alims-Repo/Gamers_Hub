package com.alim.freefire.gamershub.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alim.freefire.gamershub.Adapter.HubAdapter;
import com.alim.freefire.gamershub.Config.Config;
import com.alim.freefire.gamershub.Model.YoutubeDataModel;
import com.alim.freefire.gamershub.PlayerActivity;
import com.alim.freefire.gamershub.R;
import com.alim.freefire.gamershub.interfaces.OnItemClickListener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.alim.freefire.gamershub.Config.Config.CHANNLE_GET_URL;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HubAdapter adapter;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recycle_view);
        initList(mListData);
        new RequestYoutubeAPI().execute();

        return rootView;
    }

    private void initList(ArrayList<YoutubeDataModel> mListData) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HubAdapter(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }


    //create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(CHANNLE_GET_URL);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (Exception e) {
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
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDataModel> mList = new ArrayList<>();
        if (jsonObject.has("items")) {
            try {
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
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

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
}
