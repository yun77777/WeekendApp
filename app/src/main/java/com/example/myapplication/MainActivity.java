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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.bootpay.BootpayWebView;
import kr.co.bootpay.listener.EventListener;

public class MainActivity extends AppCompatActivity {
    private Button btn_pg;
    private Switch swc_push;

    private BootpayWebView webview;
//    private String url = "https://www.naver.com";
//private String url = "http://10.0.2.2:3000"; // instead of localhost
//    private String url = "file:///android_asset/index.html"; // instead of localhost

    final String url = "http://3.37.87.71:3000/";

    void doJavascript(String script) {
        final String str = script;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:(function(){" + str + "})()");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (BootpayWebView)findViewById(R.id.wv_main);
//        webview.addJavascriptInterface(new WebAppInterface(this), "Android");

        webview.setOnResponseListener(new EventListener() {
            @Override
            public void onError(String data) {
                System.out.println("bootpay error");
                System.out.println(data);
            }

            @Override
            public void onCancel(String data) {
                System.out.println("bootpay cancel");
                System.out.println(data);
            }

            @Override
            public void onClose(String data) {
                System.out.println("bootpay close");
                System.out.println(data);
            }

            @Override
            public void onReady(String data) {
                System.out.println("bootpay ready");
                System.out.println(data);
            }

            @Override
            public void onConfirm(String data) {
                boolean iWantPay = true;
                if(iWantPay == true) { // 재고가 있을 경우
                    doJavascript("BootPay.transactionConfirm( " + data + ");");
                } else {
                    doJavascript("BootPay.removePaymentWindow();");
                }
            }

            @Override
            public void onDone(String data) {
                System.out.println("bootpay done");
                System.out.println(data);

            }
        });
        webview.loadUrl(url);

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
        if((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}