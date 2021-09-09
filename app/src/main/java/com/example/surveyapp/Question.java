package com.example.surveyapp;

import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;

public class Question implements Serializable {
    private int id;
    private String typeActivity;
    private String classification;
    private String answerType;
    private String correctAnswer;
    private String questionNumber;
    private String instruction;
    private String imgPath;
    private String question;
    private String[] answerOptions;
    private String questionCode;


    Question(int id, String typeActivity, String classification, String answerType, String correctAnswer, String questionNumber,
             String instruction,String imgPath, String question, String[] answerOptions,
             String questionCode){
        this.id = id;
        this.typeActivity = typeActivity;
        this.classification = classification;
        this.answerType = answerType;
        this.correctAnswer = correctAnswer;
        this.questionNumber = questionNumber;
        this.instruction = instruction;
        this.question = question;
        this.imgPath = imgPath;
        this.answerOptions = answerOptions;
        this.questionCode = questionCode;
    }

    public void printQuestionAttributes(){
        Log.d("Question", "id: " + id );
        Log.d("Question", "typeActivity: " + typeActivity);
        Log.d("Question", "classification: " + classification );
        Log.d("Question", "answerType: " + answerType );
        Log.d("Question", "correctAnswer: " + correctAnswer );
        Log.d("Question", "questionNumber: " + questionNumber );
        Log.d("Question", "instruction: " + instruction );
        Log.d("Question", "imgPath: " + imgPath );
        Log.d("Question", "question: " + question );
        Log.d("Question", "answerOptions: " + Arrays.toString(answerOptions) );
        Log.d("Question", "questionCode: " + questionCode );
    }

    public int getId() {
        return id;
    }

    public String getTypeActivity() { return typeActivity; }

    public String getClassification() {
        return classification;
    }

    public String getAnswerType() {
        return answerType;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getQuestion() {
        return question;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public String getQuestionCode() {
        return questionCode;
    }
}
