package com.example.surveyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        question = questionBank.getPrevQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        TextView title = findViewById(R.id.textTitleVissearch);
        title.setText("Set " + String.valueOf(questionBank.getSetChoice()+1) + ": "+ question.getType());
        String[] stringLoc = question.getQuestionCode().split("-");

        // textviews match
        prePrompt = findViewById(R.id.visSearchPrePrompt);
        prompt = findViewById(R.id.visSearchPrompt);
        passage = findViewById(R.id.visSearchExcerpt);
        visSearchNext = findViewById(R.id.visSearchNext);
        Button choice1 = findViewById(R.id.visSearchChoice1);
        Button choice2 = findViewById(R.id.visSearchChoice2);
        Button choice3 = findViewById(R.id.visSearchChoice3);
        Button choice4 = findViewById(R.id.visSearchChoice4);
        Button choice5 = findViewById(R.id.visSearchChoice5);

        // assigning text based on the library
        prePrompt.setText(question.getInstruction());
        prompt.setVisibility(View.GONE);

        if (question.getQuestion().isEmpty()) {

            choice1.setVisibility(View.GONE);
            choice2.setVisibility(View.GONE);
            choice3.setVisibility(View.GONE);
            choice4.setVisibility(View.GONE);
            choice5.setVisibility(View.GONE);

            String text = question.getImgPath();
            SpannableString ss = new SpannableString(text);

            ForegroundColorSpan maize1 = new ForegroundColorSpan(getResources().getColor(R.color.ummaize));
            ForegroundColorSpan maize2 = new ForegroundColorSpan(getResources().getColor(R.color.ummaize));
            ForegroundColorSpan maize3 = new ForegroundColorSpan(getResources().getColor(R.color.ummaize));
            ForegroundColorSpan maize4 = new ForegroundColorSpan(getResources().getColor(R.color.ummaize));
            ForegroundColorSpan maize5 = new ForegroundColorSpan(getResources().getColor(R.color.ummaize));
            ForegroundColorSpan maize6 = new ForegroundColorSpan(getResources().getColor(R.color.ummaize));
            ForegroundColorSpan maize7 = new ForegroundColorSpan(getResources().getColor(R.color.ummaize));

            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //count += 1;
                    //Toast.makeText(VisSearchActivity.this, "test1", Toast.LENGTH_SHORT).show();
                    ss.setSpan(maize1, Integer.parseInt(stringLoc[0]), Integer.parseInt(stringLoc[1]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    passage.setText(ss);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //Toast.makeText(VisSearchActivity.this, "Two", Toast.LENGTH_SHORT).show();
                    ss.setSpan(maize2, Integer.parseInt(stringLoc[2]), Integer.parseInt(stringLoc[3]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    passage.setText(ss);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan3 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //Toast.makeText(VisSearchActivity.this, "Two", Toast.LENGTH_SHORT).show();
                    ss.setSpan(maize3, Integer.parseInt(stringLoc[4]), Integer.parseInt(stringLoc[5]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    passage.setText(ss);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan4 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //Toast.makeText(VisSearchActivity.this, "Two", Toast.LENGTH_SHORT).show();
                    ss.setSpan(maize4, Integer.parseInt(stringLoc[6]), Integer.parseInt(stringLoc[7]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    passage.setText(ss);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan5 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //Toast.makeText(VisSearchActivity.this, "Two", Toast.LENGTH_SHORT).show();
                    ss.setSpan(maize5, Integer.parseInt(stringLoc[8]), Integer.parseInt(stringLoc[9]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    passage.setText(ss);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan6 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //Toast.makeText(VisSearchActivity.this, "Two", Toast.LENGTH_SHORT).show();
                    ss.setSpan(maize6, Integer.parseInt(stringLoc[10]), Integer.parseInt(stringLoc[11]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    passage.setText(ss);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan7 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //Toast.makeText(VisSearchActivity.this, "Two", Toast.LENGTH_SHORT).show();
                    ss.setSpan(maize7, Integer.parseInt(stringLoc[12]), Integer.parseInt(stringLoc[13]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    passage.setText(ss);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan1, Integer.parseInt(stringLoc[0]), Integer.parseInt(stringLoc[1]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan2, Integer.parseInt(stringLoc[2]), Integer.parseInt(stringLoc[3]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan3, Integer.parseInt(stringLoc[4]), Integer.parseInt(stringLoc[5]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan4, Integer.parseInt(stringLoc[6]), Integer.parseInt(stringLoc[7]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan5, Integer.parseInt(stringLoc[8]), Integer.parseInt(stringLoc[9]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan6, Integer.parseInt(stringLoc[10]), Integer.parseInt(stringLoc[11]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan7, Integer.parseInt(stringLoc[12]), Integer.parseInt(stringLoc[13]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            passage.setText(ss);
            passage.setMovementMethod(LinkMovementMethod.getInstance());
            //passage.setHighlightColor(Color.TRANSPARENT);

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

            choice1.setVisibility(View.VISIBLE);
            choice2.setVisibility(View.VISIBLE);
            choice3.setVisibility(View.VISIBLE);
            choice4.setVisibility(View.VISIBLE);
            choice5.setVisibility(View.VISIBLE);

            prompt.setText(question.getQuestion());
            prompt.setVisibility(View.VISIBLE);
            passage.setText(question.getImgPath());

            // matches buttons with xml id
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
