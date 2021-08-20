package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TypingActivity extends AppCompatActivity {

    String outputName;
    Button typingNext;
    EditText typedEntry;
    String typed;
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typing);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        typedEntry = findViewById(R.id.typingEntry);
        typingNext = findViewById(R.id.typingNext);

        ConstraintLayout cLayout = findViewById(R.id.typing);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                Toast.makeText(TypingActivity.this, "tap detected", Toast.LENGTH_SHORT).show();
            }
        });

        typingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                typed = typedEntry.getText().toString();
                csvWriter.WriteAnswers(outputName, TypingActivity.this, timeStamps, "typed", typed, "three unique responses");
            }
        });

    }
}