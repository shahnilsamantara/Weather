package com.shahnil.weather;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.shahnil.weather.FragmentSettings.shared_prefs;

public class Welcome extends AppCompatActivity {

    public static final String shared_prefs = "sharedprefs";

    public static final String key_username = "keyusername";

    private TextInputLayout editTextUser;
    private Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        editTextUser =  findViewById(R.id.editText_user);

        button = findViewById(R.id.submit_button);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                openapp();
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openapp() {
        String username = editTextUser.getEditText().getText().toString().trim();

        if (username.isEmpty()) {
            editTextUser.setError("Username is required");
            editTextUser.requestFocus();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key_username,username);
        editor.apply();

        Intent intent = new Intent(Welcome.this, MainActivity.class);
        startActivity(intent);




    }
}