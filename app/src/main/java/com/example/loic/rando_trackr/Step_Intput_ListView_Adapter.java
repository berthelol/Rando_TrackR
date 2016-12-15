package com.example.loic.rando_trackr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.R.attr.label;
import static android.R.attr.type;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by loic on 07/12/2016.
 */

public class Step_Intput_ListView_Adapter extends BaseAdapter {

    //Inflater layout
    private LayoutInflater mInflater;

    //Array of the item to display
    public ArrayList <Waypoint> myItems = new ArrayList();

    //Context from parent
    Context context;

    //Database randotrackR
    SQLiteDatabase randoTrackRDB;

    public Step_Intput_ListView_Adapter(Context mainActivity) {
        //Intialize context
        this.context=mainActivity;
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
                myItems.add(waypoint);
            }
            resultSet.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        notifyDataSetChanged();
    }

    public void refresh()
    {
        myItems.clear();
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
                myItems.add(waypoint);
            }
            resultSet.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public void additem()
    {
        Waypoint listItem = new Waypoint((getCount()+1),"","ADRESSE",new Double(-1),new Double(-1));
        this.randoTrackRDB.execSQL("INSERT INTO Waypoint VALUES("+(getCount()+1)+",'','ADRESSE',-1,-1);");
       // myItems.add(listItem);
        refresh();
    }
    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Create objectview of layout
        final ViewHolder holder = new ViewHolder();
        //Initiate the view from layout
        convertView = mInflater.inflate(R.layout.step_listview_input, null);

        //Initialize the objects from the layout
        holder.stepinput = (EditText) convertView.findViewById(R.id.step_input);
        holder.flag = (ImageView) convertView.findViewById(R.id.step_flag);
        holder.delete = (ImageView) convertView.findViewById(R.id.delete_icon);
        holder.textInputLayout = (TextInputLayout)convertView.findViewById(R.id.textinputlayout);
        holder.geocode_status = (ImageView) convertView.findViewById(R.id.location_status);

        //If it's the last item we put a finish flag else normal flag
        if(position==getCount()-1)
        {
            holder.flag.setImageResource(R.drawable.step_finish_flag);

        }else
        {
            holder.flag.setImageResource(R.drawable.step_flag);
        }

        if(myItems.get(position).type.equals("COORD")||myItems.get(position).latitude !=-1||myItems.get(position).longitude !=-1)
        {
            holder.geocode_status.setImageResource(R.drawable.location_sucess);
        }
        //Create listenner to delete the item
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    //Delete the item in the db
                    randoTrackRDB.execSQL("DELETE FROM Waypoint WHERE Waypointnb="+(position+1)+";");
                    Toast toast = Toast.makeText(context, "Etape effacée", Toast.LENGTH_SHORT);
                    toast.show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(context, "Etape non effacée", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //Make sure the step after the step deleted are in order
                adjustestepnumbers(position);
                //refresh the view
               refresh();
            }
        });
        //set the name of the step
        holder.stepinput.setText(myItems.get(position).adresse);
        //Set the hint of the step input
        holder.textInputLayout.setHint("Etape "+(position+1));
        if(myItems.get(position).type.equals("COORD")&&myItems.get(position).adresse.isEmpty())
        {
            holder.textInputLayout.setHint("Etape "+(position+1)+" (set a label)");
        }
        holder.stepinput.setId(position);

        //Change the color of the flag to red when data has changed
        holder.stepinput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                //If the it's the last item we put a finish flag else normal flag
                if(position==getCount()-1)
                {
                    holder.flag.setImageResource(R.drawable.step_unsave_finish_flag);

                }else
                {
                    holder.flag.setImageResource(R.drawable.step_unsave_flag);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        //Save the step to the db
        holder.flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the geocding object if needed
                LatLng geocoded_adress = null;
                String formatted_address = holder.stepinput.getText().toString();
                //Check if the new adress is geocodable
                if(myItems.get(position).type.equals("ADRESSE"))
                {
                    //Set the image to a loading image
                    holder.geocode_status.setImageResource(R.drawable.location_loading);
                    //Create the geocoder object
                    Geocoder geocoder = new Geocoder();
                    //execute the task
                    geocoder.execute(holder.stepinput.getText().toString());
                    try {
                        //fetching the response
                        JSONObject jsonresponse =geocoder.get();
                        formatted_address = jsonresponse.getString("formatted_address");
                        geocoded_adress = new LatLng(jsonresponse.getDouble("lat"),jsonresponse.getDouble("lng"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(geocoded_adress!=null)
                    {
                        Toast toast = Toast.makeText(context, "Location trouvé", Toast.LENGTH_LONG);
                        toast.show();
                        holder.stepinput.setText(formatted_address.toString());
                        holder.geocode_status.setImageResource(R.drawable.location_sucess);
                    }else {
                        Toast toast = Toast.makeText(context, "Erreur localisation, soyez plus précis", Toast.LENGTH_LONG);
                        toast.show();
                        holder.geocode_status.setImageResource(R.drawable.location_failed);
                    }
                }
                //change color of the save button to notify the values has being saved
                //If the it's the last item we put a finish flag else normal flag
                if(position==getCount()-1)
                {
                    holder.flag.setImageResource(R.drawable.step_finish_flag);

                }else
                {
                    holder.flag.setImageResource(R.drawable.step_flag);
                }
                //Modify in the DB
                try
                {
                    if(geocoded_adress==null)
                    {
                        Log.i("gecoder ","null");
                        //No new lat lng data
                        randoTrackRDB.execSQL("UPDATE Waypoint SET Adresse = \'"+holder.stepinput.getText().toString()+"\' WHERE Waypointnb="+(position+1)+";");

                    }else
                    {
                        //add lat and lng to data
                        randoTrackRDB.execSQL("UPDATE Waypoint SET Adresse = \'"+formatted_address+"\' , Latitude = "+geocoded_adress.latitude+" , Longitude = "+geocoded_adress.longitude+" WHERE Waypointnb="+(position+1)+";");

                    }
                        Toast toast = Toast.makeText(context, "Modifications effectuées", Toast.LENGTH_SHORT);
                    toast.show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(context, "Modifications NON effectuées", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return convertView;
    }
    //View holder store the layout's item
    class ViewHolder {
        TextInputLayout textInputLayout;
        EditText stepinput;
        ImageView flag;
        ImageView geocode_status;
        ImageView delete;
    }
    //Make sure the item numbers after the one deleted are ajusted
    public void adjustestepnumbers(int deletedpostion)
    {
        for(int i =deletedpostion;i<myItems.size()+1;i++)
        {
            try
            {
                //Update in DB
                randoTrackRDB.execSQL("UPDATE Waypoint SET Waypointnb = "+i+" WHERE Waypointnb="+(i+1)+";");

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        refresh();
    }
}

