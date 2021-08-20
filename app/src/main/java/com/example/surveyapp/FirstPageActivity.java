package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class FirstPageActivity extends AppCompatActivity {

    private static final String TAG = "FirstPageActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME";

    // initializing variables for saving values/using widgets
    String partNum;
    String startTime;
    String labels;
    Button start;
    EditText partNumEntry;
    EditText startTimeEntry;

    public QuestionBank questionBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        Log.d(TAG, "onCreate: started");

        InputStream is = getResources().openRawResource(R.raw.questions_bank);
        questionBank = new QuestionBank(is);

        // attaches UM logo to imageview background
        ImageView imageView = (ImageView) findViewById(R.id.UMLogo);
        int imageResource = getResources().getIdentifier("@drawable/umlogo", null, this.getPackageName());
        imageView.setImageResource(imageResource);

        setupStarButton();
    }

    private void setupStarButton() {
        // assigns text entry boxes to relevant variables
        partNumEntry = (EditText) findViewById(R.id.partNumEntry);
        startTimeEntry = (EditText) findViewById(R.id.startTimeEntry);
        start = (Button) findViewById(R.id.startButton);
        // actions when button is clicked
        // assigns text responses to variable
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                partNum = partNumEntry.getText().toString();
                startTime = '"'+startTimeEntry.getText().toString()+'"';
                // this creates a new file output stream
                CSVWriting csvWriter = new CSVWriting();
                csvWriter.CreateCSV(partNum,startTime,FirstPageActivity.this);

                // FOR DEBUG
                Toast.makeText(FirstPageActivity.this, "File Created and Saved", Toast.LENGTH_SHORT).show();
                ActivitySwitch();
            }
        });
    }

    }
}
