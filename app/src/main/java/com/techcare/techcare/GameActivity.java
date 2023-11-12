package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {
    private byte currentGameId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViewById(R.id.btnHomeGame).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnNextGame).setOnClickListener(v->{
            ImageView imageView = findViewById(R.id.imageViewGame);
            currentGameId++;
            if (currentGameId > 4) {
                currentGameId = 1;
            }
            // not a good way to do this, but it works temporarily
            switch (currentGameId) {
                case 1:
                    imageView.setImageResource(R.drawable.sudoku1);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.sudoku2);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.sudoku3);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.sudoku4);
                    break;
            }
        });
    }
}