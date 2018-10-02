package com.example.sss.team_project.utills;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public static final String PREFERENCE_NAME = "login_pref";
    private static PreferenceUtil preferencemodule = null;
    private static Context mContext;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;


    public static PreferenceUtil getInstance(Context context) {
        mContext = context;

        if (preferencemodule == null) {
            preferencemodule = new PreferenceUtil();
        }
        if (prefs == null) {
            prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            editor = prefs.edit();
        }

        return preferencemodule;
    }

    public void putStringExtra(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringExtra(String key) {
        return prefs.getString(key, "");
    }

    public void removePreference(String key) {
        editor.remove(key).commit();
    }
}
