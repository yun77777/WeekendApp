package com.example.myapplication.Activity;//package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.DTO.LoginData;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    final String TAG = this.getClass().getSimpleName();

    EditText et_email;
    EditText et_pw;
    Button btn_login;
    Button btn_signup;

//    private final String URL = "http://3.37.87.71:5000";
    private final String URL = "http://10.0.2.2:5000";

    private Retrofit retrofit;
    private ApiService service;

    private String email;
    private String pw;
    private String device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        SettingListener();

    }

    private void init() {
        et_email = (EditText)findViewById(R.id.et_email);
        et_pw = (EditText)findViewById(R.id.et_pw);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signup = (Button)findViewById(R.id.btn_signup);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                device = token;
                Log.d("device token:", token);
            }
        });

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

            case R.id.btn_login:
                email = et_email.getText().toString();
                pw = et_pw.getText().toString();
                Log.d("email:", email);
                Log.d("pw:", pw);
                Log.d("device:", device);

                LoginData user = new LoginData(pw, email, device);
                Call<ResponseBody> call_post = service.postLoginFunc(user);
                call_post.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            String result = null;
                            try {
                                result = response.body().string();
                                JsonParser jsonParser = new JsonParser();
                                JsonElement element = jsonParser.parse(result);

                                String msg = element.getAsJsonObject().get("msg").getAsString();
                                String status = element.getAsJsonObject().get("status") != null ? element.getAsJsonObject().get("status").getAsString() : null;
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                System.out.println("msg : " + msg);

                                if(status == null) return;
                                switch(status) {
                                    case "login":
                                        Log.d("SUCCESSFULL LOGGED IN", status);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                        intent.putExtra("email",email);
//                                        intent.putExtra("device", device);

                                        SharedPreferences sp = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("email", email);
                                        editor.putString("device", device);
                                        editor.apply();

                                        Log.d("login activity email:", email);
                                        Log.d("login activity device:", device);

                                        startActivity(intent);
                                        break;
                                    case "signup":
                                        intent = new Intent(LoginActivity.this, JoinActivity.class);
                                        intent.putExtra("device", device);
                                        Log.d("login activity email:", email);
                                        Log.d("login activity device:", device);
                                        startActivity(intent);
                                        break;
                                    default:
                                        break;
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            Log.v(TAG, "result = " + result);
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
                break;
            case R.id.btn_signup:
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                intent.putExtra("device", device);
                Log.d("login activity device:", device);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void SettingListener() {
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }
}

