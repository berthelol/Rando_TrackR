package com.example.loic.rando_trackr;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by loic on 07/12/2016.
 */

public class Hiking_data extends Fragment {
    ListView listView;
    static ArrayAdapter<String> adapter;

    private ListView myList;
    private Step_Intput_ListView_Adapter myAdapter;

    //Button add a step
    Button add_step_btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accueil_hiking, container, false);

        /*listView = (ListView)rootView.findViewById(R.id.hiking_listview);

        ArrayList<String> listItems=new ArrayList<String>();

        adapter=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        adapter.add("New Item");
        adapter.add("New Item2");*/
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myList = (ListView) getView().findViewById(R.id.hiking_listview);
        myList.setItemsCanFocus(true);
        myAdapter = new Step_Intput_ListView_Adapter(getContext());
        myList.setAdapter(myAdapter);

        //Init and set a click listenner for adding a step
        add_step_btn= (Button) getView().findViewById(R.id.add_step_btn);
        add_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.additem();
            }
        });
    }
}
