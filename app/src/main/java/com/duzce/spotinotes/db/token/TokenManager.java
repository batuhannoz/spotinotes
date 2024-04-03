package com.duzce.spotinotes.db.token;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {

    private static final String PREF_NAME = "spotinotes_spotify_access_token";

    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static final String TOKEN_EXPIRATION_KEY = "token_expiration";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveAccessToken(Context context, String accessToken, long expirationTime) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putLong(TOKEN_EXPIRATION_KEY, expirationTime);
        editor.apply();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = getPrefs(context);
        long expirationTime = prefs.getLong(TOKEN_EXPIRATION_KEY, 0);
        long currentTime = System.currentTimeMillis();

        if (currentTime < expirationTime) {
            return prefs.getString(ACCESS_TOKEN_KEY, null);
        } else {
            clearAccessToken(context);
            return null;
        }
    }

    public static void clearAccessToken(Context context) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(TOKEN_EXPIRATION_KEY);
        editor.apply();
    }

}