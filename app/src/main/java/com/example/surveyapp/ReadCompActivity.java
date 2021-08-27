package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ReadCompActivity extends AppCompatActivity {

    private static final String TAG = "ReadCompActivity";

    String outputName;
    Button readCompNext;
    CSVWriting csvWriter = new CSVWriting();

    QuestionBank questionBank;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readcomp);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // splitting instruction string
//        prePrompts = question.getInstruction().split("(?m)^\\s*$");

        // matches textviews with id
        TextView prePrompt = findViewById(R.id.readCompPrePrompt);
        TextView excerpt = findViewById(R.id.readCompExcerpt);
        TextView prompt = findViewById(R.id.readCompPrompt);

        // setting text from library
        prePrompt.setText(question.getInstruction());
        excerpt.setText(question.getImgPath());
        prompt.setText(question.getQuestion());

        // matches buttons with xml id
        readCompNext = findViewById(R.id.readCompNext);
        InitChoiceButtons buttons = new InitChoiceButtons(this,"readCompChoice",question.getAnswerOptions());

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.readComp);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons.getTimeStamps().updateTimeStamp();
            }
        });

        // "next" button
        readCompNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.getTimeStamps().updateTimeStamp();
                Log.d("ReadCompActivity", "selected: " + buttons.getSelected() );
                csvWriter.WriteAnswers(outputName, ReadCompActivity.this, buttons.getTimeStamps(), question.getTypeActivity(), buttons.getSelected(), question.getCorrectAnswer());
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
