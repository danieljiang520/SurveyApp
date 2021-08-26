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
    Button choice1;
    Button choice2;
    Button choice3;
    Button choice4;
    Button choice5;
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();

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
        choice1 = findViewById(R.id.spatReasonChoice1);
        choice2 = findViewById(R.id.spatReasonChoice2);
        choice3 = findViewById(R.id.spatReasonChoice3);
        choice4 = findViewById(R.id.spatReasonChoice4);
        choice5 = findViewById(R.id.spatReasonChoice5);

        // sets the names for MCButtons
        choice1.setText(question.getAnswerOptions()[0]);
        choice2.setText(question.getAnswerOptions()[1]);
        choice3.setText(question.getAnswerOptions()[2]);
        choice4.setText(question.getAnswerOptions()[3]);
        if(question.getAnswerOptions().length==5){
            choice5.setText(question.getAnswerOptions()[4]);
            choice5.setVisibility(View.VISIBLE);
        }else{
            choice5.setVisibility(View.GONE);
        }

        // runs selectButton void
        selectButton(choice1);
        selectButton(choice2);
        selectButton(choice3);
        selectButton(choice4);
        selectButton(choice5);

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.spatReason);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // "next" button
        spatReasonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                Log.d("MapActivity", "selected: " + selected );
                csvWriter.WriteAnswers(outputName, SpatReasonActivity.this, timeStamps, question.getTypeActivity(), selected, question.getCorrectAnswer());
                ActivitySwitch();
            }
        });
    }
    public void selectButton(Button choice){
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                choice1.setSelected(false);
                choice2.setSelected(false);
                choice3.setSelected(false);
                choice4.setSelected(false);
                choice5.setSelected(false);
                choice.setSelected(true);
                selected = choice.getText().toString();
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
