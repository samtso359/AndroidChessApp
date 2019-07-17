package com.example.sam.chess;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.content.Intent;
import android.widget.TextView;

import java.io.File;


public class PopUp extends Activity {
    String returnString = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotional_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;



        getWindow().setLayout((int)(width*.7),(int)(height*.7));

    }

    public void Yes(View v){
        EditText recordingName= (EditText)findViewById(R.id.recordingName);
        returnString = recordingName.getText().toString();
        TextView warningLabel = (TextView)findViewById(R.id.warningLabel);

        Context context = getApplicationContext();
        File directory = context.getFilesDir();
        File[] listofFiles = directory.listFiles();
        for (int j =0; j < listofFiles.length; j++){
            System.out.println(listofFiles[j].getName());
            if (listofFiles[j].getName().compareTo(returnString) == 0){
                warningLabel.setText("Name already in use, Choose a new name");
                return;
            }
        }


        Intent i = new Intent();


        if(returnString.length() == 0){
            warningLabel.setText("Please Enter a Name");
            return;
        }
        i.putExtra("message",returnString);

        setResult(RESULT_OK,i);
        finish();



    }

    public void No(View v){
        Intent i = new Intent();

        i.putExtra("message",0);
        setResult(RESULT_CANCELED,i);
        finish();
    }
}