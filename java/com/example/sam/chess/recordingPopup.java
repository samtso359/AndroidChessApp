package com.example.sam.chess;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class recordingPopup extends Activity {
    ListView listofrecordings;
    ArrayAdapter<String> listadapter;
    String selected;
    Boolean selecting = false;
    TextView textview;
    File[] listofFiles;
    ArrayList<File> listOfFilesAR = new ArrayList<File>();

    ArrayList<String> sortedListofFileNames = new ArrayList<String>();
    ArrayList<String> listOfFileNames= new ArrayList<String>();

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordinglist);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        textview = (TextView) findViewById(R.id.textView);
        getWindow().setLayout((int)(width*.9),(int)(height*.9));

        Context context = getApplicationContext();
        File directory = context.getFilesDir();

        listofFiles = directory.listFiles();

        for (int j =0; j < listofFiles.length; j++){
            listOfFileNames.add(listofFiles[j].getName().toString());
            listOfFilesAR.add(listofFiles[j]);
        }
        Collections.sort(listOfFileNames);



        listofrecordings = (ListView)findViewById(R.id.listview);
        listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfFileNames);
        if (listOfFileNames.size() > 0){
            listofrecordings.setAdapter(listadapter);


        }
        else{
            selected = "";
        }

        Collections.sort(listOfFilesAR,
                new Comparator<File>()
                {
                    public int compare(File f1, File f2)
                    {
                        Date f1date = new Date(f1.lastModified());
                        Date f2date = new Date(f2.lastModified());

                        return (f1date.compareTo(f2date))*-1;
                    }
                });
        final ArrayList<String> holderList = new ArrayList<String>();
        for (int j =0; j < listOfFilesAR.size(); j++){
            holderList.add(listOfFilesAR.get(j).getName().toString());
        }

        listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, holderList);
        if (holderList.size() > 0) {
            listofrecordings.setAdapter(listadapter);
            listofrecordings.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    selected = holderList.get(position);
                    textview.setText("");
                    selecting = true;
                }
            });
        }




    }

    public void play(View v){
        if(selecting == false){
            textview.setText("Please select a recording to play");
            return;
        }

        Intent i = new Intent();

        i.putExtra("recording",selected);
        setResult(RESULT_OK,i);
        System.out.println(selected);
        finish();

    }

    public void cancel(View v){
        Intent i = new Intent();

        i.putExtra("recording",0);
        setResult(RESULT_CANCELED,i);
        finish();
    }

    public void titleradio(View v){


        Collections.sort(listOfFileNames,
                new Comparator<String>()
                {
                    public int compare(String f1, String f2)
                    {
                        return f1.toLowerCase().compareTo(f2.toLowerCase());
                    }
                });

        listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfFileNames);
        if (listOfFileNames.size() > 0) {
            listofrecordings.setAdapter(listadapter);
            listofrecordings.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    selected = listOfFileNames.get(position);
                    textview.setText("");
                    selecting = true;
                }
            });
        }

    }
    public void dateradio(View v){
        Collections.sort(listOfFilesAR,
                new Comparator<File>()
                {
                    public int compare(File f1, File f2)
                    {
                        Date f1date = new Date(f1.lastModified());
                        Date f2date = new Date(f2.lastModified());

                        return (f1date.compareTo(f2date))*-1;
                    }
                });
        ArrayList<String> holderList = new ArrayList<String>();
        for (int j =0; j < listOfFilesAR.size(); j++){
            holderList.add(listOfFilesAR.get(j).getName().toString());
        }
        final ArrayList<String> FinaltempArrayList = holderList;

        listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, holderList);
        if (holderList.size() > 0) {
            listofrecordings.setAdapter(listadapter);
            listofrecordings.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    selected = FinaltempArrayList.get(position);
                    textview.setText("");
                    selecting = true;
                }
            });
        }
    }

}
