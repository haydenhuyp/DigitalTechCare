package com.techcare.techcare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChooseMultimediaYouTube extends AppCompatActivity {
    private Button btnDatePickerChooseMultimediaYoutube;
    private String timeString = "10:10 am, DEC 1 2023";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_multimedia_you_tube);
        btnDatePickerChooseMultimediaYoutube = findViewById(R.id.btndatePickerChooseMultimediaYoutube);
        btnDatePickerChooseMultimediaYoutube.setOnClickListener(v -> {
            showDatePickerDialog(v);
            showTimePickerDialog(v);
        });

        findViewById(R.id.btnBackChooseMultimediaYoutube).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseMultimediaActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnChooseIconChooseMultimediaYoutube).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangeIconActivity.class);
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