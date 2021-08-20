package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SpotDiffActivity extends AppCompatActivity {

    private static final String TAG = "VisSearchV1Activity";

    String outputName;
    Button spotDiffNext;
    MultipleChoiceFormat.MCButton choice1 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice2 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice3 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice4 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice5 = new MultipleChoiceFormat.MCButton();
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();
    MultipleChoiceFormat multChoice = new MultipleChoiceFormat();

    QuestionBank questionBank;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotdiff);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // matches buttons with xml id
        spotDiffNext = findViewById(R.id.spotDiffNext);
        choice1.button = findViewById(R.id.spotDiffChoice1);
        choice2.button = findViewById(R.id.spotDiffChoice2);
        choice3.button = findViewById(R.id.spotDiffChoice3);
        choice4.button = findViewById(R.id.spotDiffChoice4);
        choice5.button = findViewById(R.id.spotDiffChoice5);

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
        ConstraintLayout cLayout = findViewById(R.id.spotDiff);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                Toast.makeText(SpotDiffActivity.this, "tap detected", Toast.LENGTH_SHORT).show();
            }
        });

        // "next" button
        spotDiffNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, SpotDiffActivity.this, timeStamps, "reading comprehension", multChoice.selected, "A");
                ActivitySwitch();
            }
        });
    }
    public void ActivitySwitch() {
        Question nextQuestion = questionBank.pop();
        if(nextQuestion==null){
            Intent intent = new Intent(this, FinalPageActivity.class);
            Log.d("Activity", "Activity: FINAL" );
            startActivity(intent);
        }else{
            try {
                String nextClassName = "com.example.surveyapp." + nextQuestion.getTypeActivity();
                Intent intent = new Intent(this, Class.forName(nextClassName));
                intent.putExtra("questionBank", questionBank);
                Log.d("Activity", "Activity: " + nextQuestion.getTypeActivity() );
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
