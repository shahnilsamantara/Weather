package com.shahnil.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.shahnil.weather.Welcome.key_username;

public class FragmentSettings extends Fragment {


    public static final String key_celsius = "keycelsius";
    public static final String key_fahrenheit = "keyfahrenheit";


    public static final String shared_prefs = "sharedprefs";

    private String unit;

    private TextView textViewUser;

    private Button buttonCelsius, buttonFahrenheit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.layout_fragment_settings, container, false);


        textViewUser = rootview.findViewById(R.id.username);
        buttonCelsius = rootview.findViewById(R.id.celsius);
        buttonFahrenheit= rootview.findViewById(R.id.fahrenheit);

        buttonCelsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unit = "units=metric";

                SharedPreferences prefs = getActivity().getSharedPreferences(shared_prefs, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key_celsius,unit);
                editor.apply();
            }
        });


        buttonFahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unit = "units=imperial";
                SharedPreferences prefs = getActivity().getSharedPreferences(shared_prefs, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key_celsius,unit);
                editor.apply();

            }
        });


        SharedPreferences prefs = getActivity().getSharedPreferences(shared_prefs, getContext().MODE_PRIVATE);
        String username = prefs.getString(key_username, "user");

        textViewUser.setText("Hi "+ username);


        return rootview;

    }



}
