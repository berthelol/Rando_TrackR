package com.example.loic.rando_trackr.User;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loic.rando_trackr.R;

import static android.R.attr.data;
import static android.R.attr.fragment;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Loïc on 18/09/16.
 */


public class Profil extends Fragment {

    private Boolean data_change;
    private EditText firstname;
    private EditText lastname;
    private EditText urgency_number;
    private CheckBox sms_check;
    private CheckBox call_check;
    private Button savebutton ;
    private ImageView medal_10;
    private TextView medal_text_10;
    private ImageView medal_50;
    private TextView medal_text_50;
    private ImageView medal_100;
    private TextView medal_text_100;
    //Used to fecth user's data
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profil");
        //create the sharedpreferences object to get the user's data
        sharedPreferences = getContext().getSharedPreferences("com.example.loic.rando_trackr", MODE_PRIVATE);


        data_change=false;
        //declare all object from view
        firstname = (EditText) getView().findViewById(R.id.firstname);
        lastname = (EditText) getView().findViewById(R.id.lastname);
        urgency_number =(EditText) getView().findViewById(R.id.sos);
        call_check = (CheckBox) getView().findViewById(R.id.call);
        sms_check = (CheckBox) getView().findViewById(R.id.sms);
        savebutton= (Button) getView().findViewById(R.id.savebutton);
        //medals
        medal_10 = (ImageView) getView().findViewById(R.id.medal_10);
        medal_text_10 =(TextView) getView().findViewById(R.id.medal_text_10);
        medal_50 = (ImageView) getView().findViewById(R.id.medal_50);
        medal_text_50 =(TextView) getView().findViewById(R.id.medal_text_50);
        medal_100 = (ImageView) getView().findViewById(R.id.medal_100);
        medal_text_100 =(TextView) getView().findViewById(R.id.medal_text_100);

        firstname.setText(sharedPreferences.getString("firstname",""));
        lastname.setText(sharedPreferences.getString("lastname",""));
        urgency_number.setText(sharedPreferences.getString("phonenumber",""));
        sms_check.setChecked(sharedPreferences.getBoolean("sms_checked",false));
        call_check.setChecked(sharedPreferences.getBoolean("call_checked",false));

        check_for_medal_unlock();

        //add all listenners to theses objects
        firstname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        lastname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        urgency_number.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        sms_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }
        });

        call_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }
        });
        //add listenner to save button
        final Button save_btn =(Button) getView().findViewById(R.id.savebutton);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_btn.setBackgroundResource(R.drawable.mysavebutton);
                if(data_change)
                {
                    sharedPreferences.edit().putString("firstname",firstname.getText().toString()).apply();
                    sharedPreferences.edit().putString("lastname",lastname.getText().toString()).apply();
                    sharedPreferences.edit().putString("phonenumber",urgency_number.getText().toString()).apply();
                    sharedPreferences.edit().putBoolean("sms_checked",sms_check.isChecked()).apply();
                    sharedPreferences.edit().putBoolean("call_checked",call_check.isChecked()).apply();
                    data_change=false;
                }
                Toast toast = Toast.makeText(getContext(), "Modifications saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void check_for_medal_unlock() {
        int distance_travelled=0;
        SQLiteDatabase randoTrackRDB = getActivity().openOrCreateDatabase("RandoTrackR",MODE_PRIVATE,null);
        Cursor resultSet;
        try {
            //Fetch the data from DB
            resultSet = randoTrackRDB.rawQuery("Select Distance from Historical_distance_travelled",null);
            int i=0;
            while (resultSet.moveToNext())
            {
                int distance = resultSet.getInt(resultSet.getColumnIndex("Distance"));
                distance_travelled+=distance/1000;
            }
            resultSet.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        medal_text_10.setText(""+(distance_travelled)+"/10 km");
        medal_text_50.setText(""+(distance_travelled)+"/50 km");
        medal_text_100.setText(""+(distance_travelled)+"/100 km");
        if(distance_travelled>10)
        {
            medal_10.setImageResource(R.drawable.medal);
        }
        if(distance_travelled>50)
        {
            medal_50.setImageResource(R.drawable.medal);
        }
        if(distance_travelled>100)
        {
            medal_100.setImageResource(R.drawable.medal);
        }
    }
}