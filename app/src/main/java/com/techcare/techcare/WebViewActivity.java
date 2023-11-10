package com.techcare.techcare;

import android.os.Bundle;
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

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // Nếu bạn có một ID video YouTube cụ thể để tải, bạn có thể truyền nó như một extra trong intent
        String videoId = getIntent().getStringExtra("VIDEO_ID");
        webView.loadUrl("https://www.youtube.com/watch?v=" + videoId);
    }

    // Xử lý nút quay lại cho WebView
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}