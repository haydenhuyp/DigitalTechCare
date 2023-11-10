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
        // Đặt giao diện của bạn để hiển thị WebView
        setContentView(R.layout.activity_webview);

        // go back to main activity when click on back button
        findViewById(R.id.btnBackWebView).setOnClickListener(v -> {
            // Log
            Log.w("WebViewActivity", "Back button clicked");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // Nếu bạn có một ID video YouTube cụ thể để tải, bạn có thể truyền nó như một extra trong intent
        String videoId = getIntent().getStringExtra("VIDEO_ID");
        webView.loadUrl("https://www.youtube.com/watch?v=" + videoId);

    }
}