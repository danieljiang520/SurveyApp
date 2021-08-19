package com.example.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class ShortMemV2Activity extends AppCompatActivity {

    private static final String TAG = "ShortMemV2Activity";
    public static final String EXTRA_OUTPUT = "OUTPUT_NAME"; //reading into next activity

    Button next1;
    Button next2;
    TextView prePrompt;
    ImageView task;
    TextView prompt;
    EditText responseEntry;
    String response;
    String outputName;
    GetTimeStamp timeStamps = new GetTimeStamp();
    CSVWriting csvWriter = new CSVWriting();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortmemv2);

        // Grabs output name from FirstPageActivity for CSVWriting
        Intent intent = getIntent();
        outputName = intent.getStringExtra(FirstPageActivity.EXTRA_OUTPUT);

        // inits
        next1 = findViewById(R.id.shortMemV2Next1);
        next2 = findViewById(R.id.shortMemV2Next2);
        prePrompt = findViewById(R.id.shortMemV2PrePrompt);
        task = findViewById(R.id.shortMemV2img);
        prompt = findViewById(R.id.shortMemV2Prompt);
        responseEntry = findViewById(R.id.shortMemV2Response);

        // setting visibility for the first page
        next1.setVisibility(View.VISIBLE);
        prePrompt.setVisibility(View.VISIBLE);
        next2.setVisibility(View.GONE);
        task.setVisibility(View.GONE);
        prompt.setVisibility(View.GONE);
        responseEntry.setVisibility(View.GONE);

        // registering non button clicks
        ConstraintLayout cLayout = findViewById(R.id.shortMemV2);
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamps.updateTimeStamp();
                Toast.makeText(ShortMemV2Activity.this, "tap detected", Toast.LENGTH_SHORT).show();
            }
        });

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();

                // setting visibility for second page
                next1.setVisibility(View.GONE);
                prePrompt.setVisibility(View.GONE);
                task.setVisibility(View.VISIBLE);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // setting visibility for third page after 3000 ms
                        timerVis();
                    }
                }, 20000);
            }
        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                response = responseEntry.getText().toString();
                Toast.makeText(ShortMemV2Activity.this, response, Toast.LENGTH_SHORT).show();
                csvWriter.WriteAnswers(outputName, ShortMemV2Activity.this, timeStamps, "Short Term Memory", response, "string");
            }
        });
    }

    public void timerVis(){
        task.setVisibility(View.GONE);
        next2.setVisibility(View.VISIBLE);
        prompt.setVisibility(View.VISIBLE);
        responseEntry.setVisibility(View.VISIBLE);
    }
}
