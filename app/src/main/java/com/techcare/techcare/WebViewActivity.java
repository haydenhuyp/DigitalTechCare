package com.techcare.techcare;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private int currentVolume;
    private boolean isMuted = false;

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
            if (isMuted) {
                // unmute
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                Toast.makeText(this, "Unmuted", Toast.LENGTH_SHORT).show();
                isMuted = false;
                TextView textView = findViewById(R.id.txtMuteUnmute);
                textView.setText("Mute");
                ImageView imageView = findViewById(R.id.imgMuteUnmute);
                imageView.setImageResource(R.drawable.ic_volume);
            }
            else {
                // mute
                isMuted = true;
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                Toast.makeText(this, "Muted", Toast.LENGTH_SHORT).show();
                TextView textView = findViewById(R.id.txtMuteUnmute);
                textView.setText("Unmute");
                ImageView imageView = findViewById(R.id.imgMuteUnmute);
                imageView.setImageResource(R.drawable.ic_mute);
            }
        });
    }
}