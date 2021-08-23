package com.example.surveyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SurRefActivity extends AppCompatActivity {

    Button item;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortmem);

        item = findViewById(R.id.surRefCircle);

        ImageView imageView = (ImageView) findViewById(R.id.surRefImg);
        int imageResource = getResources().getIdentifier("@drawable/surrtest", null, this.getPackageName());
        imageView.setImageResource(imageResource);
        
    }
}
