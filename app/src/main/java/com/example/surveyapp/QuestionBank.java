package com.example.surveyapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank implements Serializable {

    private List<Question> questions;
    private int indexQuestion;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public QuestionBank(InputStream is) {
        this.indexQuestion = 0;
        this.questions = new ArrayList<>();
        readData(is);
    }

    public Question getCurrentQuestion() {
        int ind = indexQuestion;
        if (ind > 0){
            ind--;
        }
        questions.get(ind).printQuestionAttributes();
        return questions.get(ind);
    }

    public Question pop(){
        if (indexQuestion < questions.size()){
            Question q = questions.get(indexQuestion);
            indexQuestion++;
            return q;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readData(InputStream is){

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line = "";
        try {
            // step over header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                //split string
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                //read data
                //Question sample = new Question(tokens);
                int id = Integer.parseInt(tokens[0]);
                String type = tokens[1];
                String classification = tokens[2];
                String answerType = tokens[3];
                String correctAnswer = tokens[4];
                String instruction = tokens[5];
                String imgPath = tokens[6];
                String surveyQuestion = tokens[7];
                String questionCode = tokens[9];

                String[] answerOptions;
                if(tokens[8].length() > 0) {
                    tokens[8] = tokens[8].substring(1, tokens[8].length() - 1);
                    answerOptions = tokens[8].split(",");
                }else{
                    answerOptions = new String[0];
                }

                String typeActivity = null;
                switch(type) { // in Java 7
                    case "Map":
                        typeActivity = "MapActivity";
                        break;
                    case "Short Term Memory":
                        typeActivity = "ShortMemActivity";
                        break;
                    case "Spatial Reasoning":
                        typeActivity = "SpatReasonActivity";
                        break;
                    case "Reading Comprehension":
                        typeActivity = "ReadCompActivity";
                        break;
                    case "Pattern":
                        typeActivity = "PatternActivity";
                        break;
                    //case "Visual Search - TEXT":
                        //typeActivity = "VisSearchActivity";
                        //break;
                    case "Typing":
                        typeActivity = "TypingActivity";
                        break;
                    //case "Subjective":
                        //typeActivity = "SubjectiveActivity";
                        //break;
                    case "Spot the Difference":
                        typeActivity = "SpotDiffActivity";
                        break;
                }
                if(typeActivity != null) {
                    Question question = new Question(id,typeActivity,classification,answerType,correctAnswer,instruction,
                            imgPath,surveyQuestion,answerOptions,questionCode);
                    //question.printQuestionAttributes();
                    questions.add(question);
                } else{
                    Log.wtf("MyActivity", "Cannot create class on line: " + line);
                }
            }
            is.close();
        }catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on line: " + line, e);
            e.printStackTrace();
        }
    }
}
