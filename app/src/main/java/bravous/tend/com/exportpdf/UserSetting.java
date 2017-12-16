package bravous.tend.com.exportpdf;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cloud on 2017-12-14.
 */

public class UserSetting {

    private final static String PREF_NAME = "user_setting";
    public final static String USER_EMAIL = "user_email";
    public final static String IS_CURRENT_NOTEBOOK = "current_notebook";
    public final static String COMMENT_ALARM = "comment_alarm";
    public final static String REGULAR_ALARM = "regular_alarm";
    public final static String ALARM_TIME = "alarm_time";
    public Context context;

    UserSetting(Context context){
        this.context = context;
    }


    public void put(String key,String value){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void put(String key,boolean value){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void put(String key,long value){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public String getValue(String key, String dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public Boolean getValue(String key, Boolean dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public long getValue(String key, long dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            return pref.getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

}
