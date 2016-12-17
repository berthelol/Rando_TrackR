package com.example.loic.rando_trackr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Loïc on 18/09/16.
 */

public class Parcours extends Fragment implements LocationListener {


    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    /* Position */
    private static final int MINIMUM_TIME = 1000;  // 1s
    private static final int MINIMUM_DISTANCE = 1; // 1m

    /* GPS */
    public String mProviderName;
    public LocationManager mLocationManager;
    public LocationListener mLocationListener;

    //TextViews from the layout
    TextView latitude_display;
    TextView longitude_display;
    TextView altitude_display;
    TextView vitesse_display;
    TextView distance_display;
    TextView temps_display;

    //Output listview
    ListView lv;

    //Decimal formatter
    DecimalFormat decimalFormat;

    //Database randotrackR
    SQLiteDatabase randoTrackRDB;
    //total distanceand time duration of the parcours
    String total_duration="";
    String total_distance="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_parcours, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Parcours");
        //Formatter
        decimalFormat= new DecimalFormat("###.#####");
        //Get total distance
        set_total_distance();
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Get the best provider between gps, network and passive
        Criteria criteria = new Criteria();
        mProviderName = mLocationManager.getBestProvider(criteria, true);


        // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // No one provider activated: prompt GPS
            if (mProviderName == null || mProviderName.equals("")) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            // At least one provider activated. Get the coordinates
            switch (mProviderName) {
                case "passive":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, this);
                    break;

                case "network":
                    break;

                case "gps":
                    break;

            }

            // One or both permissions are denied.
        } else {

            // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }

        }
        Location location = mLocationManager.getLastKnownLocation(mProviderName);
        if(location!=null){
            fill_view(location);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

        lv=(ListView) getView().findViewById(R.id.step_listview);
        lv.setItemsCanFocus(true);
        lv.setAdapter(new Step_Output_ListView_Adapter(getContext()));
    }
    @Override
    public void onLocationChanged(Location location) {
        fill_view(location);
    }
    public void fill_view(Location location)
    {
        //Toast.makeText(getContext(),"update view",Toast.LENGTH_SHORT).show();
        longitude_display =(TextView) getView().findViewById(R.id.longitude_display);
        latitude_display =(TextView) getView().findViewById(R.id.latitude_display);
        altitude_display =(TextView) getView().findViewById(R.id.altitude_display);
        vitesse_display =(TextView) getView().findViewById(R.id.vitesse_display);
        distance_display =(TextView) getView().findViewById(R.id.distance_display);
        temps_display =(TextView) getView().findViewById(R.id.temps_display);

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        double alt = location.getAltitude();
        double speed = location.getSpeed();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        longitude_display.setText("Longitude\n"+decimalFormat.format(lng));
        latitude_display.setText("Latitude\n"+decimalFormat.format(lat));
        altitude_display.setText("Altitude\n"+String.valueOf(alt)+"m");
        vitesse_display.setText("Vitesse\n"+df.format(speed)+"m/s");
        distance_display.setText("Distance restante\n"+total_distance);
        temps_display.setText("Durée éstimée\n"+total_duration);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, this);
    }

    private void set_total_distance() {
        //Formating for distance
        DecimalFormat distance_format = new DecimalFormat("###.## km");
        int local_total_duration=0;
        float local_total_distance=0f;
        this.randoTrackRDB = getContext().openOrCreateDatabase("RandoTrackR",MODE_PRIVATE,null);
        Cursor resultSet;
        try {
            //Fetch the data from DB
            resultSet = randoTrackRDB.rawQuery("Select * from Waypoint",null);

            while (resultSet.moveToNext()) {
                int duration = resultSet.getInt(resultSet.getColumnIndex("Duration_value"));
                local_total_duration+=duration;
                int distance = resultSet.getInt(resultSet.getColumnIndex("Distance_value"));
                local_total_distance+=distance;

            }
            resultSet.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        total_distance = distance_format.format(local_total_distance/1000);
        long second = (local_total_duration) % 60;
        long minute = (local_total_duration / (60)) % 60;
        long hour = (local_total_duration / (60 * 60)) % 24;

        total_duration = String.format("%02dh:%02dm:%02ds", hour, minute, second);

    }

}