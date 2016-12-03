package com.example.loic.rando_trackr;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

/**
 * A fragment that launches other parts of the demo application.
 */
public class Accueil extends Fragment {

    //Map ressources
    MapView mMapView;
    private GoogleMap googleMap;

    //Quick_info ressources
    TextView textview_option1;
    TextView textview_option2;
    TextView textview_option3;
    TextView textview_option4;

    //Sharedpreferences object (Database)
    SharedPreferences sharedPreferences ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);

                } else {
                    // Show rationale and request permission.
                }

               /* GoogleMapOptions options = new GoogleMapOptions();
                options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                        .compassEnabled(true)
                        .rotateGesturesEnabled(false)
                        .tiltGesturesEnabled(false);*/
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                // For dropping a marker at a point on the Map
               // LatLng sydney = new LatLng(-34, 151);
               // googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
               // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Accueil");

        //get the options textview's
        this.textview_option1 = (TextView) getView().findViewById(R.id.option_1);
        this.textview_option2 = (TextView) getView().findViewById(R.id.option_2);
        this.textview_option3 = (TextView) getView().findViewById(R.id.option_3);
        this.textview_option4 = (TextView) getView().findViewById(R.id.option_4);

        //create the sharedpreferences object to get the user's data
        sharedPreferences = getContext().getSharedPreferences("com.example.loic.rando_trackr", Context.MODE_PRIVATE);

        //get the enum the user parametered
        Quick_info quick_info1 =  Quick_info.valueOf(sharedPreferences.getString("option1","Not defined"));
        Quick_info quick_info2 =  Quick_info.valueOf(sharedPreferences.getString("option2","Not defined"));
        Quick_info quick_info3 =  Quick_info.valueOf(sharedPreferences.getString("option3","Not defined"));
        Quick_info quick_info4 =  Quick_info.valueOf(sharedPreferences.getString("option4","Not defined"));

        //Set the content of the enum
        this.textview_option1.setText(quick_info1.toString());
        this.textview_option2.setText(quick_info2.toString());
        this.textview_option3.setText(quick_info3.toString());
        this.textview_option4.setText(quick_info4.toString());


    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

           /* Marker mMarker = googleMap.addMarker(new MarkerOptions()
                    .position(loc)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mypositionmarker)));*/
            if(googleMap != null){
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.0f));
            }
        }
    };
}
