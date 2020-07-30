package com.shahnil.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.shahnil.weather.FragmentSettings.key_celsius;
import static com.shahnil.weather.FragmentSettings.shared_prefs;
import static com.shahnil.weather.FragmentToday.key_latitude;
import static com.shahnil.weather.FragmentToday.key_longitude;
import static com.shahnil.weather.Welcome.key_username;

public class FragmentWeek extends Fragment {



    private RecyclerView mRecyclerView;
    private RecyclerAdaptor mAdaptor;
    private RecyclerView.LayoutManager mLayoutManager ;
    private TextView mUsername;



    private ArrayList<WeekData> weeklist;


    private RequestQueue mQueue;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.layout_fragment_week, container, false);





        weeklist = new ArrayList<>();
        mRecyclerView = rootview.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdaptor = new RecyclerAdaptor(getActivity(),weeklist);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdaptor);

        mUsername = rootview.findViewById(R.id.username);

        mQueue = Volley.newRequestQueue(getActivity());

        getWeather();


        return rootview;

    }

    private void getWeather() {


        SharedPreferences prefs = getActivity().getSharedPreferences(shared_prefs, getContext().MODE_PRIVATE);
        String Lat = prefs.getString(key_latitude, "0");
        String Lon = prefs.getString(key_longitude, "0");
        String unit = prefs.getString(key_celsius, "units=metric");
        String username = prefs.getString(key_username, "user");

        mUsername.setText(username);


        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + Lat + "&lon=" + Lon + "&%20exclude=minutely,hourly,current&appid=d8c160f6c844f4ef7189cbbd32a0fd21&"+ unit;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONArray jsonArrayWeek = response.getJSONArray("daily");

                    for(int i=0; i<jsonArrayWeek.length(); i++ ){
                        JSONObject day = jsonArrayWeek.getJSONObject(i);

                        long unixTimestamp = day.getLong("dt");
                        long javaTimestamp = unixTimestamp * 1000L;
                        Date d = new Date(javaTimestamp);
                        SimpleDateFormat f = new SimpleDateFormat("EEE, dd.MM.yyyy");
                        f.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String date = f.format(d);

                        JSONObject jsonTemp = day.getJSONObject("temp");
                        String temp = String.valueOf(jsonTemp.getDouble("day"));

                        String humidity = String.valueOf(day.getLong("humidity"));
                        String wind = String.valueOf(day.getLong("wind_speed"));




                        weeklist.add(new WeekData(date,temp,humidity,wind,null));



                    }

                    mAdaptor.notifyDataSetChanged();
                  //  mTVdate.setText(s);
                   // mTVcityName.setText(city);
                  //  mTVtemp.setText(temp);





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


}
