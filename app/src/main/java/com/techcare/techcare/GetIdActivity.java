package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GetIdActivity extends AppCompatActivity {
    private EditText txtIDGetID;
    private EditText txtNameGetID;
    private EditText txtTargetIDGetID;
    private EditText txtTargetNameGetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);
        txtIDGetID = findViewById(R.id.txtIDGetID);
        txtNameGetID = findViewById(R.id.txtNameGetID);
        txtTargetIDGetID = findViewById(R.id.txtTargetIDGetID);
        txtTargetNameGetID = findViewById(R.id.txtTargetNameGetID);

        findViewById(R.id.btnEnterGetID).setOnClickListener(v -> {
            // set currentUserID
            DataUtility.currentUserID = txtIDGetID.getText().toString();
            // set currentUserName
            DataUtility.currentUserName = txtNameGetID.getText().toString();
            // set targetUserID
            DataUtility.targetUserID = txtTargetIDGetID.getText().toString();
            // set targetUserName
            DataUtility.targetUserName = txtTargetNameGetID.getText().toString();
            // go to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}