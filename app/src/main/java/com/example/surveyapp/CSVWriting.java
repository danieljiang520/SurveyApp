package com.example.surveyapp;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CSVWriting extends AppCompatActivity {

    // creates csv for writing
    public void CreateCSV(String partNum, String startTime, Context context){

        String labels = ",first click,last click,page submit,click count,type,answer,correct answer,correctness";

        // this creates a new file and corresponding output stream
        File myExternalFile = new File(context.getExternalFilesDir("CsvFileDir"), partNum + ".csv");
        FileOutputStream fos;
        try{
            // this attaches the file to the file output stream
            fos = new FileOutputStream(myExternalFile);
            fos.write(partNum.getBytes());
            // this writes the time, date, and initial labels
            for(int i = 0; i < 8; ++i) {
                fos.write(",".getBytes());
            }
            fos.write("\n".getBytes());
            fos.write(startTime.getBytes());
            fos.write("\n".getBytes());
            fos.write(labels.getBytes());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
