package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ChooseMultimediaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_multimedia);

        findViewById(R.id.btnAddYoutubeVideoChooseMultimedia).setOnClickListener(v -> {

        });

        findViewById(R.id.btnBackMultimedia).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });
    }
}