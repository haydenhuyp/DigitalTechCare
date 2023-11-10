package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class UserAppLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_app_layout);

        findViewById(R.id.btnAddLayout1).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout2).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddVideoCallActivity.class);
            startActivity(intent);
        });
    }
}