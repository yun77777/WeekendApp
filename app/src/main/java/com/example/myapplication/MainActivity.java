package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private Button btn_pg;
    private Switch swc_push;

    private WebView webView;
//    private String url = "https://www.naver.com";
//private String url = "http://10.0.2.2:3000"; // instead of localhost
    private String url = "file:///android_asset/index.html"; // instead of localhost


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting for WebView
        webView = (WebView)findViewById(R.id.wv_main);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // FCM gets an device token for push notification
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                Log.d("token:", token);
            }
        });

        // setting for push notification on/off
        swc_push = (Switch) findViewById(R.id.swc_push);

        SharedPreferences sharedPreferences = getSharedPreferences("shared", 0);

        String push = sharedPreferences.getString("push", "");
        boolean bool = false;
        if (push.equals("true")) {
            bool = true;
        } else {
        }

        swc_push.setChecked(bool);

        swc_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("push", String.valueOf(b));
                editor.commit();
            }
        });


        // PG
        btn_pg = (Button) findViewById(R.id.btn_pg);
        btn_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PgActivity.class);
                startActivity(intent);
            }
        });

    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}