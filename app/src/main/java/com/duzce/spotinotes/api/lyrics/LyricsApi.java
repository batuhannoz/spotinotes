package com.duzce.spotinotes.api.lyrics;

import com.google.gson.Gson;

import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LyricsApi {

    public static final String API_KEY = "3efecaac665438febe60f50b68914624";

    public static final String BASE_URL = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get";

    public static String CommercialUsePattern = "\\*\\*\\*\\*\\*\\*\\* This Lyrics is NOT for Commercial use \\*\\*\\*\\*\\*\\*\\*";

    public class Body {

        private Lyrics lyrics;

        public Lyrics getLyrics() {
            return lyrics;
        }

    }

    public class Header {

        private int status_code;

        private double execute_time;

        public int getStatus_code() {
            return status_code;
        }

        public double getExecute_time() {
            return execute_time;
        }

    }

    public class Lyrics {

        private int lyrics_id;

        private int explicit;

        private String lyrics_body;

        private String script_tracking_url;

        private String pixel_tracking_url;

        private String lyrics_copyright;

        private Date updated_time;

        public int getLyrics_id() {
            return lyrics_id;
        }

        public int getExplicit() {
            return explicit;
        }


        public String getLyrics_body() {
            return lyrics_body;
        }

        public String getScript_tracking_url() {
            return script_tracking_url;
        }

        public String getPixel_tracking_url() {
            return pixel_tracking_url;
        }

        public String getLyrics_copyright() {
            return lyrics_copyright;
        }

        public Date getUpdated_time() {
            return updated_time;
        }

    }

    public class Message {

        private Header header;

        private Body body;

        public Header getHeader() {
            return header;
        }

        public Body getBody() {
            return body;
        }

    }

    public class LyricsResponse {

        private Message message;

        public Message getMessage() {
            return message;
        }

    }

    public static LyricsResponse LyricsMatcher(String qTrack, String qArtist) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("apikey", API_KEY);
        urlBuilder.addQueryParameter("q_track", qTrack);
        urlBuilder.addQueryParameter("q_artist", qArtist);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        LyricsResponse lyricsResponse = null;
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            Gson gson = new Gson();
            lyricsResponse = gson.fromJson(responseBody, LyricsResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lyricsResponse;
    }

}
