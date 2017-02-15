package com.project.noobs.restock.data;

import com.project.noobs.restock.model.Place;
import com.project.noobs.restock.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.noobs.restock.Util.Utils;

/**
 * Created by Noobs on 10/19/2016.
 */
public class JSONWeatherParser {

    public static Weather getWeather(String data){
        Weather weather = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(data);
            Place place = new Place();
            JSONObject coords = Utils.getObject("coord",jsonObject);
            place.setLat(Utils.getFloat("lat",coords));
            place.setLon(Utils.getFloat("lon",coords));

            JSONObject systemObject = Utils.getObject("sys",jsonObject);
            place.setCountry(Utils.getString("country",systemObject));
            place.setLastupdate(Utils.getInt("dt",jsonObject));
            place.setCity(Utils.getString("name",jsonObject));
            weather.place = place;

            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id",jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description",jsonWeather));
            weather.currentCondition.setCondiction(Utils.getString("main",jsonWeather));


            JSONObject mainObject = Utils.getObject("main",jsonObject);
            weather.currentCondition.setTemperature(Utils.getDouble("temp",mainObject));

            JSONObject windObject = Utils.getObject("wind",jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed",windObject));
            weather.wind.setDeg(Utils.getFloat("deg",windObject));

            JSONObject cloudObject = Utils.getObject("clouds",jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all",cloudObject));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
