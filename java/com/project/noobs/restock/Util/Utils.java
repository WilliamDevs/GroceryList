package com.project.noobs.restock.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Noobs on 10/19/2016.
 */
public class Utils {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String APP_ID = "&appid=48ee2728a5c8d1781a1ea9984f428b0e";


    public static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException{
        JSONObject jsonObject1 = jsonObject.getJSONObject(tagName);
        return  jsonObject1;
    }
    public static String getString(String tagName,JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    public static float getFloat(String tagName,JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    public static double getDouble(String tagName,JSONObject jsonObject)throws JSONException{
        return (float) jsonObject.getDouble(tagName);
    }

    public static int getInt(String tagName,JSONObject jsonObject) throws JSONException{
        return jsonObject.getInt(tagName);
    }

}
