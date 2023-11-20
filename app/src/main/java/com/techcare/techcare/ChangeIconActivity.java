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

        findViewById(R.id.btnMassChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);
            // pass the icon name to the next activity
            intent.putExtra("icon", "ic_church");
            intent.putExtra("name", "Mass");
            startActivity(intent);
        });

        findViewById(R.id.btnBookChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);

            intent.putExtra("icon", "ic_book");
            intent.putExtra("name", "book");
            startActivity(intent);
        });

        findViewById(R.id.btnMusicChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);

            intent.putExtra("icon", "ic_music");
            intent.putExtra("name", "music");
            startActivity(intent);
        });
        findViewById(R.id.btnLiveTVChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);

            intent.putExtra("icon", "ic_tv");
            intent.putExtra("name", "Live TV");
            startActivity(intent);
        });
        findViewById(R.id.btnWeatherChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);

            intent.putExtra("icon", "ic_weather");
            intent.putExtra("name", "weather");
            startActivity(intent);
        });

        findViewById(R.id.btnYouTubeChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);

            intent.putExtra("icon", "youtube_logo");
            intent.putExtra("name", "YouTube");
            startActivity(intent);
        });

        findViewById(R.id.btnNewsChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);

            intent.putExtra("icon", "ic_news");
            intent.putExtra("name", "News");
            startActivity(intent);
        });

        findViewById(R.id.btnTravelChangeIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaYouTube.class);

            intent.putExtra("icon", "ic_plane");
            intent.putExtra("name", "Travel");
            startActivity(intent);
        });
    }
}