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
    private List<List<Question>> questionSets = new ArrayList<>();
    private int setChoice;

    public void setSetChoice(int setChoice) {
        this.setChoice = setChoice - 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public QuestionBank(InputStream is) {
        this.indexQuestion = 0;
        this.questions = new ArrayList<>();
        readData(is);
    }

    public int getSetChoice() {
        return setChoice;
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
        questions = questionSets.get(setChoice);
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
            int numQuestionSet = 0;
            int indQuestionSet = -1;
            String prevType = "";
            while ((line = reader.readLine()) != null) {
                //split string
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if(tokens.length != 11 || line.length() <= tokens.length - 1){
                    continue;
                }
                    //read data
                int id = Integer.parseInt(tokens[0]);
                String type = tokens[1];
                String classification = tokens[2];
                String answerType = tokens[3];
                String correctAnswer = tokens[4];
                String questionNumber = tokens[5];
                String instruction = tokens[6];
                String imgPath = tokens[7];
                String surveyQuestion = tokens[8];
                String questionCode = tokens[10];

                String[] answerOptions;
                if(tokens[9].length() > 0) {
                    tokens[9] = tokens[9].substring(1, tokens[9].length() - 1);
                    answerOptions = tokens[9].split(",");
                }else{
                    answerOptions = new String[0];
                }


                String typeActivity = null;
                if (type.contains("Map")) {
                    typeActivity = "MapActivity";
                    List<Question> e = new ArrayList<>();
                    questionSets.add(e);
                    numQuestionSet++;
                }else if(type.contains("Short Term Memory")) {
                    typeActivity = "ShortMemActivity";
                }else if(type.contains("Spatial Reasoning")) {
                    typeActivity = "SpatReasonActivity";
                }else if(type.contains("Reading Comprehension")) {
                    typeActivity = "ReadCompActivity";
                }else if(type.contains("Pattern")) {
                    typeActivity = "PatternActivity";
                }else if(type.contains("Visual Search - TEXT")) {
                    typeActivity = "VisSearchActivity";
                }else if(type.contains("Typing")) {
                    typeActivity = "TypingActivity";
                }else if(type.contains("Visual Search - IMAGE")) {
                    typeActivity = "VisSearchImgActivity";
                }else if(type.contains("Word Search")) {
                    typeActivity = "WordSearchActivity";
                }else if(type.contains("Subjective")) {
                    typeActivity = "SubjectiveActivity";
                }else if(type.contains("Surrogate Reference Task")) {
                    typeActivity = "SurRefActivity";
                }else if(type.contains("Spot the Difference")) {
                    typeActivity = "SpotDiffActivity";
                }else if(type.contains("Reaction Time")) {
                    typeActivity = "ReactTimeActivity";
                }


                if(typeActivity != null) {
                    Question question = new Question(id,type,typeActivity,classification,answerType,correctAnswer,questionNumber, instruction,
                            imgPath,surveyQuestion,answerOptions,questionCode);
                    question.printQuestionAttributes();
//                    questions.add(question);

                    if(prevType.equals(typeActivity)){
                        indQuestionSet++;
                    }else{
                        indQuestionSet=0;
                    }
                    questionSets.get(indQuestionSet).add(question);
                    prevType = typeActivity;


                } else{
                    Log.wtf("MyActivity", "No corresponding question type on line: " + line);
                }
            }
            is.close();
        }catch (Exception e){
            Log.wtf("MyActivity", "Error reading data file on line: " + line, e);
            e.printStackTrace();
        }
    }
}
