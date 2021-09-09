package com.example.surveyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class VisSearchActivity extends AppCompatActivity {

    private static final String TAG = "VisSearchActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    String outputName;
    Button visSearchNext;
    CSVWriting csvWriter = new CSVWriting();
    TextView prePrompt;
    TextView prompt;
    TextView passage;
    GetTimeStamp timeStamps = new GetTimeStamp();
    Integer count = 0;

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
        new AlertDialog.Builder(VisSearchActivity.this)
                .setTitle("End Survey")
                .setMessage("Are you sure you want to end this survey? All results will be saved and the app will restart")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        navigateUpTo(new Intent(VisSearchActivity.this, FirstPageActivity.class));
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
        setContentView(R.layout.vissearch);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // textviews match
        prePrompt = findViewById(R.id.visSearchPrePrompt);
        prompt = findViewById(R.id.visSearchPrompt);
        passage = findViewById(R.id.visSearchExcerpt);

        // assigning text based on the library
        prePrompt.setText(question.getInstruction());
        prompt.setVisibility(View.GONE);

        if (question.getQuestion().isEmpty()) {
            String text = question.getImgPath();
            SpannableString ss = new SpannableString(text);
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    count += 1;
                    Toast.makeText(VisSearchActivity.this, "test1", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.ummaize));
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(VisSearchActivity.this, "Two", Toast.LENGTH_SHORT).show();
                }
            };
            ss.setSpan(clickableSpan1, 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan2, 16, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            passage.setText(ss);
            passage.setMovementMethod(LinkMovementMethod.getInstance());

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
                    csvWriter.WriteAnswers(outputName, VisSearchActivity.this, timeStamps, question.getTypeActivity(), "Found: "+Integer.toString(count), question.getCorrectAnswer());
                    ActivitySwitch();
                }
            });
        } else {

            prompt.setText(question.getQuestion());
            prompt.setVisibility(View.VISIBLE);
            passage.setText(question.getImgPath());

            // matches buttons with xml id
            visSearchNext = findViewById(R.id.visSearchNext);
            InitChoiceButtons buttons = new InitChoiceButtons(this, "visSearchChoice", question.getAnswerOptions());

            // detects tap on screen, records timestamp
            ConstraintLayout cLayout = findViewById(R.id.visSearch);
            cLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttons.getTimeStamps().updateTimeStamp();
                }
            });

            // "next" button
            visSearchNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttons.getTimeStamps().updateTimeStamp();
                    csvWriter.WriteAnswers(outputName, VisSearchActivity.this, buttons.getTimeStamps(), question.getTypeActivity(), buttons.getSelected(), question.getCorrectAnswer());
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
