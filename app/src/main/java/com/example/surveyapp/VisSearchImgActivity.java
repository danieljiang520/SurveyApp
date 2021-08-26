package com.example.surveyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class VisSearchImgActivity extends AppCompatActivity {

    Button word1;
    Button word2;
    Button word3;
    GetTimeStamp timeStamps = new GetTimeStamp();
    CSVWriting csvWriter = new CSVWriting();
    String outputName;
    String answer;
    Button next;

    QuestionBank questionBank;
    Question question;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vissearchimg);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        word1 = findViewById(R.id.visSearchImgWord1);
        word2 = findViewById(R.id.visSearchImgWord2);
        word3 = findViewById(R.id.visSearchImgWord3);
        next = findViewById(R.id.surRefNext);
        word1.setBackgroundColor(Color.TRANSPARENT);
        word2.setBackgroundColor(Color.TRANSPARENT);
        word3.setBackgroundColor(Color.TRANSPARENT);

        ImageView imageView = (ImageView) findViewById(R.id.visSearchImgImg);
        int imageResource = getResources().getIdentifier("@drawable/surrtest", null, this.getPackageName());
        imageView.setImageResource(imageResource);

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.visSearchImg);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(constraintLayout);

        //button1
        constraint.constrainPercentHeight(R.id.visSearchImgWord1,10);
        constraint.constrainPercentWidth(R.id.visSearchImgWord1,10);
        constraint.setVerticalBias(R.id.visSearchImgWord1,0);
        constraint.setHorizontalBias(R.id.visSearchImgWord1,0);

        //button 2
        constraint.constrainPercentHeight(R.id.visSearchImgWord2,10);
        constraint.constrainPercentWidth(R.id.visSearchImgWord2,10);
        constraint.setVerticalBias(R.id.visSearchImgWord2,0);
        constraint.setHorizontalBias(R.id.visSearchImgWord2,0);

        //button 3
        constraint.constrainPercentHeight(R.id.visSearchImgWord3,10);
        constraint.constrainPercentWidth(R.id.visSearchImgWord3,10);
        constraint.setVerticalBias(R.id.visSearchImgWord3,0);
        constraint.setHorizontalBias(R.id.visSearchImgWord3,0);

        constraint.applyTo(constraintLayout);
        // figure out how to show when selected

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.visSearchImg);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // testing for item found
        word1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                word1.setBackgroundColor(getResources().getColor(R.color.ummaize));
                word1.setSelected(true);
            }
        });
        word2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                word2.setBackgroundColor(getResources().getColor(R.color.ummaize));
                word2.setSelected(true);
            }
        });
        word3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                word3.setBackgroundColor(getResources().getColor(R.color.ummaize));
                word3.setSelected(true);
            }
        });

        // "next" button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                if(word1.isSelected()){
                    answer += "word1name";
                }
                if(word2.isSelected()){
                    answer += "word2name";
                }
                if(word3.isSelected()){
                    answer += "word3name";
                }
                csvWriter.WriteAnswers(outputName, VisSearchImgActivity.this, timeStamps, "NA"/*question.getTypeActivity()*/, answer, "found");
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