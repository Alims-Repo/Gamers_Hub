package com.alim.freefire.gamershub.Config;

public class Config {

    private Config() {
    }

    public static final String API_KEY = "AIzaSyAdDix7i7a3an-gyXiquTV_14cIsr8-DZg";
    //public static final String API_KEY = "AIzaSyBEBZzlQACiOzr8K0NmA1ECqCvxvRkR52U";
    public static final String CHANNEL_ID = "UC9nXCL3Cv-krVocODOZoxYg";

    public static final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&regionCode=IN&order=relevance&maxResults=10&q="+"Free+Fire";
    public static final String SEARCH_URL2 = "&key="+API_KEY+"";
    public static final String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=relevance&maxResults=5&key=" + API_KEY + "";

    /*public static final String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=relevance&channelId="
            + CHANNEL_ID + "&maxResults=20&key=" + API_KEY + "";*/

    public static final String NEXTPAGE = "https://www.googleapis.com/youtube/v3/search?pageToken=";
    public static final String  NEXTPAGE2 = "&part=snippet&maxResults=10&regionCode=IN&order=viewCount&q="+"Free+Fire"+"&key="+API_KEY+"";

    public static final String TRENDING1 = "https://www.googleapis.com/youtube/v3/search?pageToken=&";
    public static final String TRENDING2 = "part=snippet&order=relevance&type=video&channelId="+ CHANNEL_ID + "&maxResults=10&key=" + API_KEY + "";
    public static final String TRENDING = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&type=video&channelId="
            + CHANNEL_ID + "&maxResults=10&key=" + API_KEY + "";

    public static final String CHANNEL_THUMB = "https://www.googleapis.com/youtube/v3/channels?part=snippet&fields=items%2Fsnippet%2Fthumbnails%2Fdefault&id=";
    public static final String CHANNEL_THUMB_KEY = "&key="+API_KEY;

    public static final String LIVE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&regionCode=IN&type=video&q=Free+Fire&eventType=live&order=viewCount&maxResults=20&key=" + API_KEY + "";

}
