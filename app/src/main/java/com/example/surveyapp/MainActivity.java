package com.example.surveyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readData();
        Button btnStart = (Button) findViewById(R.id.btnStart);
        TextView txtType = (TextView) findViewById((R.id.txtType));
        TextView txtInstruction = (TextView) findViewById((R.id.txtInstruction));
        TextView txtQuestion = (TextView) findViewById((R.id.txtQuestion));
        TextView txtAnswer1 = (TextView) findViewById((R.id.txtAnswer1));
        TextView txtAnswer2 = (TextView) findViewById((R.id.txtAnswer2));
        TextView txtCorrectAnswer = (TextView) findViewById((R.id.txtCorrectAnswer));
        btnStart.setOnClickListener(view -> {
            QuestionSample sample;
            sample = questionSamples.get(0);
            txtType.setText("Type: " + sample.getType());
            txtInstruction.setText("Instruction: " + sample.getInstruction());
            txtQuestion.setText("Question: " + sample.getQuestion());
            txtAnswer1.setText("Answer1: " + sample.getAnswer1());
            txtAnswer2.setText("Answer2: " + sample.getAnswer2());
            txtCorrectAnswer.setText("Correct Answer: " + sample.getCorrect_answer());
        });


    }

    private final List<QuestionSample> questionSamples;

    {
        questionSamples = new ArrayList<>();
    }

    private void readData(){
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line = "";
        try {
            // step over header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity", "Line: " + line );
                //split string
                String[] tokens = line.split(",");

                //read data
                QuestionSample sample = new QuestionSample(tokens);

                questionSamples.add(sample);

                Log.d("Myactivity", "Just created: "+ sample);

            }
            is.close();
        }catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on  line: " + line, e);
            e.printStackTrace();
        }

    }
}