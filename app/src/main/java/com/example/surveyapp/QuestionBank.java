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
import java.util.Collections;
import java.util.List;

public class QuestionBank implements Serializable {

    private List<Question> questions;
    private int indexQuestion;
    private int setChoice; // 0 = baseline; 1 = rest of questions
    private String setChoiceString;
//    private List<List<Question>> questionSets;
    private int numAvailableQuestions;
    private int numSetQuestions;
    private int startingIndex;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public QuestionBank(InputStream is, int setChoice, String setChoiceString) {
        this.indexQuestion = (setChoice==0) ? 0 : 6;
        this.questions = new ArrayList<>();
//        this.questionSets = new ArrayList<>();
        this.setChoice = setChoice;
        this.setChoiceString = setChoiceString;
        this.numAvailableQuestions = readData(is);
        this.numSetQuestions = (setChoice==0) ? 6 : numAvailableQuestions - 6;
        this.startingIndex = indexQuestion;
    }

    public String getSetChoiceString() {
        return setChoiceString;
    }

    public Question getPrevQuestion() {
        int ind = indexQuestion;
        if (ind > 0){
            ind--;
        }
        Question q = questions.get(ind);
        q.printQuestionAttributes();
        return q;
    }

    public Question pop(){
        if (indexQuestion < startingIndex + numSetQuestions){
            Question q = questions.get(indexQuestion);
            indexQuestion++;
            return q;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int readData(InputStream is){

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line = "";
        try {
            // step over header
            reader.readLine();
            int indQuestionSet = 0;
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
//                if (type.contains("Map")) {
//                    typeActivity = "MapActivity";
//                    List<Question> e = new ArrayList<>();
//                    questionSets.add(e);
//                    numQuestionSet++;
//                }
//                else if(type.contains("Short Term Memory")) {
//                    typeActivity = "ShortMemActivity";
//                }
//                else if(type.contains("Spatial Reasoning")) {
//                    typeActivity = "SpatReasonActivity";
//                }
                if(type.contains("Reading Comprehension")) {
                    typeActivity = "ReadCompActivity";
                }
//                else if(type.contains("Pattern")) {
//                    typeActivity = "PatternActivity";
//                }
//                else if(type.contains("Visual Search - TEXT")) {
//                    typeActivity = "VisSearchActivity";
//                }
//                else if(type.contains("Typing")) {
//                    typeActivity = "TypingActivity";
//                }
//                else if(type.contains("Visual Search - IMAGE")) {
//                    typeActivity = "VisSearchImgActivity";
//                }
//                else if(type.contains("Word Search")) {
//                    typeActivity = "WordSearchActivity";
//                }
//                else if(type.contains("Subjective")) {
//                    typeActivity = "SubjectiveActivity";
//                }
//                else if(type.contains("Surrogate Reference Task")) {
//                    typeActivity = "SurRefActivity";
//                }
//                else if(type.contains("Spot the Difference")) {
//                    typeActivity = "SpotDiffActivity";
//                }
//                else if(type.contains("Reaction Time")) {
//                    typeActivity = "ReactTimeActivity";
//                }
//                else if(type.contains("Video")) {
//                    typeActivity = "VideoActivity";
//                }


                if(typeActivity != null) {
                    Question question = new Question(id,type,typeActivity,classification,answerType,correctAnswer,questionNumber, instruction,
                            imgPath,surveyQuestion,answerOptions,questionCode);
                    question.printQuestionAttributes();
                    questions.add(question);

//                    if(prevType.equals(typeActivity)){
//                        indQuestionSet++;
//                    }else{
//                        indQuestionSet=0;
//                    }
//                    indQuestionSet = Math.floorMod(indQuestionSet, numSets);
                    Log.wtf("dj", "#questions " + questions.size());
                    Log.wtf("dj", "#indset " + indQuestionSet);

//                    if(questions.size()<=numSets) {
//                        List<Question> e = new ArrayList<>();
//                        questionSets.add(e);
//                        numQuestionSet++;
//                    }
//                    if(questionSets.get(0).size()<numBaseline){
//                        indQuestionSet=0;
//                    }else{
//                        indQuestionSet=1;
//                    }
//                    questionSets.get(indQuestionSet).add(question);
////                    prevType = typeActivity;
//                    indQuestionSet++;

                } else{
                    Log.wtf("MyActivity", "No corresponding question type on line: " + line);
                }
            }

            // Collections.shuffle(questions);
            is.close();
            return questions.size();
        }catch (Exception e){
            Log.wtf("MyActivity", "Error reading data file on line: " + line, e);
            e.printStackTrace();
        }
        return 0;
    }
}
