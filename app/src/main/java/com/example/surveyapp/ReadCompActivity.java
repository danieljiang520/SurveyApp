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
    String prePrompts[];
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
        choice1 = findViewById(R.id.readCompChoice1);
        choice2 = findViewById(R.id.readCompChoice2);
        choice3 = findViewById(R.id.readCompChoice3);
        choice4 = findViewById(R.id.readCompChoice4);
        choice5 = findViewById(R.id.readCompChoice5);

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
        ConstraintLayout cLayout = findViewById(R.id.readComp);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // "next" button
        readCompNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, ReadCompActivity.this, timeStamps, question.getTypeActivity(), selected, question.getCorrectAnswer());
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
