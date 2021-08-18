package com.example.surveyapp;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MultipleChoice extends AppCompatActivity {

    MCButton answer1;
    MCButton answer2;
    MCButton answer3;
    MCButton answer4;
    MCButton answer5;
    GetTimeStamp timeStamps;
    Boolean fiveAnswers;
    String selected;

    public static class MCButton{
        Button button;
        String buttonName;
    }

    public void selectButton(MCButton choice){
        if(!fiveAnswers){
            answer5.button.setVisibility(View.GONE);
        }
        else{
            answer5.button.setVisibility(View.VISIBLE);
        }
        choice.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                answer1.button.setSelected(false);
                answer2.button.setSelected(false);
                answer3.button.setSelected(false);
                answer4.button.setSelected(false);
                answer5.button.setSelected(false);
                choice.button.setSelected(true);
                selected = choice.buttonName;
            }
        });
    }
}
