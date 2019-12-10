package com.alim.freefire.gamershub.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    private Context context;

    public AppSettings(Context Appcontext) {
        context = Appcontext;
    }

    private final String DATA_NAME = "APP_SETTINGS";
    private final String THEME = "THEME";
    private final String MESSAGE = "MESSAGE";
    private final String DATE = "DATE";

    public void setTheme(int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(DATA_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(THEME, value);
        editor.apply();
    }

    public int getTheme() {
        SharedPreferences prefs = context.getSharedPreferences(DATA_NAME, 0);
        return prefs.getInt(THEME,0);
    }
}
