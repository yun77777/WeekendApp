package com.example.myapplication;//package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CallFragment;
import com.example.myapplication.CameraFragment;
import com.example.myapplication.DTO.JoinData;
import com.example.myapplication.DTO.JoinResponse;
import com.example.myapplication.HomeFragment;
import com.example.myapplication.R;
import com.example.myapplication.SettingsFragment;
import com.example.myapplication.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();
    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    EditText et_email;
    EditText et_pw;
    EditText et_name;
    Button btn_signup;


    private final String URL = "http://3.37.87.71:5000";

    private Retrofit retrofit;
    private ApiService service;

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

        et_email = (EditText)findViewById(R.id.et_email);
        et_pw = (EditText)findViewById(R.id.et_pw);
        et_name = (EditText)findViewById(R.id.et_name);
        btn_signup = (Button)findViewById(R.id.btn_signup);


//        Gson gson = new GsonBuilder() .setLenient() .create();

//        retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        Log.d("retrofit:",retrofit.toString());
        Log.d("service:",service.toString());


    }

    private void SettingListener() {
//        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
        btn_signup.setOnClickListener(new View.OnClickListener() {

            //    private String url = "https://www.naver.com";
//private String url = "http://10.0.2.2:3000"; // instead of localhost
//    private String url = "file:///android_asset/index.html"; // instead of localhost

            @Override
            public void onClick(View view) {
//                new JSONTask().execute("http://3.37.87.71:5000/user/join");
//                Call<ResponseBody> call_post = service.postFunc("post data");
                String email = et_email.getText().toString();
                String name = et_email.getText().toString();
                String pw = et_email.getText().toString();

                Call<ResponseBody> call_post = service.postJoinFunc(new JoinData(pw,name,email));
//                Call<ResponseBody> call_post = service.postFunc("post data");
                call_post.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                Log.v(TAG, "result = " + result);
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.v(TAG, "error = " + String.valueOf(response.code()));
                            Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(TAG, "Fail");
                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
//                Call<ResponseBody> call_post = service.postJoinFunc(et_email.getText().toString());
//                call_post.enqueue(new Callback<JoinResponse>() {
//                    @Override
//                    public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
//                            JoinResponse result = response.body();
//
//                            Log.v(TAG, "result = " + result);
//                            Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
//                            if(result.getStatus() == 200) {
//                                finish();
//                            }
//                    }
//
//                    @Override
//                    public void onFailure(Call<JoinResponse> call, Throwable t) {
//                        Log.v(TAG, "Fail");
//                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });


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

