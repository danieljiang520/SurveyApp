package com.example.surveyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readData();
    }

    private List<QuestionSample> questionSamples= new ArrayList<>();
    private void readData(){
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
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