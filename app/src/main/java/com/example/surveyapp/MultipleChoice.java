package com.example.surveyapp;

import java.util.List;

class MultipleChoice extends Question{
    private List<String> answers;

    MultipleChoice(int id, String instruction, String question, String imgPath, List<String> answers){
        super(id, instruction, question, imgPath);
        this.answers = answers;
    }
}
