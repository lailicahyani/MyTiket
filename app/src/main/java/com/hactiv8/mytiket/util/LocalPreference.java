package com.hactiv8.mytiket.util;

import static android.content.Context.MODE_PRIVATE;
import static com.hactiv8.mytiket.util.Constant.TITLE_VIW_ONLY;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalPreference {
    private static LocalPreference instance;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public LocalPreference(Context context) {
        preferences = context.getSharedPreferences(TITLE_VIW_ONLY, MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static LocalPreference getInstance(Context context) {
        if(instance == null) {
            instance = new LocalPreference(context);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}