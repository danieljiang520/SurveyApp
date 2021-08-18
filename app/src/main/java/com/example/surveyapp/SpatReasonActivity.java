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

    public static class MCButton{
        Button button;
        String buttonName;
    }

    String outputName;
    Button spatReasonNext;
    MCButton choice1 = new MCButton();
    MCButton choice2 = new MCButton();
    MCButton choice3 = new MCButton();
    MCButton choice4 = new MCButton();
    MCButton choice5 = new MCButton();
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();

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

        // runs selectButton void
        selectButton(choice1);
        selectButton(choice2);
        selectButton(choice3);
        selectButton(choice4);

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
                csvWriter.WriteAnswers(outputName, SpatReasonActivity.this, timeStamps, "spatial reason", selected, "A");
            }
        });
    }
    // sets the tapped button to "selected"
    public void selectButton(MCButton choice){
        choice.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                choice1.button.setSelected(false);
                choice2.button.setSelected(false);
                choice3.button.setSelected(false);
                choice4.button.setSelected(false);
                choice5.button.setSelected(false);
                choice.button.setSelected(true);
                selected = choice.buttonName;
            }
        });
    }
}
