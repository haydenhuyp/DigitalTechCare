package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keeps screen on

        findViewById(R.id.btn_call).setOnClickListener(v -> {
            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        });
        /* Another way to do it by using webview: https://www.youtube.com/watch?v=V2KCAfHjySQ&ab_channel=EverydayProgrammer */
        findViewById(R.id.btn_youtube).setOnClickListener(v -> {
            String id = "pPKMMlrFdeM";
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        });
    }
}