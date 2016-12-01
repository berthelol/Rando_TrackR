package com.example.loic.rando_trackr;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import static android.R.attr.data;
import static android.R.attr.fragment;

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
        sharedPreferences = getContext().getSharedPreferences("com.example.loic.rando_trackr", Context.MODE_PRIVATE);


        data_change=false;
        //declare all object from view
        firstname = (EditText) getView().findViewById(R.id.firstname);
        lastname = (EditText) getView().findViewById(R.id.lastname);
        urgency_number =(EditText) getView().findViewById(R.id.sos);
        call_check = (CheckBox) getView().findViewById(R.id.call);
        sms_check = (CheckBox) getView().findViewById(R.id.sms);
        savebutton= (Button) getView().findViewById(R.id.savebutton);

        firstname.setText(sharedPreferences.getString("firstname",""));
        lastname.setText(sharedPreferences.getString("lastname",""));
        urgency_number.setText(sharedPreferences.getString("phonenumber",""));
        sms_check.setChecked(sharedPreferences.getBoolean("sms_checked",false));
        call_check.setChecked(sharedPreferences.getBoolean("call_checked",false));

        /*try {
            SQLiteDatabase database = getContext().openOrCreateDatabase("RandoTrackR", Context.MODE_PRIVATE,null);
            Cursor c =database.rawQuery("SELECT * FROM user",null);

            int firstnameindex = c.getColumnIndex("firstname");
            int lastnameindex = c.getColumnIndex("lastname");
            int nburgenceindex = c.getColumnIndex("nburgence");

            c.moveToFirst();
            while (c!=null)
            {
                firstname.setText(c.getString(firstnameindex));
                Log.i("in profile firstname:",c.getString(firstnameindex));
                lastname.setText(c.getString(lastnameindex));
                Log.i("in profile lastname:",c.getString(lastnameindex));
                urgency_number.setText(c.getString(nburgenceindex));
                Log.i("in profile nb urgence:",c.getString(nburgenceindex));
                c.moveToNext();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/

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
            /*try {
                SQLiteDatabase database = getContext().openOrCreateDatabase("RandoTrackR", Context.MODE_PRIVATE,null);
                Cursor c =database.rawQuery("INSERT INTO RandoTrackR (firstname, lastname, nburgence)\n" +
                        "VALUES ("+firstname.getText()+","+lastname.getText()+","+urgency_number.getText()+");",null);

            }catch (Exception e)
            {
                e.printStackTrace();
            }*/

                }
                Toast toast = Toast.makeText(getContext(), "Modifications saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}