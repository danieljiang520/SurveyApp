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
    Button choice1;
    Button choice2;
    Button choice3;
    Button choice4;
    Button choice5;
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();
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
        choice1 = findViewById(R.id.spotDiffChoice1);
        choice2 = findViewById(R.id.spotDiffChoice2);
        choice3 = findViewById(R.id.spotDiffChoice3);
        choice4 = findViewById(R.id.spotDiffChoice4);
        choice5 = findViewById(R.id.spotDiffChoice5);

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
        ConstraintLayout cLayout = findViewById(R.id.spotDiff);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // "next" button
        spotDiffNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, SpotDiffActivity.this, timeStamps, question.getTypeActivity(), selected, question.getCorrectAnswer());
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
