package com.example.surveyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class SurRefActivity extends AppCompatActivity {

    Button item;
    GetTimeStamp timeStamps = new GetTimeStamp();
    CSVWriting csvWriter = new CSVWriting();
    String outputName;
    String answer;
    Button next;
    String[] posSize = new String[4];
    TextView prompt;

    QuestionBank questionBank;
    Question question;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surreftask);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // read values for pos and size
//        String[] posSize = question.getQuestionCode().split(",");

        item = findViewById(R.id.surRefCircle);
        next = findViewById(R.id.surRefNext);
        prompt = findViewById(R.id.surRefPrompt);
        prompt.setText(question.getInstruction());
        item.setBackgroundColor(Color.TRANSPARENT);

        ImageView imageView = (ImageView) findViewById(R.id.surRefImg);
        int imageResource = getResources().getIdentifier("@drawable/"+question.getImgPath(), null, this.getPackageName());
        imageView.setImageResource(imageResource);

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.surRefTask);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(constraintLayout);
        constraint.constrainPercentHeight(R.id.surRefCircle,Integer.parseInt(posSize[0]));
        constraint.constrainPercentWidth(R.id.surRefCircle,Integer.parseInt(posSize[1]));
        constraint.setVerticalBias(R.id.surRefCircle,Integer.parseInt(posSize[3]));
        constraint.setHorizontalBias(R.id.surRefCircle,Integer.parseInt(posSize[2]));
        constraint.applyTo(constraintLayout);
        // figure out how to show when selected

        // detects tap on screen, records timestamp
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // testing for item found
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                item.setBackgroundColor(getResources().getColor(R.color.ummaize));
                item.setSelected(true);
            }
        });

        // "next" button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                if(item.isSelected()){
                    answer = "found";
                }
                else{
                    answer = "not found";
                }
                csvWriter.WriteAnswers(outputName, SurRefActivity.this, timeStamps, question.getTypeActivity(), answer, "found");
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
