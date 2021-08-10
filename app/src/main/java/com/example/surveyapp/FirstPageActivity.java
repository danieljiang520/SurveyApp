package com.example.surveyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {

    private static final String TAG = "FirstPageActivity";

    // initializing variables for saving values/using widgets
    String partNum;
    String startTime;
    Button start;
    EditText partNumEntry;
    EditText startTimeEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        Log.d(TAG, "onCreate: started");

        // attaches UM logo to imageview background
        ImageView imageView = (ImageView) findViewById(R.id.UMLogo);
        int imageResource = getResources().getIdentifier("@drawable/umlogo", null, this.getPackageName());
        imageView.setImageResource(imageResource);

        // assigns text entry boxes to relevant variables
        partNumEntry = (EditText) findViewById(R.id.partNumEntry);
        startTimeEntry = (EditText) findViewById(R.id.startTimeEntry);
        start = (Button) findViewById(R.id.startButton);

        // actions when button is clicked
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            // assigns text responses to variable
            public void onClick(View view) {
                partNum = partNumEntry.getText().toString();
                startTime = startTimeEntry.getText().toString();

                // FOR DEBUG
                //showButtonWork(partNum);
                //showButtonWork(startTime);
                // FOR DEBUG
            }
        });
    }

    // FOR DEBUG
    //private void showButtonWork (String entry){
        //Toast.makeText(FirstPageActivity.this, entry, Toast.LENGTH_SHORT).show();
        // FOR DEBUG
    //}
}
