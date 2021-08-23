package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class VisSearchActivity extends AppCompatActivity {

    private static final String TAG = "VisSearchActivity";

    String outputName;
    Button visSearchNext;
    MultipleChoiceFormat.MCButton choice1 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice2 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice3 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice4 = new MultipleChoiceFormat.MCButton();
    MultipleChoiceFormat.MCButton choice5 = new MultipleChoiceFormat.MCButton();
    String selected = "N/A";
    CSVWriting csvWriter = new CSVWriting();
    GetTimeStamp timeStamps = new GetTimeStamp();
    MultipleChoiceFormat multChoice = new MultipleChoiceFormat();
    String[] prePrompts;
    TextView prePrompt;
    TextView prompt;
    TextView passage;

    QuestionBank questionBank;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vissearchv1);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // splitting instruction string
        prePrompts = question.getInstruction().split("\\r?\\n");

        // matches buttons with xml id
        visSearchNext = findViewById(R.id.visSearchNext);
        choice1.button = findViewById(R.id.visSearchChoice1);
        choice2.button = findViewById(R.id.visSearchChoice2);
        choice3.button = findViewById(R.id.visSearchChoice3);
        choice4.button = findViewById(R.id.visSearchChoice4);
        choice5.button = findViewById(R.id.visSearchChoice5);

        // textviews match
        prePrompt = findViewById(R.id.visSearchPrePrompt);
        prompt = findViewById(R.id.visSearchPrompt);
        passage = findViewById(R.id.visSearchExcerpt);

        // assigning text based on the library
        prePrompt.setText(prePrompts[0]);
        passage.setText(prePrompts[1]);
        prompt.setText(question.getQuestion());

        // sets the names for MCButtons
        choice1.buttonName = question.getAnswerOptions()[0];
        choice2.buttonName = question.getAnswerOptions()[1];
        choice3.buttonName = question.getAnswerOptions()[2];
        choice4.buttonName = question.getAnswerOptions()[3];
        choice5.buttonName = question.getAnswerOptions()[4];

        // importing everything into MultChoice
        multChoice.answer1 = choice1;
        multChoice.answer2 = choice2;
        multChoice.answer3 = choice3;
        multChoice.answer4 = choice4;
        multChoice.answer5 = choice5;
        multChoice.timeStamps = timeStamps;
        multChoice.fiveAnswers = true;
        multChoice.selected = selected;

        // runs selectButton void
        multChoice.selectButton(choice1);
        multChoice.selectButton(choice2);
        multChoice.selectButton(choice3);
        multChoice.selectButton(choice4);
        multChoice.selectButton(choice5);

        // detects tap on screen, records timestamp
        ConstraintLayout cLayout = findViewById(R.id.visSearch);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // "next" button
        visSearchNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, VisSearchActivity.this, timeStamps, question.getTypeActivity(), multChoice.selected, question.getCorrectAnswer());
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
