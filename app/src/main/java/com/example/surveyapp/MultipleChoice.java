package com.example.surveyapp;

import java.util.List;

class MultipleChoice extends Question{

    MultipleChoice(int id, String classification, String answerType, String correctAnswer,
                   String instruction, String question, String imgPath, String[] answerOptions,
                   String questionCode){
        super(id, classification, answerType, correctAnswer,
                instruction, question, imgPath, answerOptions,
                questionCode);
    }
}
