package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SpatReasonActivity extends AppCompatActivity {

    private static final String TAG = "SpatReasonActivity";

    String outputName;
    Button spatReasonNext;
    MultipleChoice.MCButton choice1 = new MultipleChoice.MCButton();
    MultipleChoice.MCButton choice2 = new MultipleChoice.MCButton();
    MultipleChoice.MCButton choice3 = new MultipleChoice.MCButton();
    MultipleChoice.MCButton choice4 = new MultipleChoice.MCButton();
    MultipleChoice.MCButton choice5 = new MultipleChoice.MCButton();
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();
    MultipleChoice multChoice = new MultipleChoice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spatreason);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        String outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // matches buttons with xml id
        spatReasonNext = findViewById(R.id.spatReasonNext);
        choice1.button = findViewById(R.id.spatReasonChoice1);
        choice2.button = findViewById(R.id.spatReasonChoice2);
        choice3.button = findViewById(R.id.spatReasonChoice3);
        choice4.button = findViewById(R.id.spatReasonChoice4);
        choice5.button = findViewById(R.id.spatReasonChoice5);

        // sets the names for MCButtons
        choice1.buttonName = "A";
        choice2.buttonName = "B";
        choice3.buttonName = "C";
        choice4.buttonName = "D";
        choice5.buttonName = "E";

        // importing everything into MultChoice
        multChoice.answer1 = choice1;
        multChoice.answer2 = choice2;
        multChoice.answer3 = choice3;
        multChoice.answer4 = choice4;
        multChoice.answer5 = choice5;
        multChoice.timeStamps = timeStamps;
        multChoice.fiveAnswers = true;
        multChoice.selected = selected;

        // runs selectButton void
        multChoice.selectButton(choice1);
        multChoice.selectButton(choice2);
        multChoice.selectButton(choice3);
        multChoice.selectButton(choice4);
        multChoice.selectButton(choice5);

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.spatReason);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                Toast.makeText(SpatReasonActivity.this, "tap detected", Toast.LENGTH_SHORT).show();
            }
        });

        // "next" button
        spatReasonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, SpatReasonActivity.this, timeStamps, "spatial reason", multChoice.selected, "A");
            }
        });
    }
}
