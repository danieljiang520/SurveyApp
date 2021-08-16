package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";

    public class MCButton{
        Button button;
        String buttonName;
    }

    MCButton choice1 = new MCButton();
    MCButton choice2 = new MCButton();
    MCButton choice3 = new MCButton();
    MCButton choice4 = new MCButton();
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        String outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // DEBUG/TEST!!!
        //csvWriter.WriteAnswers(outputName);

        // matches buttons with xml id
        choice1.button = findViewById(R.id.mapChoice1);
        choice2.button = findViewById(R.id.mapChoice2);
        choice3.button = findViewById(R.id.mapChoice3);
        choice4.button = findViewById(R.id.mapChoice4);

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
    }
    // sets the tapped button to "selected"
    public void selectButton(MCButton choice){
        choice.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice1.button.setSelected(false);
                choice2.button.setSelected(false);
                choice3.button.setSelected(false);
                choice4.button.setSelected(false);
                choice.button.setSelected(true);
                selected = choice.buttonName;
            }
        });
    }

}
