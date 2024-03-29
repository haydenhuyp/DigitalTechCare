package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private byte currentGameId = 0;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        findViewById(R.id.btnHomeGame).setOnClickListener(v -> {
            finish();
        });

        imageView = findViewById(R.id.imageViewGame);

        ArrayList<Drawable> images = new ArrayList<Drawable>();
        images.add(getResources().getDrawable(R.drawable.crossword1));
        images.add(getResources().getDrawable(R.drawable.crossword2));
        images.add(getResources().getDrawable(R.drawable.sudoku1));
        images.add(getResources().getDrawable(R.drawable.sudoku2));
        images.add(getResources().getDrawable(R.drawable.sudoku3));
        images.add(getResources().getDrawable(R.drawable.sudoku4));
        images.add(getResources().getDrawable(R.drawable.sudoku5));
        images.add(getResources().getDrawable(R.drawable.sudoku6));
        images.add(getResources().getDrawable(R.drawable.sudoku7));
        images.add(getResources().getDrawable(R.drawable.sudoku8));
        images.add(getResources().getDrawable(R.drawable.sudoku9));
        images.add(getResources().getDrawable(R.drawable.sudoku10));

        imageView.setImageDrawable(images.get(currentGameId));

        findViewById(R.id.btnNextGame).setOnClickListener(v->{
            currentGameId++;
            if (currentGameId > 1 && currentGameId < 12){
                imageView.setScaleX(1.5f);
                imageView.setScaleY(1.5f);
            } else {
                imageView.setScaleX(1.0f);
                imageView.setScaleY(1.0f);
            }
            if (currentGameId > 11) {
                currentGameId = 0;
            }
            // set image
            imageView.setImageDrawable(images.get(currentGameId));
        });
    }
}