package com.example.myapplication.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication.R;

import kr.co.bootpay.BootpayWebView;
import kr.co.bootpay.listener.EventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BootpayWebView webview;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Log.d("back button is pressed","in fragment");
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //         android:id="@+id/wv_home"

        webview = view.findViewById(R.id.wv_home);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//
//        webView.loadUrl("file:///android_asset/index.html");


        webview = view.findViewById(R.id.wv_home);
        webview.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("sssss", url);
                try { /** * 201229 * 카카오링크 오류 수정을 위해 아래 if문을 추가함. */
                    if (url != null && url.startsWith("intent://kakaopay/")) {
                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            Intent existPackage = getActivity().getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                            if (existPackage != null) { startActivity(intent); }
                            else { Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                marketIntent.setData(Uri.parse("pg?url=" + intent.getPackage()));
                                startActivity(marketIntent); } return true; } catch (Exception e) { e.printStackTrace(); } } }
                catch (Exception e) { e.printStackTrace(); return false; } return false; }

        });

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

        webview.loadUrl("file:///android_asset/index.html");

//        return inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    void doJavascript(String script) {
        final String str = script;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:(function(){" + str + "})()");
            }
        });
    }
}