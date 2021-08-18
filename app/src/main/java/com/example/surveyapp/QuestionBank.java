package com.example.surveyapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    private List<Question> questions;
    private int indexQuestion;

    public QuestionBank(InputStream is) {
        this.indexQuestion = 0;
        this.questions = new ArrayList<>();
        readData(is);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getNextQuestion(){
        return questions.get(indexQuestion);
    }

    private void readData(InputStream is){

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line = "";
        try {
            // step over header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity", "Line: " + line );
                //split string
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                //read data
                //Question sample = new Question(tokens);
                int id = Integer.parseInt(tokens[0]);
                String type = tokens[1];
                String[] answerOptions;
                if(tokens[8].length() > 0) {
                    answerOptions = tokens[8].split(",");
                }else{
                    answerOptions = new String[0];
                }
                Question question;
                switch(type) { // in Java 7
                    case "Map":
                        question = new MultipleChoice(id,tokens[2],tokens[3],tokens[4],tokens[5],
                                tokens[6],tokens[7],answerOptions,tokens[9]);
                        question.printQuestionAttributes();
                        questions.add(question);
                        break;
                }
            }
            is.close();
        }catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on  line: " + line, e);
            e.printStackTrace();
        }
    }
}
