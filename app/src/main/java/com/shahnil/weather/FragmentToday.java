package com.shahnil.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static com.shahnil.weather.FragmentSettings.key_celsius;
import static com.shahnil.weather.FragmentSettings.shared_prefs;
import static com.shahnil.weather.Welcome.key_username;

public class FragmentToday extends Fragment {


    private TextView mTVcityName, mTVtemp, mTVdate, mTVhumidity, mTVwind, mTVpressure, mTVweather;
    private TextView mUsername;

    private TextView mTVtempMin, mTVtempMax;
    private RequestQueue mQueue;


    public static final String key_latitude = "keylatitude";
    public static final String key_longitude = "keylongitude";

    private String Lat, Lon, unit;

    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getLocation();
                return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.layout_fragment_today, container, false);


        mTVweather = rootview.findViewById(R.id.main);

        mTVcityName = rootview.findViewById(R.id.TV_cityName);
        mTVtemp = rootview.findViewById(R.id.TV_temp);
        mTVdate = rootview.findViewById(R.id.current_date);
        mTVhumidity = rootview.findViewById(R.id.humidity);
        mTVpressure = rootview.findViewById(R.id.pressure);
        mTVwind = rootview.findViewById(R.id.wind);
        mTVtempMin = rootview.findViewById(R.id.temp_min);
        mTVtempMax = rootview.findViewById(R.id.temp_max);

        mUsername = rootview.findViewById(R.id.textView_username);

        mQueue = Volley.newRequestQueue(getActivity());


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                Lon = String.valueOf(location.getLongitude());
                Lat = String.valueOf(location.getLatitude());
                getWeather();

                SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(shared_prefs, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key_latitude, Lat);
                editor.putString(key_longitude, Lon);
                editor.apply();



            }


            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getActivity(), "GPS OFF:", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }


        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 5);


        } else {
            getLocation();
        }



        getWeather();

        return rootview;

    }


    private void getWeather() {



        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(shared_prefs, getContext().MODE_PRIVATE);
        if(prefs != null) {
            unit = prefs.getString(key_celsius, "units=metric");
            String username = prefs.getString(key_username, "user");
                mUsername.setText(username + " you are in ");

        }
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + Lat + "&lon=" + Lon + "&appid=d8c160f6c844f4ef7189cbbd32a0fd21&" + unit;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    String city = response.getString("name");

                    JSONArray jsonArrayWeek = response.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeek.getJSONObject(0);
                    String weather = String.valueOf(jsonObjectWeather.get("main"));

                    JSONObject jsonObject = response.getJSONObject("main");
                    String temp = String.valueOf(jsonObject.getDouble("temp"));
                    String tempMin = String.valueOf(jsonObject.getDouble("temp_min"));
                    String tempMax = String.valueOf(jsonObject.getDouble("temp_max"));

                    String humidity = String.valueOf(jsonObject.getDouble("humidity"));
                    String pressure = String.valueOf(jsonObject.getDouble("pressure"));

                    JSONObject jsonObjectWind = response.getJSONObject("wind");
                    String wind = String.valueOf(jsonObjectWind.getDouble("speed"));

                    long unixTimestamp = response.getLong("dt");
                    long javaTimestamp = unixTimestamp * 1000L;
                    Date d = new Date(javaTimestamp);
                    SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
                    f.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String s = f.format(d);
                    mTVdate.setText(s);


                    mTVweather.setText(weather);
                    mTVcityName.setText(city);
                    mTVtemp.setText(temp);
                    mTVtempMin.setText(tempMin);
                    mTVtempMax.setText(tempMax);

                    mTVpressure.setText(pressure + "  hPa");
                    mTVhumidity.setText(humidity + "% humidity");
                    mTVwind.setText(wind + " Km/h");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        mQueue.add(request);

    }

    private void getLocation() {


        locationManager.requestLocationUpdates("gps", 5000, 5000, locationListener);

    }

}



