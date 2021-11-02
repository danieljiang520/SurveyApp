package com.example.surveyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ReadCompActivity extends AppCompatActivity {

    private static final String TAG = "ReadCompActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    String outputName;
    Button readCompNext;
    CSVWriting csvWriter = new CSVWriting();

    QuestionBank questionBank;
    Question question;

    //20,Reading Comprehension,Visual Cognitive,Accuracy/Timing,CA,Q#,PRE,PASS,POST,OPS,

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
        new AlertDialog.Builder(ReadCompActivity.this)
                .setTitle("End Survey")
                .setMessage("Are you sure you want to end this survey? All results will be saved and the app will restart")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        navigateUpTo(new Intent(ReadCompActivity.this, FirstPageActivity.class));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    // THIS IS MENU STUFF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readcomp);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        TextView title = findViewById(R.id.textTitleReadcomp);
        title.setText("Set " + String.valueOf(questionBank.getSetChoice()+1) + ": "+ question.getType());
        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // splitting instruction string
//        prePrompts = question.getInstruction().split("(?m)^\\s*$");
        ImageView imageView = (ImageView) findViewById(R.id.readCompImg);
        if(question.getQuestionCode()!="null") {
            int imageResource = getResources().getIdentifier("@drawable/" + question.getQuestionCode(), null, this.getPackageName());
            imageView.setImageResource(imageResource);
        }
        else{
            imageView.setVisibility(View.GONE);
        }

        // matches textviews with id
        TextView prePrompt = findViewById(R.id.readCompPrePrompt);
        TextView excerpt = findViewById(R.id.readCompExcerpt);
        TextView prompt = findViewById(R.id.readCompPrompt);
        EditText edit = findViewById(R.id.readCompTextEntry);

        // setting text from library
        prePrompt.setText(question.getInstruction());
        excerpt.setText(question.getImgPath());
        prompt.setText(question.getQuestion());

        // setting entry box visibility
        edit.setVisibility(View.GONE);

        // matches buttons with xml id
        readCompNext = findViewById(R.id.readCompNext);

        if(question.getAnswerOptions()==null){
            Button b1 = findViewById(R.id.readCompChoice1);
            Button b2 = findViewById(R.id.readCompChoice2);
            Button b3 = findViewById(R.id.readCompChoice3);
            Button b4 = findViewById(R.id.readCompChoice4);
            Button b5 = findViewById(R.id.readCompChoice5);
            b1.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);
            b3.setVisibility(View.GONE);
            b4.setVisibility(View.GONE);
            b5.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            GetTimeStamp timeStamp = new GetTimeStamp();
            // detects tap on screen, records timestamp
            ConstraintLayout cLayout = findViewById(R.id.readComp);
            cLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeStamp.updateTimeStamp();
                }
            });
            // "next" button
            readCompNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeStamp.updateTimeStamp();
                    String response = edit.getText().toString();
                    csvWriter.WriteAnswers(outputName, ReadCompActivity.this, timeStamp, question.getTypeActivity(), response, question.getCorrectAnswer());
                    ActivitySwitch();
                }
            });
        }
        else{
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
