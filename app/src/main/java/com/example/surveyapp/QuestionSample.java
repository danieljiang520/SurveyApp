package com.example.surveyapp;

public class QuestionSample {
    private String type;
    private String instruction;
    private String question;
    private String answer1;
    private String answer2;
    private String correct_answer;

    public QuestionSample(String[] tokens) {
        setType(tokens[0]);
        setInstruction(tokens[1]);
        setQuestion(tokens[2]);
        setAnswer1(tokens[3]);
        setAnswer2(tokens[4]);
        setCorrect_answer(tokens[5]);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }
}
