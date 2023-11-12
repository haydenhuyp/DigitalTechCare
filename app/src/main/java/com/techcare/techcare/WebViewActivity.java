package com.techcare.techcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.youtube.com/watch?v=" + "I1u6r7Kw9Go");
        // channel ID: UCi6JtCVy4XKu4BSG-AE2chg
        // API Key: AIzaSyAVd2m5rqy4WBsjq7uZS8xt4BRuueQh1Qw
        // go back to main activity when click on back button
        findViewById(R.id.btnBackWebView).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}