package com.example.myapplication;//package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CallFragment;
import com.example.myapplication.CameraFragment;
import com.example.myapplication.HomeFragment;
import com.example.myapplication.R;
import com.example.myapplication.SettingsFragment;
import com.example.myapplication.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();
    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SettingListener();
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    private void init() {
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener() {
//        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());

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

//    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.tab_home: {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new HomeFragment()).commit();
//                    return true;
//                }
//                case R.id.tab_camera: {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new CameraFragment()).commit();
//                    return true;
//                }
//                case R.id.tab_call: {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new CallFragment()).commit();
//                    return true;
//                }
//                case R.id.tab_user: {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new UserFragment()).commit();
//                    return true;
//                }
//                case R.id.tab_settings: {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new SettingsFragment()).commit();
//                    return true;
//                }
//            }
//            return false;
//        }
//    }
}

