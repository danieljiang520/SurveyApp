package com.example.surveyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
import java.util.Vector;

public class SubjectiveActivity extends AppCompatActivity{

    public static class selectionButton{
        Button button;
        String buttonName = "N/A";
        public selectionButton(Button insertButton, String insertName) {
            this.button = insertButton;
            this.buttonName = insertName;
        }
    }

    private static final String TAG = "SubjectiveActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    Vector<selectionButton> choices = new Vector<selectionButton>();
    GetTimeStamp timeStamps = new GetTimeStamp();
    Button next;
    CSVWriting csvWriter = new CSVWriting();
    String outputName;
    TextView prompt;

    QuestionBank questionBank;
    Question question;

    // THIS IS MENU STUFF
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.end_survey:
                endSurvey();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void endSurvey(){
        new AlertDialog.Builder(SubjectiveActivity.this)
                .setTitle("End Survey")
                .setMessage("Are you sure you want to end this survey? All results will be saved and the app will restart")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        navigateUpTo(new Intent(SubjectiveActivity.this, FirstPageActivity.class));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    // THIS IS MENU STUFF

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjective);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        int choiceAmt = Integer.parseInt(question.getQuestionCode());

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // filling vector with buttons
        selectionButton choice1 = new selectionButton(findViewById(R.id.subChoice1),"1");
        selectionButton choice2 = new selectionButton(findViewById(R.id.subChoice2),"2");
        selectionButton choice3 = new selectionButton(findViewById(R.id.subChoice3),"3");
        selectionButton choice4 = new selectionButton(findViewById(R.id.subChoice4),"4");
        selectionButton choice5 = new selectionButton(findViewById(R.id.subChoice5),"5");
        selectionButton choice6 = new selectionButton(findViewById(R.id.subChoice6),"6");
        selectionButton choice7 = new selectionButton(findViewById(R.id.subChoice7),"7");
        selectionButton choice8 = new selectionButton(findViewById(R.id.subChoice8),"8");
        selectionButton choice9 = new selectionButton(findViewById(R.id.subChoice9),"9");
        selectionButton choice10 = new selectionButton(findViewById(R.id.subChoice10),"10");
        selectionButton choice11 = new selectionButton(findViewById(R.id.subChoice11),"11");
        selectionButton choice12 = new selectionButton(findViewById(R.id.subChoice12),"12");
        choices.add(choice1); choices.add(choice2); choices.add(choice3);
        choices.add(choice4); choices.add(choice5); choices.add(choice6);
        choices.add(choice7); choices.add(choice8); choices.add(choice9);
        choices.add(choice10); choices.add(choice11); choices.add(choice12);

        // other views that need to be assigned
        next = findViewById(R.id.subNext);
        prompt = findViewById(R.id.subjectivePrompt);

        // assigning prompt according to the library
        prompt.setText(question.getInstruction());

        // setting the correct number of buttons to be visible + setting their names
        for(int i = 0; i<12; ++i){
            if(i<choiceAmt){
                choices.get(i).button.setVisibility(View.VISIBLE);
                choices.get(i).buttonName = question.getAnswerOptions()[i];
                choices.get(i).button.setText(question.getAnswerOptions()[i]);
            }
            else{
                choices.get(i).button.setVisibility(View.GONE);
            }
        }

        // registering non button clicks
        ConstraintLayout cLayout = findViewById(R.id.shortMem);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // detecting if button is selected or not
        for(int i = 0; i < 12; ++i){
            buttonSelect(choices.get(i).button);
        }

        // registering use of next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                csvWriter.WriteAnswers(outputName, SubjectiveActivity.this, timeStamps, question.getTypeActivity(), selectedAnswers(choices), question.getCorrectAnswer());
                ActivitySwitch();
            }
        });

    }

    // helper functions

    public void buttonSelect(Button choice){
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                choice.setSelected(!choice.isSelected());
            }
        });
    }

    public String selectedAnswers(Vector<selectionButton> answers){
        String answerReturn = "";
        for(int i = 0; i < 12; ++i){
            if(answers.get(i).button.isSelected()){
                answerReturn += answers.get(i).buttonName;
                answerReturn += " ";
            }
        }
        return answerReturn;
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
                intent.putExtra(EXTRA_OUTPUT, outputName); // this sends the io name to the next activity
                intent.putExtra("questionBank", questionBank);
                Log.d("Activity", "Activity: " + nextQuestion.getTypeActivity() );
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
