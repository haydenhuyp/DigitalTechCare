package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private void showYoutubeVideoInWebView(String url) {
        setContentView(R.layout.activity_webview); //

        WebView webView = findViewById(R.id.webview); //
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // when button is clicked, starts callActivity

        findViewById(R.id.btn_call).setOnClickListener(v -> {
            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_youtube).setOnClickListener(v -> {
            // Duy's code for Webview Youtube link
            String id = "QVsCHTnt9mo";
            String url = "https://www.youtube.com/watch?v=" + id;
            showYoutubeVideoInWebView(url);
           /* Huy's code for accessing Youtube app
            String id = "QVsCHTnt9mo";
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }*/
        });
    }
}