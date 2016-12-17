package com.example.loic.rando_trackr.Historique_data;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loic.rando_trackr.Historique_data.DayAxisValueFormatter;
import com.example.loic.rando_trackr.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.media.CamcorderProfile.get;


/**
 * Created by Loïc on 18/09/16.
 */

public class Historique extends Fragment {
    //Database object
    SQLiteDatabase randoTrackRDB;

    //Array list of chart data
    List<Integer> chart_data = new ArrayList<>();
    //Start date
    long start_date=0;
    //Date format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
    //Bar chart
    protected BarChart barChart;
    //Total text view
    private TextView total_distance;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_historique, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Historique");

        //Initialse database
        this.randoTrackRDB = getContext().openOrCreateDatabase("RandoTrackR",MODE_PRIVATE,null);

        total_distance= (TextView)getView().findViewById(R.id.total);
        //fetch data from db
        getdatafromDB();

        barChart = (BarChart) getView().findViewById(R.id.graph_1);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be drawn
        barChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(start_date);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        setData();

        barChart.animateX(500);
        barChart.animateY(1000);
    }

    private void setData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < chart_data.size() ; i++) {
            float val =(float)chart_data.get(i);
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Distance parcouru (mètres)");
            set1.setColors(ColorTemplate.LIBERTY_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(7f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            barChart.setData(data);
        }
    }

    public class DB_Object
    {
        public long date_unix;
        public int distance;
        DB_Object(long date_unix,int distance)
        {
            this.date_unix=date_unix;
            this.distance=distance;
        }
    }
    public void getdatafromDB()
    {

        int total_dist=0;
        long last_date=0;
        ArrayList<DB_Object> datafromdb = new ArrayList<>();
        Cursor resultSet;
        try {
            //Fetch the data from DB
            resultSet = randoTrackRDB.rawQuery("Select * from Historical_distance_travelled",null);
            int i=0;
            resultSet.moveToFirst();
            String first_date = resultSet.getString(resultSet.getColumnIndex("Date"));
            try {
                //Set the starting date in unix time in seconds
                start_date = sdf.parse(first_date).getTime()/1000;
                int distance = resultSet.getInt(resultSet.getColumnIndex("Distance"));
                datafromdb.add(new DB_Object(start_date,distance));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (resultSet.moveToNext())
            {
                //Get the date and parse it to unix time
                String date = resultSet.getString(resultSet.getColumnIndex("Date"));
                long date_unix =0;
                try {
                    //Set the starting date in unix time in seconds
                    date_unix = sdf.parse(date).getTime()/1000;
                    last_date= sdf.parse(date).getTime()/1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int distance = resultSet.getInt(resultSet.getColumnIndex("Distance"));
                datafromdb.add(new DB_Object(date_unix,distance));
                total_dist+=distance;
                i++;
            }

            resultSet.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        long date=start_date;
        int i =0;
        int stop=0;
        while(date<(last_date+1)&&stop!=100)
        {
            //If this day has been registered
            if(date==datafromdb.get(i).date_unix)
            {
                chart_data.add(datafromdb.get(i).distance);
                i++;
            }else //else create empty one
            {
                chart_data.add(0);
            }
            stop++;
            date+=86400;
        }

        total_distance.setText("Total: "+(total_dist/1000)+" km");
    }
}
