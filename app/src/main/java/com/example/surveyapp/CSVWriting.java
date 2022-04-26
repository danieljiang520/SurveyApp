package com.example.surveyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSVWriting extends AppCompatActivity {

    // creates csv for writing
    public void CreateCSV(String partNum, String startTime, Context context){

        String labels = ",first click,last click,page submit,click count,type,prompt,answer,correct answer";

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

    // writes out answer format
    public void WriteAnswers(String outputName, Context context, GetTimeStamp timeStamps,
                             String type, String prompt, String answer, String correctAnswer){
        File myExternalFile = new File(context.getExternalFilesDir("CsvFileDir"), outputName + ".csv");
        FileOutputStream fos;
        try{
            fos = new FileOutputStream(myExternalFile, true);
            fos.write("\n".getBytes());
            fos.write(",".getBytes());
            fos.write(timeStamps.firstClick.getBytes());
            fos.write(",".getBytes());
            fos.write(timeStamps.lastClick.getBytes());
            fos.write(",".getBytes());
            fos.write(timeStamps.pageSubmit.getBytes());
            fos.write(",".getBytes());
            fos.write(String.valueOf(timeStamps.clickCount).getBytes());
            fos.write(",".getBytes());
            fos.write(type.getBytes());
            fos.write(",".getBytes());
            fos.write(prompt.getBytes());
            fos.write(",".getBytes());
            fos.write(answer.getBytes());
            fos.write(",".getBytes());
            fos.write(correctAnswer.getBytes());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
