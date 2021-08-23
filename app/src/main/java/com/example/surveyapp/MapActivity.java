package com.example.surveyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    public static class MCButton{
        Button button;
        String buttonName;
    }

    String outputName;
    Button mapNext;
    MCButton choice1 = new MCButton();
    MCButton choice2 = new MCButton();
    MCButton choice3 = new MCButton();
    MCButton choice4 = new MCButton();
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();

    QuestionBank questionBank;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();
        question.printQuestionAttributes();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // sets up image
        ImageView imageView = (ImageView) findViewById(R.id.mapImage);
        int imageResource = getResources().getIdentifier("@drawable/"+question.getImgPath(), null, this.getPackageName());
        imageView.setImageResource(imageResource);

        // sets up prompt
        TextView prompt = findViewById(R.id.mapPrompt);
        prompt.setText(question.getQuestion());

        // matches buttons with xml id
        mapNext = findViewById(R.id.mapNext);
        choice1.button = findViewById(R.id.mapChoice1);
        choice2.button = findViewById(R.id.mapChoice2);
        choice3.button = findViewById(R.id.mapChoice3);
        choice4.button = findViewById(R.id.mapChoice4);

        // sets the names for MCButtons
        choice1.button.setText(question.getAnswerOptions()[0]);
        choice2.button.setText(question.getAnswerOptions()[1]);
        choice3.button.setText(question.getAnswerOptions()[2]);
        choice4.button.setText(question.getAnswerOptions()[3]);

        // runs selectButton void
        selectButton(choice1);
        selectButton(choice2);
        selectButton(choice3);
        selectButton(choice4);

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.map);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // "next" button
        mapNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                Log.d("MapActivity", "selected: " + selected );
                csvWriter.WriteAnswers(outputName, MapActivity.this, timeStamps, question.getAnswerType(), selected, question.getCorrectAnswer());
                ActivitySwitch();
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
                choice.button.setSelected(true);
                selected = choice.button.getText().toString();
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
