package com.shahnil.weather;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.shahnil.weather.FragmentSettings.key_celsius;
import static com.shahnil.weather.FragmentSettings.shared_prefs;
import static com.shahnil.weather.FragmentToday.key_latitude;
import static com.shahnil.weather.FragmentToday.key_longitude;

public class FragmentDate extends Fragment implements DatePickerDialog.OnDateSetListener {





    private RequestQueue mQueue;

    private  Long  unixTime;
    private String Lat, Lon;

    private TextView textViewDate2, textViewTemp, textViewHumidity;

    private TextView textViewDate;

    private Spinner spinnerCity;


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        unixTime = c.getTimeInMillis() / 1000;
        //textViewDate.setText(String.valueOf( unixTime));
        getWeather();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.layout_fragment_date, container, false);


        textViewDate = (TextView) rootview.findViewById(R.id.date);

        textViewDate2 = rootview.findViewById(R.id.dateOld);
        textViewHumidity = rootview.findViewById(R.id.humidity);
        textViewTemp = rootview.findViewById(R.id.temp);

        spinnerCity=rootview.findViewById(R.id.spinner_city);



        Button button = (Button) rootview.findViewById(R.id.button_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    DialogFragment datePicker = new FragmentDatePicker();
                    datePicker.setTargetFragment(FragmentDate.this, 0);
                    datePicker.show(getFragmentManager(), "date picker");

               // DialogFragment datePicker = new FragmentDatePicker();
                //datePicker.show(getChildFragmentManager(),"date picker");
            }
        });

        mQueue = Volley.newRequestQueue(getActivity());

       // getWeather();




        return rootview;

    }

    private void getWeather() {

        String spinner=spinnerCity.getSelectedItem().toString();



        switch (spinner){

            case "Delhi":
                Lat = "28.7041";
                Lon = "77.1025";
                break;

            case "Mumbai":
                Lat = "19.0760";
                Lon = "72.8777";
                break;

            case "Noida":
                Lat = "28.5355";
                Lon = "77.3910";


        }

        SharedPreferences prefs = getActivity().getSharedPreferences(shared_prefs, getContext().MODE_PRIVATE);
        String unit = prefs.getString(key_celsius, "units=metric");





        String url = "https://api.openweathermap.org/data/2.5/onecall/timemachine?lat="+Lat+"&lon="+Lon+"&dt="+unixTime+"&appid=d8c160f6c844f4ef7189cbbd32a0fd21&"+unit;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    //String city = response.getString("name");

                    //JSONArray jsonArrayWeek = response.getJSONArray("daily");

                    JSONObject jsonObjectCurrent = response.getJSONObject("current");


                        long unixTimestamp = jsonObjectCurrent.getLong("dt");
                        long javaTimestamp = unixTimestamp * 1000L;
                        Date d = new Date(javaTimestamp);
                        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
                        f.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String date = f.format(d);

                        String temp = String.valueOf(jsonObjectCurrent.getDouble("temp"));

                        String humidity = String.valueOf(jsonObjectCurrent.getDouble("humidity"));

                        textViewDate2.setText(date);
                        textViewTemp.setText(temp);
                        textViewHumidity.setText(humidity);

                    //mAdaptor.notifyDataSetChanged();
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
