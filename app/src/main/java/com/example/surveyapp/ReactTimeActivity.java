package com.example.surveyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ReactTimeActivity extends AppCompatActivity {

    private static final String TAG = "ReactTimeActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    QuestionBank questionBank;
    Question question;
    String[] imgPaths;
    Button next;
    GetTimeStamp timeStamps = new GetTimeStamp();
    TextView prePrompt;
    String outputName;
    CSVWriting csvWriter = new CSVWriting();

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
        new AlertDialog.Builder(ReactTimeActivity.this)
                .setTitle("End Survey")
                .setMessage("Are you sure you want to end this survey? All results will be saved and the app will restart")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        navigateUpTo(new Intent(ReactTimeActivity.this, FirstPageActivity.class));
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
        setContentView(R.layout.reacttime);

        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getPrevQuestion();

        imgPaths = question.getImgPath().split("-");

        TextView title = findViewById(R.id.textTitleReacttime);
        title.setText("Set " + String.valueOf(questionBank.getSetChoiceString()+1) + ": "+ question.getType());

        next = findViewById(R.id.reactTimeNext);
        prePrompt = findViewById(R.id.reactTimePrePrompt);
        prePrompt.setText(question.getInstruction());

        // assigning images their paths
        ImageView image1 = (ImageView) findViewById(R.id.reactTimeImg1);
        int imageResource1 = getResources().getIdentifier("@drawable/"+imgPaths[0], null, this.getPackageName());
        image1.setImageResource(imageResource1);
        ImageView image2 = (ImageView) findViewById(R.id.reactTimeImg2);
        int imageResource2 = getResources().getIdentifier("@drawable/"+imgPaths[1], null, this.getPackageName());
        image2.setImageResource(imageResource2);
        ImageView image3 = (ImageView) findViewById(R.id.reactTimeImg3);
        int imageResource3 = getResources().getIdentifier("@drawable/"+imgPaths[2], null, this.getPackageName());
        image3.setImageResource(imageResource3);

        // setting initial visibility
        next.setVisibility(View.VISIBLE);
        prePrompt.setVisibility(View.VISIBLE);
        image1.setVisibility(View.GONE);
        image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);

        // general clicks
        ConstraintLayout cLayout = findViewById(R.id.reactTime);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // action if images get clicked
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                image1.setColorFilter(getResources().getColor(R.color.ummaize), PorterDuff.Mode.DARKEN);
                image1.setSelected(true);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                image2.setColorFilter(getResources().getColor(R.color.ummaize), PorterDuff.Mode.DARKEN);
                image2.setSelected(true);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                image3.setColorFilter(getResources().getColor(R.color.ummaize), PorterDuff.Mode.DARKEN);
                image3.setSelected(true);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();

                // setting visibility for first img
                next.setVisibility(View.GONE);
                prePrompt.setVisibility(View.GONE);
                image1.setVisibility(View.VISIBLE);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // setting visibility for second img
                        image1.setVisibility(View.GONE);
                        image2.setVisibility(View.VISIBLE); //2

                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // setting visibility for third img
                                image2.setVisibility(View.GONE); //2
                                image3.setVisibility(View.VISIBLE); //3

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // completing the module
                                        String response = image1.isSelected()+" "+image2.isSelected()+" "+image3.isSelected();
                                        csvWriter.WriteAnswers(outputName, ReactTimeActivity.this, timeStamps, question.getTypeActivity(), question.getQuestion(),response, question.getCorrectAnswer());
                                        ActivitySwitch();
                                    }
                                }, 3000);
                            }
                        }, 3000);
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable back button
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
