package com.example.myapplication.Activity;//package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.Fragment.CallFragment;
import com.example.myapplication.Fragment.CameraFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.R;
import com.example.myapplication.Fragment.SettingsFragment;
import com.example.myapplication.Fragment.UserFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    final String TAG = this.getClass().getSimpleName();
    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    private final String URL = "http://10.0.2.2:5000";

    private Retrofit retrofit;
    private ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        SettingListener();

    }

    private void init() {
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new HomeFragment()).commit();

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        Log.d("retrofit:",retrofit.toString());
        Log.d("service:",service.toString());
    }

    @Override
    public void onClick(View v) {
        Log.d("v.getId():", String.valueOf(v.getId()));
        switch (v.getId()) {

            default:
                break;
        }
    }

    private void SettingListener() {

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Log.e("id: ", String.valueOf(id));
                switch (id) {
                    case R.id.tab_home: {
                        Log.e("home: ", String.valueOf(id));

                        getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new HomeFragment()).commit();
                        return true;
                    }
                    case R.id.tab_camera: {
                        Log.e("camera: ", String.valueOf(id));

                        getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new CameraFragment()).commit();
                        return true;
                    }
                    case R.id.tab_call: {
                        Log.e("call: ", String.valueOf(id));

                        getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new CallFragment()).commit();
                        return true;
                    }
                    case R.id.tab_user: {
                        Log.e("user: ", String.valueOf(id));

                        getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new UserFragment()).commit();
                        return true;
                    }
                    case R.id.tab_settings: {
                        Log.e("settings: ", String.valueOf(id));

                        getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new SettingsFragment()).commit();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}

