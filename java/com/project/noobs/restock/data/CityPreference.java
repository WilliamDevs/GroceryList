package com.project.noobs.restock.data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Noobs on 10/29/2016.
 */
public class CityPreference {

    SharedPreferences preferences;

    public CityPreference(Activity act){

        preferences = act.getPreferences(Activity.MODE_PRIVATE);
    }
    public String getCity(){
        return preferences.getString("city","St.Louis,US");
    }
    public void setCity(String city){
        preferences.edit().putString("city",city).apply();

    }
}
