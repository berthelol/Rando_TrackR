package com.example.loic.rando_trackr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by loic on 07/12/2016.
 */

public class Step_Output_ListView_Adapter extends BaseAdapter {
    //Parent context
    Context context;

    //Array of the item to display
    public ArrayList<Waypoint> mywaypoints = new ArrayList();

    //Database randotrackR
    SQLiteDatabase randoTrackRDB;

    //Inflater layout
    private static LayoutInflater inflater;

    //Constructor
    public Step_Output_ListView_Adapter(Context context) {
        this.context=context;

        //Initialize DB
        this.randoTrackRDB = this.context.openOrCreateDatabase("RandoTrackR",MODE_PRIVATE,null);
        Cursor resultSet;
        try {
            //Fetch the data from DB
            resultSet = randoTrackRDB.rawQuery("Select * from Waypoint",null);
            while (resultSet.moveToNext()) {
                String adresse = resultSet.getString(resultSet.getColumnIndex("Adresse"));
                String type = resultSet.getString(resultSet.getColumnIndex("Type"));
                Double lat = resultSet.getDouble(resultSet.getColumnIndex("Latitude"));
                Double lng = resultSet.getDouble(resultSet.getColumnIndex("Longitude"));
                int waypointnb = resultSet.getInt(resultSet.getColumnIndex("Waypointnb"));
                Waypoint waypoint = new Waypoint(waypointnb,adresse,type,lat,lng);
                mywaypoints.add(waypoint);
            }
            resultSet.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mywaypoints.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView info;
        ImageView flag;
        TextView data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.step_listview_layout, null);

        //Get the specifique item in the step_listview_layout
        holder.info=(TextView) rowView.findViewById(R.id.step_info);
        holder.flag=(ImageView) rowView.findViewById(R.id.step_flag);
        holder.data=(TextView) rowView.findViewById(R.id.step_distant);

        //Set the values in the textboxes
        //IF it's not the last item
        if(position != (getCount()-1))
        {
            holder.info.setText("Prochaine étape "+mywaypoints.get(position).waypointnb+":\n"+mywaypoints.get(position).adresse);
            holder.flag.setImageResource(R.drawable.step_flag);
        }else
        {
            //last item we change the image and the label
            holder.info.setText("Dernière étape "+mywaypoints.get(position).waypointnb+":\n"+mywaypoints.get(position).adresse);
            holder.flag.setImageResource(R.drawable.step_finish_flag);
        }
        holder.data.setText("Mètres restant: "+"TODO");
        //Set the on click listenner on the flag
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Etape "+mywaypoints.get(position).waypointnb)
                        .setMessage("Adresse "+mywaypoints.get(position).adresse+"\nLatitude: "+mywaypoints.get(position).latitude+"\nLongitude: "+mywaypoints.get(position).longitude)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(R.drawable.randotrackr)
                        .show();
            }
        });
        return rowView;
    }
}
