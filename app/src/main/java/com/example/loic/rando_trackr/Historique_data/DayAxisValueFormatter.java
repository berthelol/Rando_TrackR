package com.example.loic.rando_trackr.Historique_data;


import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by loic on 17/12/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter
{
    //Date format
    SimpleDateFormat sdf ;
    //First date registered
    long firstdate_unix_time=0;
    public DayAxisValueFormatter(long firstdate) {
        //Log.i("First date",firstdate);
        sdf = new SimpleDateFormat("yyyy:MM:dd");

            this.firstdate_unix_time = firstdate;

        //Log.i("Firstdate Unix",""+firstdate_unix_time);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int daynumber =(int)value;
        int onedayinsecond=86400;
        float unix_date=(onedayinsecond*daynumber)+firstdate_unix_time;
        Date date = new Date((long)unix_date*1000);
       // Log.i("Unix date",""+unix_date);
       // Log.i("Dateto display",sdf.format(date));
        return sdf.format(date);
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}

