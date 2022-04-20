package com.example.surveyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

    private static final String TAG = "SurRefActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

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
        new AlertDialog.Builder(SurRefActivity.this)
                .setTitle("End Survey")
                .setMessage("Are you sure you want to end this survey? All results will be saved and the app will restart")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        navigateUpTo(new Intent(SurRefActivity.this, FirstPageActivity.class));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    // THIS IS MENU STUFF

    @Override
    public void onBackPressed() {
        // Disable back button
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surreftask);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getPrevQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        TextView title = findViewById(R.id.textTitleSurreftask);
        title.setText("Set " + String.valueOf(questionBank.getSetChoiceString()+1) + ": "+ question.getType());
        // read values for pos and size
        String[] posSize = question.getQuestionCode().split("-");

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

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float imageHeight = imageView.getHeight();
                float imageWidth = imageView.getWidth();

                constraint.constrainHeight(R.id.surRefCircle, (int) (imageHeight*(Float.parseFloat(posSize[1]))));
                constraint.constrainWidth(R.id.surRefCircle, (int) (imageWidth*(Float.parseFloat(posSize[0]))));
                constraint.setVerticalBias(R.id.surRefCircle,Float.parseFloat(posSize[3]));
                constraint.setHorizontalBias(R.id.surRefCircle,Float.parseFloat(posSize[2]));
                constraint.applyTo(constraintLayout);

            }
        });

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
                item.getBackground().setAlpha(70);
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
