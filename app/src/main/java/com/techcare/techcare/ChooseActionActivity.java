package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

public class ChooseActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);
        // TODO: The choose action screen doesn’t work. The GridView doesn’t work on real phone
        /*findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent(this, UserAppLayoutActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddVideoCall).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddVideoCallActivity.class);
            startActivity(intent);
        });*/
    }
}