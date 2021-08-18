package com.example.surveyapp;

public class WordSearch extends Question {
    WordSearch(int id, String classification, String answerType, String correctAnswer,
                 String instruction, String question, String imgPath, String[] answerOptions,
                 String questionCode){
        super(id, classification, answerType, correctAnswer,
                instruction, question, imgPath, answerOptions,
                questionCode);
    }
}