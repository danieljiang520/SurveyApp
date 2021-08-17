package com.example.surveyapp;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetTimeStamp extends AppCompatActivity {

    // variables we need to write
    String firstClick = "N/A";
    String lastClick;
    String pageSubmit;
    int clickCount;

    // updates first click, last click, page submit, click count
    public void updateTimeStamp(){
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        lastClick = pageSubmit;
        pageSubmit = currentTime;
        if(firstClick == "N/A"){
            firstClick = currentTime;
            lastClick = currentTime;
        }
        ++clickCount;
    }
}
