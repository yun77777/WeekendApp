package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

import javax.xml.transform.ErrorListener;

import kr.co.bootpay.BootpayWebView;
import kr.co.bootpay.listener.EventListener;


public class PgActivity extends AppCompatActivity {
        BootpayWebView webview;
        final String url = "https://{개발하신 웹 페이지 주소}";

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pg);

            webview = findViewById(R.id.wv_pg);
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

        void doJavascript(String script) {
            final String str = script;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webview.loadUrl("javascript:(function(){" + str + "})()");
                }
            });
        }
    }