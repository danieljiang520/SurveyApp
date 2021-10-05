package com.example.surveyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class FirstPageActivity extends AppCompatActivity {

    private static final String TAG = "FirstPageActivity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME";

    // initializing variables for saving values/using widgets
    String partNum;
    String startTime;
    Button start;
    EditText partNumEntry;
    EditText startTimeEntry;

    QuestionBank questionBank;

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
        new AlertDialog.Builder(FirstPageActivity.this)
                .setTitle("End Survey")
                .setMessage("Are you sure you want to end this survey? All results will be saved and the app will restart")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        // after on CLick we are using finish to close and then just after that
                        // we are calling startactivity(getIntent()) to open our application
                        finish();
                        startActivity(getIntent());

                        // this basically provides animation
                        overridePendingTransition(0, 0);
                        String time = System.currentTimeMillis() + "";
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    // THIS IS MENU STUFF

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        Log.d(TAG, "onCreate: started");

        InputStream is = getResources().openRawResource(R.raw.questions_bank_full);
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

        Switch switchAutoResume = findViewById(R.id.switchAutoResume);
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // SAMPLE SET CHOICE MECHANISM
                int setChoice = Integer.parseInt(dropdown.getSelectedItem().toString());
                boolean autoResume = switchAutoResume.isChecked();
                questionBank.setSetChoice(setChoice);
                questionBank.setAutoResume(autoResume);

                partNum = partNumEntry.getText().toString();
                startTime = '"'+startTimeEntry.getText().toString()+'"';
                // this creates a new file output stream
                CSVWriting csvWriter = new CSVWriting();
                csvWriter.CreateCSV(partNum,startTime,FirstPageActivity.this);

                // FOR DEBUG
                //Toast.makeText(FirstPageActivity.this, "File Created and Saved", Toast.LENGTH_SHORT).show();
                ActivitySwitch();
            }
        });
    }

    public void ActivitySwitch() {
        try {
            Question nextQuestion = questionBank.pop();
            String nextClassName = "com.example.surveyapp." + nextQuestion.getTypeActivity();
            Intent intent = new Intent(this, Class.forName(nextClassName));
            intent.putExtra(EXTRA_OUTPUT, partNum); // this sends the io name to the next activity
            intent.putExtra("questionBank", questionBank);
            Log.d("FirstPageActivity", "Activity: " + nextQuestion.getTypeActivity() );
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
