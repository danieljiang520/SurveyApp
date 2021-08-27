package com.example.surveyapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class InitChoiceButtons  extends AppCompatActivity {
    private List<Button> choiceList = new ArrayList<Button>();
    private String selected = "N/A";
    private GetTimeStamp timeStamps = new GetTimeStamp();

    InitChoiceButtons(Activity activity, String activityName, String[] answerOptions){
        for(int i = 1; i <= 5; ++i){
            Button choice;
            String buttonID = activityName + i;
            int resID = activity.getResources().getIdentifier(buttonID, "id", activity.getPackageName());
            choice = activity.findViewById(resID);
            if(i <= answerOptions.length){
                choice.setVisibility(View.VISIBLE);
                choice.setText(answerOptions[i-1]);
            }else{
                choice.setVisibility(View.GONE);
            }
            selectButton(choice);
            choiceList.add(choice);
        }
    }
    public void selectButton(Button choice){
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamps.updateTimeStamp();
                for(int i=0; i < choiceList.size(); ++i){
                    choiceList.get(i).setSelected(false);
                }
                choice.setSelected(true);
                selected = choice.getText().toString();
            }
        });
    }

    public String getSelected() {
        return selected;
    }

    public GetTimeStamp getTimeStamps() {
        return timeStamps;
    }
}
