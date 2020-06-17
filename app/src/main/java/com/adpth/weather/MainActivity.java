package com.adpth.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.androdocs.httprequest.HttpRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView temp, sunraise, sunsets, windsd, pressure, lon, lat, humidty, loc, description,date_data;
    EditText search;
    LinearLayout temp_data,condition_data;

    ImageView sunset_img,sunrise_img,humidity_img;

    ConstraintLayout constraintLayout;
    String API = "3dc8a8be15b581b5a6e240a35e2b647a";
    ImageButton search_btn;

    LottieAnimationView wind_anim,globe_anim,pressure_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = findViewById(R.id.temp);
        sunraise = findViewById(R.id.sunrise);
        sunsets = findViewById(R.id.sunset);
        windsd = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        humidty = findViewById(R.id.humidity);
        loc = findViewById(R.id.location);
        description = findViewById(R.id.desc);
        lon = findViewById(R.id.lon);
        lat = findViewById(R.id.lat);
        temp_data = findViewById(R.id.temp_data);
        condition_data = findViewById(R.id.condition_data);
        date_data =findViewById(R.id.time);

        wind_anim = findViewById(R.id.wind_anim);
        globe_anim = findViewById(R.id.globe_anim);
        pressure_anim = findViewById(R.id.pressure_anim);

        sunrise_img = findViewById(R.id.sunrise_img);
        sunset_img = findViewById(R.id.sunset_img);
        humidity_img = findViewById(R.id.humidity_img);

        Calendar calendar = Calendar.getInstance();
        String currentdata = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_data.setText(currentdata);

        search = findViewById(R.id.your_city);

        constraintLayout = findViewById(R.id.constraintLayout);

        search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert ConnectionManager != null;
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new WeatherData().execute();

                } else {
                    Snackbar snackbar = Snackbar.make(constraintLayout, "check your Internet connection", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });
    }

    class WeatherData extends AsyncTask<String, Void, String> {
        String City = search.getText().toString();
        @Override
        protected String doInBackground(String... strings) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + City + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject coor = jsonObj.getJSONObject("coord");
                    JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                    JSONObject sys = jsonObj.getJSONObject("sys");
                    JSONObject wind = jsonObj.getJSONObject("wind");

                    String latitude = coor.getString("lat");
                    String longitude = coor.getString("lon");
                    String temperature = main.getString("temp");
                    String humidity = main.getString("humidity");
                    String pres = main.getString("pressure");
                    String location_details = jsonObj.getString("name") + ", " + sys.getString("country");
                    long rise = sys.getLong("sunrise");
                    String sunrise = new SimpleDateFormat("HH:mm").format(new Date(rise * 1000));
                    long set = sys.getLong("sunset");
                    String sunset = new SimpleDateFormat("HH:mm").format(new Date(set * 1000));
                    String status = weather.getString("description");
                    String speed = wind.getString("speed");

                    pressure_anim.setVisibility(View.VISIBLE);
                    pressure_anim.playAnimation();
                    pressure.setVisibility(View.VISIBLE);
                    pressure.setText(pres);

                    temp_data.setVisibility(View.VISIBLE);
                    temp.setText(temperature);

                    humidity_img.setVisibility(View.VISIBLE);
                    humidty.setVisibility(View.VISIBLE);
                    humidty.setText(humidity);

                    loc.setVisibility(View.VISIBLE);
                    loc.setText(location_details);

                    sunrise_img.setVisibility(View.VISIBLE);
                    sunraise.setVisibility(View.VISIBLE);
                    sunraise.setText(sunrise);

                    sunset_img.setVisibility(View.VISIBLE);
                    sunsets.setVisibility(View.VISIBLE);
                    sunsets.setText(sunset);

                    condition_data.setVisibility(View.VISIBLE);
                    description.setText(status);

                    wind_anim.playAnimation();
                    windsd.setVisibility(View.VISIBLE);
                    windsd.setText(speed);

                    globe_anim.setVisibility(View.VISIBLE);
                    globe_anim.playAnimation();
                    lat.setVisibility(View.VISIBLE);
                    lat.setText(latitude);

                    lon.setVisibility(View.VISIBLE);
                    lon.setText(longitude);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }
}