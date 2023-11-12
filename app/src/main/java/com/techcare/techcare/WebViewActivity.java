package com.techcare.techcare;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private int currentVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.youtube.com/watch?v=" + "I1u6r7Kw9Go");
        // go back to main activity when click on back button
        findViewById(R.id.btnBackWebView).setOnClickListener(v -> {
            // set back the volume to the previous level
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        findViewById(R.id.btnMuteWebView).setOnClickListener(v -> {
            // store the current volume level in a variable
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            // mute
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            Toast.makeText(this, "Muted", Toast.LENGTH_SHORT).show();
        });
    }
}