package com.example.surveyapp;

abstract class Question {
    private int id;
    private String instruction;
    private String question;
    private String imgPath;

    Question(int id, String instruction, String question, String imgPath){
        this.id = id;
        this.instruction = instruction;
        this.question = question;
        this.imgPath = imgPath;
    }

    final int getId() {
        return id;
    }

    final String getInstruction() {
        return instruction;
    }

    final String getQuestion() {
        return question;
    }

    final String getImgPath() {
        return imgPath;
    }
}
