package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ChangeIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_icon);

        findViewById(R.id.btnBackChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);
            startActivity(intent);
        });
    }
}