package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ReadCompActivity extends AppCompatActivity {

    private static final String TAG = "ReadCompActivity";

    String outputName;
    Button readCompNext;
    MultipleChoiceFormat.MCButton choice1 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice2 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice3 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice4 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice5 = new MultipleChoiceFormat.MCButton();
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();
    MultipleChoiceFormat multChoice = new MultipleChoiceFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readcomp);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // matches buttons with xml id
        readCompNext = findViewById(R.id.readCompNext);
        choice1.button = findViewById(R.id.readCompChoice1);
        choice2.button = findViewById(R.id.readCompChoice2);
        choice3.button = findViewById(R.id.readCompChoice3);
        choice4.button = findViewById(R.id.readCompChoice4);
        choice5.button = findViewById(R.id.readCompChoice5);

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
        ConstraintLayout cLayout = findViewById(R.id.readComp);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                Toast.makeText(ReadCompActivity.this, "tap detected", Toast.LENGTH_SHORT).show();
            }
        });

        // "next" button
        readCompNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, ReadCompActivity.this, timeStamps, "spatial reason", multChoice.selected, "A");
            }
        });
    }
}
