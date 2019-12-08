package com.alim.freefire.gamershub.Config;

public class Config {

    private Config() {
    }

    public static final String API_KEY = "AIzaSyBEBZzlQACiOzr8K0NmA1ECqCvxvRkR52U";
    public static final String CHANNEL_ID = "UC9nXCL3Cv-krVocODOZoxYg";

    public static final String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResults=50&key=" + API_KEY + "";

    /*public static final String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId="
            + CHANNEL_ID + "&maxResults=20&key=" + API_KEY + "";*/
    public static final String LIVE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&channelId="
            + CHANNEL_ID + "&eventType=live&maxResults=20&key=" + API_KEY + "";

}
