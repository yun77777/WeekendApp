package com.example.myapplication.Activity;//package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.DTO.JoinData;
import com.example.myapplication.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener{
    final String TAG = this.getClass().getSimpleName();

    EditText et_email;
    EditText et_pw;
    EditText et_name;
    Button btn_signup;

//    private final String URL = "http://3.37.87.71:5000";
    private final String URL = "http://10.0.2.2:5000";

    private Retrofit retrofit;
    private ApiService service;

    private String device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();
        SettingListener();

    }

    private void init() {
        Intent intent = getIntent();
        device = intent.getStringExtra("device");

        et_email = (EditText)findViewById(R.id.et_email);
        et_pw = (EditText)findViewById(R.id.et_pw);
        et_name = (EditText)findViewById(R.id.et_name);
        btn_signup = (Button)findViewById(R.id.btn_signup);


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

            case R.id.btn_signup:
                String email = et_email.getText().toString();
                String name = et_name.getText().toString();
                String pw = et_pw.getText().toString();

                Call<ResponseBody> call_post = service.postJoinFunc(new JoinData(pw, name, email, device));
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
                                        Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("device", device);

                                        startActivity(intent);
                                        break;
                                    case "signup":
                                        intent = new Intent(JoinActivity.this, JoinActivity.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("device", device);

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

            default:
                break;
        }
    }

    private void SettingListener() {
        btn_signup.setOnClickListener(this);
    }
}

