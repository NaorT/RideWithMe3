package com.example.ridewithme;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

    private WebView F_webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        F_webView = (WebView) findViewById(R.id.web_View);
        F_webView.getSettings().setJavaScriptEnabled(true);
        F_webView.loadUrl("https://www.facebook.com/groups/114964358535991/");
    }
}
