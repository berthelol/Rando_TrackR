package com.example.loic.rando_trackr.Map_direction;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by loic on 16/12/2016.
 */
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;
        Log.i("JSON from google API",jsonData[0]);
        try{
            jObject = new JSONObject(jsonData[0]);

            if(!jObject.getString("status").equals("OK")){
                Log.e("ERROR","NO LOCATION FOUND:"+jObject.getString("status"));
                return null;
            }

            DirectionJSONParser parser = new DirectionJSONParser();

            // Starts parsing data
            routes = parser.parse(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        if(result==null)
        {
            return ;
        }
        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(5);
            lineOptions.color(Color.RED);
        }
            //return lineOptions;
        // Drawing polyline in the Google Map for the i-th route
        //polylines = googleMap.addPolyline(lineOptions);
    }
}
