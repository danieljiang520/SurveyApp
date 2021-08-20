package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PatternActivity extends AppCompatActivity {

    private static final String TAG = "PatternActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    String outputName;
    Button patternNext;
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
        setContentView(R.layout.pattern);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // matches buttons with xml id
        patternNext = findViewById(R.id.patternNext);
        choice1.button = findViewById(R.id.patternChoice1);
        choice2.button = findViewById(R.id.patternChoice2);
        choice3.button = findViewById(R.id.patternChoice3);
        choice4.button = findViewById(R.id.patternChoice4);
        choice5.button = findViewById(R.id.patternChoice5);

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
        multChoice.fiveAnswers = false;
        multChoice.selected = selected;

        // runs selectButton void
        multChoice.selectButton(choice1);
        multChoice.selectButton(choice2);
        multChoice.selectButton(choice3);
        multChoice.selectButton(choice4);
        multChoice.selectButton(choice5);

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.pattern);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // "next" button
        patternNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, PatternActivity.this, timeStamps, "pattern", multChoice.selected, "A");
                ActivitySwitch();
            }
        });
    }
    public void ActivitySwitch(){
        Intent intent = new Intent(this,PatternActivity.class);
        intent.putExtra(EXTRA_OUTPUT, outputName); // this sends the io name to the next activity
        startActivity(intent);
    }
}