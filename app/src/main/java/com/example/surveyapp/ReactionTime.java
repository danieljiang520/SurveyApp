package com.example.surveyapp;

import android.util.Pair;

import java.util.List;

class ReactionTime extends Question{
    private List<Pair<String, String>> textAndColors;

    ReactionTime(int id, String instruction, String question, String imgPath, List<Pair<String, String>> textAndColors){
        super(id, instruction, question, imgPath);
        this.textAndColors = textAndColors;
    }
}
