package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // when button is clicked, starts callActivity

        findViewById(R.id.call_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        });

    }
}