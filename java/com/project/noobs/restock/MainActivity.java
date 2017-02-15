package com.project.noobs.restock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.project.noobs.restock.data.CityPreference;
import com.project.noobs.restock.data.JSONWeatherParser;
import com.project.noobs.restock.data.WeatherHttpClient;
import com.project.noobs.restock.model.Weather;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView cityName,temp,description,humidity,wind,update;
    Weather weather = new Weather();


    Button go;
    LinearLayout main;
    int h,ho,s,count,supercount,hour;
    ImageView weatheranim;
    TextClock tc;
    String errorM;
    Activity activity = this;
    Window window = activity.getWindow();
    Toolbar myToolbar;
    int precolor,precolor2;
    SharedPreferences colorpref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //  main = (LinearLayout)findViewById(R.id.main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Integer One = Color.parseColor("#303F9F");
            activity.getWindow().setStatusBarColor(One);
        }
        myToolbar = (Toolbar) findViewById(R.id.change);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        tc = (TextClock) findViewById(R.id.clock);
        cityName = (TextView)findViewById(R.id.cityName);
        temp = (TextView)findViewById(R.id.temp);
        description = (TextView)findViewById(R.id.description);
        update = (TextView)findViewById(R.id.lastUpdate);
        go =(Button)findViewById(R.id.go);
        weatheranim = (ImageView)findViewById(R.id.weatheranim);

        go.setOnClickListener(this);


        cityName.setTranslationY(50f);
        cityName.setAlpha(0);
        temp.setTranslationX(-50f);
        temp.setAlpha(0);
        description.setTranslationY(-50f);
        description.setAlpha(0);
        update.setTranslationY(75f);
        update.setAlpha(0);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/caviardreams.ttf");
        tc.setTypeface(custom_font);

        CityPreference preference = new CityPreference(this);
        getWeatherData(preference.getCity());





    }
    public void getWeatherData(String city){
       WeatherTask weatherTask = new WeatherTask();
        Log.d("LuancherTag","City is "+ city);
        if(city==null){
            city="St.Louis,Us";
        }
        weatherTask.execute(city+"&units=metric");
    }



    private class WeatherTask extends AsyncTask<String,Void,Weather>{

        @Override
        protected Weather doInBackground(String... params) {
            String data = ((new WeatherHttpClient())).getWeatherData(params[0]);
            if(weather==null){
             return null;
            }else {
                weather = JSONWeatherParser.getWeather(data);
                Log.d("LauncherTag","Weather = " +weather);
                return weather;
            }
        }

        @Override
        protected void onPostExecute(final Weather weather) {
            super.onPostExecute(weather);
            DateFormat df = DateFormat.getTimeInstance();
            Log.d("LauncherTag","Weather = " +weather);
            if(weather==null){

                cityName.setText(R.string.tryagain);
                cityName.animate().alpha(1).translationY(0f).setDuration(1000);
            }else {
                final String lastUpdated = df.format(new Date(weather.place.getLastupdate()));

                DecimalFormat dc = new DecimalFormat("#.#");
                String tempFormat = dc.format(weather.currentCondition.getTemperature() * 1.8 + 32);

                cityName.setText(weather.place.getCity());
                temp.setText(tempFormat + "ºF");
                description.setText(weather.currentCondition.getDescription());
                update.setText("Last Updated: " + lastUpdated);
                cityName.animate().alpha(1).translationY(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        temp.animate().alpha(1).translationX(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                description.animate().alpha(1).translationY(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        update.animate().alpha(1).translationY(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                String desc = weather.currentCondition.getDescription();
                                                getAnimation(desc);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });

            }
        }
    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change the City");

        final EditText cityInput = new EditText(this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("St.Louis,US");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityPreference preference = new CityPreference(MainActivity.this);
                boolean correctinput = getCityInput(cityInput.getText().toString());
                if(correctinput) {
                    preference.setCity(cityInput.getText().toString());
                    String city = preference.getCity();
                    getWeatherData(city);
                }else {
                    showDialog();
                    Toast.makeText(MainActivity.this,errorM,Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.show();
    }
    public boolean getCityInput(String input){
        if(input.equals("")){
            int error=0;
            errorString(error);
            return false;
        }else if(input.contains(" ")){
            int error = 1;
            errorString(error);
            return false;
        }else if(input.matches(".*\\d+.*")){
            int error = 2;
            errorString(error);
            return false;

        }else {

            return true;
        }
    }
    public void errorString(int error){
        if(error==0){
            errorM = "Please enter a city";
        }else if(error==1){
            errorM = "Please leave out spaces";
        }else {
            errorM = "Please leave out numbers";

        }
    }

    public void getAnimation(String condition){
        if(condition.contains("rain")){
            weatheranim.setBackgroundResource(R.drawable.rain010);
            weatheranim.setBackgroundResource(R.drawable.rain);
            AnimationDrawable anim = (AnimationDrawable) weatheranim.getBackground();
            anim.start();
            Integer One = Color.parseColor("#303F9F");
            Integer Two = Color.parseColor("#303F9F");
            Integer Three = Color.parseColor("#3F51B5");
            Integer Four = Color.parseColor("#3F51B5");
            changeBackground(One,Two,Three,Four);
            Log.d("LauncherTag","condition is " + condition);
        }else  if (condition.contains("snow")) {

            weatheranim.setBackgroundResource(R.drawable.snow003);
            weatheranim.setBackgroundResource(R.drawable.snow);
            AnimationDrawable anim = (AnimationDrawable) weatheranim.getBackground();
            anim.start();
            Integer One = Color.parseColor("#303F9F");
            Integer Two = Color.parseColor("#616161");
            Integer Three = Color.parseColor("#3F51B5");
            Integer Four = Color.parseColor("#9e9e9e");
            changeBackground(One,Two,Three,Four);
        }else  if (condition.contains("clear")){
            weatheranim.setBackgroundResource(R.drawable.sun002);
            weatheranim.setBackgroundResource(R.drawable.sun);
            AnimationDrawable anim = (AnimationDrawable) weatheranim.getBackground();
            anim.start();
            Integer One = Color.parseColor("#303F9F");
            Integer Two = Color.parseColor("#ffeb3b");
            Integer Three = Color.parseColor("#3F51B5");
            Integer Four = Color.parseColor("#FBC02D");
            changeBackground(One,Two,Three,Four);

        }else if (condition.contains("cloud")){
            weatheranim.setBackgroundResource(R.drawable.cloud035);
            weatheranim.setBackgroundResource(R.drawable.clouds);
            AnimationDrawable anim = (AnimationDrawable) weatheranim.getBackground();
            anim.start();
            Integer One = Color.parseColor("#303F9F");
            Integer Two = Color.parseColor("#9e9e9e");
            Integer Three = Color.parseColor("#3F51B5");
            Integer Four = Color.parseColor("#616161");
            changeBackground(One,Two,Three,Four);

        }
       // changeBackground(R.color.colorPrimaryDark,R.color.sunSecondary,R.color.colorPrimary,R.color.sunPrimary);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final int dark = Color.DKGRAY;
        final int day = Color.CYAN;
        final int three = Color.MAGENTA;
        final int midday = Color.RED;
        final int dawn = Color.DKGRAY;
        Calendar c = Calendar.getInstance();
        h = c.HOUR;
        ho = c.HOUR_OF_DAY;
        s = c.MINUTE;
        Log.d("LauncherTag", "The time is  "+ h +" "+ ho + " " + s);


    }

    public void changeBackground(int One,final int Two,int Three,final int Four){
        precolor = One;
        precolor2 = Three;

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),  activity.getWindow().getStatusBarColor(), Two);
        ValueAnimator colorStatusAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), myToolbar.getDrawingCacheBackgroundColor(), Four);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {


                myToolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
                go.setBackgroundColor((Integer) animator.getAnimatedValue());
                SharedPreferences sharedPreferences = getSharedPreferences("Color",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("status",activity.getWindow().getStatusBarColor());
                editor.putInt("tool",Two);
                editor.apply();

            }
        });

        colorStatusAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                }
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    //    tintManager.setStatusBarTintColor((Integer) animator.getAnimatedValue());
                }
            }
        });

        colorAnimation.setDuration(1300);
        colorAnimation.setStartDelay(0);
        colorAnimation.start();
        colorStatusAnimation.setDuration(1300);
        colorStatusAnimation.setStartDelay(0);
        colorStatusAnimation.start();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.go:
                Intent in = new Intent(this,DisplayList.class);
                startActivity(in);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.change_city, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showDialog();
        return super.onOptionsItemSelected(item);

    }
}
