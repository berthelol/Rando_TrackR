package com.example.loic.rando_trackr.Map_data;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by loic on 16/12/2016.
 */
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

    //list of distance from one point from another
    ArrayList <Value_total> distance ;
    //list of duration from one point from another
    ArrayList <Value_total> duration ;
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        //latlng to trace the polylines
        List<List<HashMap<String, String>>> routes = null;
        try{
            jObject = new JSONObject(jsonData[0]);

            if(!jObject.getString("status").equals("OK")){
                Log.e("ERROR","NO LOCATION FOUND:"+jObject.getString("status"));
                return null;
            }

            DirectionJSONParser parser = new DirectionJSONParser();

            // Starts parsing data
            routes = parser.parseroutes(jObject);
            distance = parser.parsedistance(jObject);
            duration = parser.parseduration(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        super.onPostExecute(result);
    }

    public ArrayList<Value_total> getdistance ()
    {
        return this.distance;
    }

    public ArrayList<Value_total> getduration ()
    {
        return this.duration;
    }
}