package com.example.loic.rando_trackr.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.loic.rando_trackr.R;

/**
 * Created by Loïc on 18/09/16.
 */

public class Parametres extends Fragment {
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_parametres, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Parametres");

        //create the sharedpreferences object to get the user's data
        sharedPreferences = getContext().getSharedPreferences("com.example.loic.rando_trackr", Context.MODE_PRIVATE);

        //Instantiate the spinners
        Spinner spinner1 = (Spinner) getView().findViewById(R.id.spinner_item1);
        Spinner spinner2 = (Spinner) getView().findViewById(R.id.spinner_item2);
        Spinner spinner3 = (Spinner) getView().findViewById(R.id.spinner_item3);
        Spinner spinner4 = (Spinner) getView().findViewById(R.id.spinner_item4);

        //Set ressources to the spinner and implementing the listenner
        spinner1.setAdapter(new ArrayAdapter<Quick_info>(getContext(), android.R.layout.simple_spinner_item, Quick_info.values()));
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id)
            {
                //get the enum Quick_info after a click
                Quick_info quick_info= (Quick_info) parent.getSelectedItem();
                //save it to the preferences
                sharedPreferences.edit().putString("option1",quick_info.name()).commit();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        spinner2.setAdapter(new ArrayAdapter<Quick_info>(getContext(), android.R.layout.simple_spinner_item, Quick_info.values()));
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id)
            {
                //get the enum Quick_info after a click
                Quick_info quick_info= (Quick_info) parent.getSelectedItem();
                //save it to the preferences
                sharedPreferences.edit().putString("option2",quick_info.name()).commit();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        spinner3.setAdapter(new ArrayAdapter<Quick_info>(getContext(), android.R.layout.simple_spinner_item, Quick_info.values()));
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id)
            {
                //get the enum Quick_info after a click
                Quick_info quick_info= (Quick_info) parent.getSelectedItem();
                //save it to the preferences
                sharedPreferences.edit().putString("option3",quick_info.name()).commit();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        spinner4.setAdapter(new ArrayAdapter<Quick_info>(getContext(), android.R.layout.simple_spinner_item, Quick_info.values()));
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id)
            {
                //get the enum Quick_info after a click
                Quick_info quick_info= (Quick_info) parent.getSelectedItem();
                //save it to the preferences
                sharedPreferences.edit().putString("option4",quick_info.name()).commit();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        //Set the selected item of the spinner with the one the user registered earlier
        spinner1.setSelection(Quick_info.valueOf(sharedPreferences.getString("option1","Not_defined")).getindex());
        spinner2.setSelection(Quick_info.valueOf(sharedPreferences.getString("option2","Not_defined")).getindex());
        spinner3.setSelection(Quick_info.valueOf(sharedPreferences.getString("option3","Not_defined")).getindex());
        spinner4.setSelection(Quick_info.valueOf(sharedPreferences.getString("option4","Not_defined")).getindex());
    }
}