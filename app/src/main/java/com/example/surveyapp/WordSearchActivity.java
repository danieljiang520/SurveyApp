package com.example.surveyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class WordSearchActivity extends AppCompatActivity {

    private static final String TAG = "WordSearchActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    Button word1;
    Button word2;
    Button word3;
    GetTimeStamp timeStamps = new GetTimeStamp();
    CSVWriting csvWriter = new CSVWriting();
    String outputName;
    String answer;
    Button next;
    String[] posSize = new String[12];
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
        new AlertDialog.Builder(WordSearchActivity.this)
                .setTitle("End Survey")
                .setMessage("Are you sure you want to end this survey? All results will be saved and the app will restart")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        navigateUpTo(new Intent(WordSearchActivity.this, FirstPageActivity.class));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    // THIS IS MENU STUFF

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordsearch);

        questionBank = (QuestionBank) getIntent().getSerializableExtra("questionBank");
        question = questionBank.getCurrentQuestion();

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        TextView title = findViewById(R.id.textTitleWordsearch);
        title.setText("Set " + String.valueOf(questionBank.getSetChoice()+1) + ": "+ question.getType());
        // read values for pos and size
        String[] posSize = question.getQuestionCode().split("-");
        //.167-.833-.677-.167-.167-.833-.833-.167-.167-.5-.333-.167

        word1 = findViewById(R.id.wordSearchWord1);
        word2 = findViewById(R.id.wordSearchWord2);
        word3 = findViewById(R.id.wordSearchWord3);
        prompt = findViewById(R.id.wordSearchPrompt);
        next = findViewById(R.id.wordSearchNext);
        //word1.setBackgroundColor(Color.TRANSPARENT);
        //word2.setBackgroundColor(Color.TRANSPARENT);
        //word3.setBackgroundColor(Color.TRANSPARENT);
        prompt.setText(question.getInstruction());

        ImageView imageView = (ImageView) findViewById(R.id.wordSearchImg);
        int imageResource = getResources().getIdentifier("@drawable/" + question.getImgPath(), null, this.getPackageName());
        imageView.setImageResource(imageResource);

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.wordSearch);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(constraintLayout);

         imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float imageHeight = imageView.getHeight();
                float imageWidth = imageView.getWidth();
                //float test = imageView.get
                //float ah = constraint.set
                //button1
                constraint.constrainHeight(R.id.wordSearchWord1,(int) (imageHeight*(Float.parseFloat(posSize[1]))));
                constraint.constrainWidth(R.id.wordSearchWord1,(int) (imageWidth*(Float.parseFloat(posSize[0]))));
                constraint.setVerticalBias(R.id.wordSearchWord1,Float.parseFloat(posSize[3]));
                constraint.setHorizontalBias(R.id.wordSearchWord1,Float.parseFloat(posSize[2]));

                //button 2
                constraint.constrainHeight(R.id.wordSearchWord2,(int) (imageHeight*(Float.parseFloat(posSize[5]))));
                constraint.constrainWidth(R.id.wordSearchWord2,(int) (imageWidth*(Float.parseFloat(posSize[4]))));
                constraint.setVerticalBias(R.id.wordSearchWord2,Float.parseFloat(posSize[7]));
                constraint.setHorizontalBias(R.id.wordSearchWord2,Float.parseFloat(posSize[6]));

                //button 3
                constraint.constrainHeight(R.id.wordSearchWord3,(int) (imageHeight*(Float.parseFloat(posSize[9]))));
                constraint.constrainWidth(R.id.wordSearchWord3,(int) (imageWidth*(Float.parseFloat(posSize[8]))));
                constraint.setVerticalBias(R.id.wordSearchWord3,Float.parseFloat(posSize[11]));
                constraint.setHorizontalBias(R.id.wordSearchWord3,Float.parseFloat(posSize[10]));

                constraint.applyTo(constraintLayout);

            }
        });

        // figure out how to show when selected
        /*ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) word1.getLayoutParams();
        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) word2.getLayoutParams();
        ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams) word3.getLayoutParams();

        params1.matchConstraintPercentHeight = Float.parseFloat(posSize[1]);
        params1.matchConstraintPercentWidth = Float.parseFloat(posSize[0]);
        params1.verticalBias = Float.parseFloat(posSize[3]);
        params1.horizontalBias = Float.parseFloat(posSize[2]);

        params2.matchConstraintPercentHeight = Float.parseFloat(posSize[5]);
        params2.matchConstraintPercentWidth = Float.parseFloat(posSize[4]);
        params2.verticalBias = Float.parseFloat(posSize[7]);
        params2.horizontalBias = Float.parseFloat(posSize[6]);

        params3.matchConstraintPercentHeight = Float.parseFloat(posSize[9]);
        params3.matchConstraintPercentWidth = Float.parseFloat(posSize[8]);
        params3.verticalBias = Float.parseFloat(posSize[11]);
        params3.horizontalBias = Float.parseFloat(posSize[10]);

        word1.setLayoutParams(params1);
        word2.setLayoutParams(params2);
        word3.setLayoutParams(params3);*/

        /*imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewGroup.LayoutParams layoutParams1 = word1.getLayoutParams();
                ViewGroup.LayoutParams layoutParams2 = word2.getLayoutParams();
                ViewGroup.LayoutParams layoutParams3 = word3.getLayoutParams();

                layoutParams1.width = (int) (imageView.getWidth()*Float.parseFloat(posSize[0]));
                layoutParams1.height = (int) (imageView.getWidth()*Float.parseFloat(posSize[1]));

                layoutParams2.width = (int) (imageView.getWidth()*Float.parseFloat(posSize[4]));
                layoutParams2.height = (int) (imageView.getWidth()*Float.parseFloat(posSize[5]));

                layoutParams3.width = (int) (imageView.getWidth()*Float.parseFloat(posSize[8]));
                layoutParams3.height = (int) (imageView.getWidth()*Float.parseFloat(posSize[9]));

                word1.setLayoutParams(layoutParams1);
                word2.setLayoutParams(layoutParams2);
                word3.setLayoutParams(layoutParams3);

                Toast.makeText(WordSearchActivity.this, String.valueOf(imageView.getHeight()), Toast.LENGTH_SHORT).show();

            }
        });*/

        // detects tap on screen, records timestamp
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
            }
        });

        // testing for item found
        word1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                word1.setBackgroundColor(getResources().getColor(R.color.ummaize));
                word1.getBackground().setAlpha(70);
                word1.setSelected(true);
            }
        });
        word2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                word2.setBackgroundColor(getResources().getColor(R.color.ummaize));
                word2.getBackground().setAlpha(70);
                word2.setSelected(true);
            }
        });
        word3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                word3.setBackgroundColor(getResources().getColor(R.color.ummaize));
                word3.getBackground().setAlpha(70);
                word3.setSelected(true);
            }
        });

        // "next" button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
//                if(word1.isSelected()){
//                    answer += question.getAnswerOptions()[0];
//                }
//                if(word2.isSelected()){
//                    answer += question.getAnswerOptions()[1];
//                }
//                if(word3.isSelected()){
//                    answer += question.getAnswerOptions()[2];
//                }
                csvWriter.WriteAnswers(outputName, WordSearchActivity.this, timeStamps, question.getTypeActivity(), "wordSearch answer", "wordSearch correct answer");
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
