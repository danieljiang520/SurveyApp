package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SpatReasonActivity extends AppCompatActivity {

    private static final String TAG = "SpatReasonActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    String outputName;
    Button spatReasonNext;
    CSVWriting csvWriter = new CSVWriting();

    QuestionBank questionBank;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spatreason);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // sets up image
        ImageView imageView = (ImageView) findViewById(R.id.spatReasonImage);
        int imageResource = getResources().getIdentifier("@drawable/"+question.getImgPath(), null, this.getPackageName());
        imageView.setImageResource(imageResource);

        // sets up prompt
        TextView prompt = findViewById(R.id.spatReasonPrePrompt);
        prompt.setText(question.getInstruction());

        // matches buttons with xml id
        spatReasonNext = findViewById(R.id.spatReasonNext);
        InitChoiceButtons buttons = new InitChoiceButtons(this,"spatReasonChoice",question.getAnswerOptions());
        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.spatReason);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons.getTimeStamps().updateTimeStamp();
            }
        });

        // "next" button
        spatReasonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.getTimeStamps().updateTimeStamp();
                Log.d("MapActivity", "selected: " + buttons.getSelected() );
                csvWriter.WriteAnswers(outputName, SpatReasonActivity.this, buttons.getTimeStamps(), question.getTypeActivity(), buttons.getSelected(), question.getCorrectAnswer());
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
