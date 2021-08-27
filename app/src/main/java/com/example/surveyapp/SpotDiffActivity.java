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

public class SpotDiffActivity extends AppCompatActivity {

    private static final String TAG = "VisSearchV1Activity";

    String outputName;
    Button spotDiffNext;
    CSVWriting csvWriter = new CSVWriting();
    TextView prePrompt;
    TextView prompt;

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

        // matches buttons with xml id and prompts
        spotDiffNext = findViewById(R.id.spotDiffNext);
        prePrompt = findViewById(R.id.spotDiffPrePrompt);
        prompt = findViewById(R.id.spotDiffPrompt);

        // assign textviews based on library
        prePrompt.setText(question.getInstruction());
        prompt.setText(question.getQuestion());

        // sets up image
        ImageView imageView = (ImageView) findViewById(R.id.spotDiffImg);
        int imageResource = getResources().getIdentifier("@drawable/"+question.getImgPath(), null, this.getPackageName());
        imageView.setImageResource(imageResource);

        // matches buttons with xml id
        InitChoiceButtons buttons = new InitChoiceButtons(this,"spotDiffChoice",question.getAnswerOptions());

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.spotDiff);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons.getTimeStamps().updateTimeStamp();
            }
        });

        // "next" button
        spotDiffNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.getTimeStamps().updateTimeStamp();
                Log.d("SpotDiffActivity", "selected: " + buttons.getSelected() );
                csvWriter.WriteAnswers(outputName, SpotDiffActivity.this, buttons.getTimeStamps(), question.getTypeActivity(), buttons.getSelected(), question.getCorrectAnswer());
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
