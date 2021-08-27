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

    String outputName;
    Button mapNext;
    CSVWriting csvWriter = new CSVWriting();

    QuestionBank questionBank;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

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

        InitChoiceButtons buttons = new InitChoiceButtons(this,"mapChoice",question.getAnswerOptions());

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.map);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons.getTimeStamps().updateTimeStamp();
            }
        });

        // "next" button
        mapNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.getTimeStamps().updateTimeStamp();
                Log.d("MapActivity", "selected: " + buttons.getSelected() );
                csvWriter.WriteAnswers(outputName, MapActivity.this, buttons.getTimeStamps(), question.getAnswerType(), buttons.getSelected(), question.getCorrectAnswer());
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
