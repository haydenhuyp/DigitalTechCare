package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

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

        ArrayList<Drawable> images = new ArrayList<Drawable>();
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

        findViewById(R.id.btnNextGame).setOnClickListener(v->{
            ImageView imageView = findViewById(R.id.imageViewGame);

            currentGameId++;
            if (currentGameId > 10) {
                currentGameId = 1;
            }
            // set image
            imageView.setImageDrawable(images.get(currentGameId - 1));
        });
    }
}