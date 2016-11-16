package com.example.loic.rando_trackr;


import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Toast;

import static android.R.attr.fragment;

/**
 * Created by Loïc on 18/09/16.
 */


public class Profil extends Fragment {

    private EditText firstname;
    private EditText lastname;
    private EditText urgency_number;
    private Button savebutton ;
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
        //declare all object from view
        firstname = (EditText) getView().findViewById(R.id.firstname);
        lastname = (EditText) getView().findViewById(R.id.lastname);
        urgency_number =(EditText) getView().findViewById(R.id.sos);
        savebutton= (Button) getView().findViewById(R.id.savebutton);

        //add all listenners to theses objects
        firstname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        lastname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        urgency_number.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }
}