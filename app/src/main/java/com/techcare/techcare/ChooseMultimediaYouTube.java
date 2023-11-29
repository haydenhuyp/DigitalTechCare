package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChooseMultimediaYouTube extends AppCompatActivity {
    private Button btnDatePickerChooseMultimediaYoutube;
    private String timeString = "10:10 am, DEC 1 2023";
    TextInputEditText txtVideolink;
    CheckBox chkTurnOffAfter;
    CheckBox chkTurnOnAuto;
    TextInputEditText txtMinutes;
    private String name = "YouTube";
    private String icon = "youtube_logo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_multimedia_you_tube);
        btnDatePickerChooseMultimediaYoutube = findViewById(R.id.btndatePickerChooseMultimediaYoutube);
        btnDatePickerChooseMultimediaYoutube.setOnClickListener(v -> {
            showDatePickerDialog(v);
            showTimePickerDialog(v);
        });

        txtVideolink = findViewById(R.id.txtVideoLinkChooseMultimediaYoutube);
        chkTurnOffAfter = findViewById(R.id.chkTurnOffAfterChooseMultimediaYoutube);
        chkTurnOnAuto = findViewById(R.id.chkTurnOnAutomaticallyChooseMultimediaYoutube);
        txtMinutes = findViewById(R.id.txtMinutesChooseMultimediaYoutube);

        // process icon name and title

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String iconName = (extras.get("icon") != null) ? extras.get("icon").toString() : "youtube_logo";
            icon = iconName;
            name = extras.get("name") != null ? extras.get("name").toString() : "YouTube";
            if (iconName != null) {
                // change icon to iconName
                int resID = getResources().getIdentifier(iconName, "drawable", getPackageName());
                ImageButton imgbtn = findViewById(R.id.btnChooseIconChooseMultimediaYoutube);
                imgbtn.setImageResource(resID);
            }
        }

        findViewById(R.id.btnBackChooseMultimediaYoutube).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnChooseIconChooseMultimediaYoutube).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangeIconActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnSaveChooseMultimediaYoutube).setOnClickListener(v -> {
            StringBuilder builder = new StringBuilder();
            builder.append("video_link: " + txtVideolink.getText());

            // update the firebase database
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("gridCells").document("grid3").update("action", "YouTube");
            if (txtVideolink.getText() != null) db.collection("gridCells").document("grid3").update("actionParameter", txtVideolink.getText().toString());
            // capitalize the first letter of the name
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            db.collection("gridCells").document("grid3").update("title", name);
            db.collection("gridCells").document("grid3").update("icon", icon);

            Intent intent = new Intent(this, UserAppLayoutActivity.class);
            startActivity(intent);
        });
    }


    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                // format date to DEC 1 2023
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
                String formattedDate = dateFormat.format(calendar.getTime());
                btnDatePickerChooseMultimediaYoutube.setText(String.format("%s, %s", timeString, formattedDate.toString()));
            }
        },2023, 12, 1);

        datePickerDialog.show();
    }

    public void showTimePickerDialog(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour, minute) -> {
            // format time to 12:00 AM
            btnDatePickerChooseMultimediaYoutube.setText(String.format("%02d:%02d", hour, minute) + " AM");
            timeString = String.format("%02d:%02d", hour, minute) + " PM";
        }, 12, 0, false);

        timePickerDialog.show();
    }
}