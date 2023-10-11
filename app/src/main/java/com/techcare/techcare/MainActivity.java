package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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
            String id = "QVsCHTnt9mo";
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