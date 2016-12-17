package com.example.loic.rando_trackr;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.loic.rando_trackr.Map_data.ParserTask;
import com.example.loic.rando_trackr.Map_data.Value_total;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


/**
 * A fragment that launches other parts of the demo application.
 */
public class Map extends Fragment {

    //Database randotrackR
    SQLiteDatabase randoTrackRDB;

    //List of waypoints
    ArrayList <Waypoint> waypoints;
    //List of lines drawn on the map for the parcours
    Polyline polylines;

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

    //Stored last position
    LatLng lastlocation =null;

    //previous location to calculate the distance the user makes
    private Location prevLocation ;
    //Total distance
    private double total_distance = 0d;

    //Boolean to signal when position is created
    Boolean position_ready;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accueil_map, container, false);

        //Initialize position_ready
        this.position_ready=false;

        //Initialize the randotrackR DB
        this.randoTrackRDB = getContext().openOrCreateDatabase("RandoTrackR",MODE_PRIVATE,null);
        this.randoTrackRDB.execSQL("CREATE TABLE IF NOT EXISTS Waypoint(Waypointnb INTEGER,Adresse VARCHAR,Type VARCHAR,Latitude REAL,Longitude REAL,Distance_text VARCHAR,Distance_value REAL,Duration_text VARCHAR,Duration_value REAL);");
        this.randoTrackRDB.execSQL("CREATE TABLE IF NOT EXISTS Historical_distance_travelled(Date TEXT, Distance REAL);");

        //Initialize the way points array
        waypoints = new ArrayList<Waypoint>();

        //Initialize the map view
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //When the map is ready
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                //refresh_parcours();
                //Create the settings options for the map
                UiSettings uisetting = googleMap.getUiSettings();
                uisetting.setMapToolbarEnabled(false);
                uisetting.setCompassEnabled(true);
                uisetting.setZoomControlsEnabled(true);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        //Add in the bdd a marker
                        randoTrackRDB.execSQL("INSERT INTO Waypoint VALUES("+waypoints.size()+",'','COORD',"+point.latitude+","+point.longitude+",'',0,'',0);");

                        //Adding the marker
                        googleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title(""+(waypoints.size()+1))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        //waypoints.add(new Waypoint(waypoints.size()+1,"","COORD",point.latitude,point.longitude,"",""));
                        //Display on the map
                        if(lastlocation!=null)
                        {
                            refresh_parcours(lastlocation);
                        }
                    }
                });

                /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        Log.i("map clicked","fbdfbdsfdfsdf");
                        googleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("You are here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    }
                });*/
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                try {
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.style_json));

                    if (!success) {
                        Log.e("error", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("error", "Can't find style. Error: ", e);
                }

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
                // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

            }
        });
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get the options textview's
        this.textview_option1 = (TextView) getView().findViewById(R.id.option_1);
        this.textview_option2 = (TextView) getView().findViewById(R.id.option_2);
        this.textview_option3 = (TextView) getView().findViewById(R.id.option_3);
        this.textview_option4 = (TextView) getView().findViewById(R.id.option_4);

        //create the sharedpreferences object to get the user's data
        sharedPreferences = getContext().getSharedPreferences("com.example.loic.rando_trackr", MODE_PRIVATE);

        //get the enum the user parametered
        Quick_info quick_info1 =  Quick_info.valueOf(sharedPreferences.getString("option1","Not_defined"));
        Quick_info quick_info2 =  Quick_info.valueOf(sharedPreferences.getString("option2","Not_defined"));
        Quick_info quick_info3 =  Quick_info.valueOf(sharedPreferences.getString("option3","Not_defined"));
        Quick_info quick_info4 =  Quick_info.valueOf(sharedPreferences.getString("option4","Not_defined"));

        //Set the content of the enum
        this.textview_option1.setText(quick_info1==Quick_info.Not_defined?"":quick_info1.toString());
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
        if(mMapView!=null)
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

            if(!position_ready&&googleMap != null)
            {
                //set the first position
                prevLocation=location;
                //First zoom to your location but false afterwards we don't want to zoom on each mouvement
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.0f));
                //refresh parcours
                refresh_parcours(loc);
            }
            //The first location has been made
            position_ready=true;
            //Set the last position know to the lastloc in case of click on map and add a marker
            lastlocation=loc;

            double distanceToLast = location.distanceTo(prevLocation);
            // if less than 10 metres, do not record
            if (distanceToLast < 10.00) {
            } else
            {
                total_distance += distanceToLast;
                update_distance_historical(distanceToLast);
                prevLocation = location;
                if(googleMap != null)
                {
                    //refresh parcours
                    refresh_parcours(loc);
                    googleMap.addCircle(new CircleOptions().center(loc)
                            .radius(2)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.BLUE));
                }

            }
        }
    };

    private void update_distance_historical(double distance_now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String currentDate = sdf.format(new Date());
        Cursor resultSet;
        try {
            //Fetch the data from DB
            resultSet = randoTrackRDB.rawQuery("Select * from Historical_distance_travelled",null);
            //Get only the last item
            resultSet.moveToLast();
            if(resultSet!=null)
            {
                //Long _id = resultSet.getLong(resultSet.getColumnIndex("_id"));
                String date = resultSet.getString(resultSet.getColumnIndex("Date"));
                int distance = resultSet.getInt(resultSet.getColumnIndex("Distance"));
                int updated_distance =(int)distance_now+distance;
                //Check if distance from today is new or already registered
                if(!date.equals(currentDate))
                {
                    //Create new distance from today
                    this.randoTrackRDB.execSQL("INSERT INTO Historical_distance_travelled VALUES(\'"+currentDate+"\',"+total_distance+");");
                }else
                {
                    //Update current distance
                    randoTrackRDB.execSQL("UPDATE Historical_distance_travelled SET Distance = "+updated_distance+" WHERE Date=\'"+date+"\';");
                }
            }else
            {
                //Create new distance from today
                this.randoTrackRDB.execSQL("INSERT INTO Historical_distance_travelled VALUES(\'"+currentDate+"\',"+total_distance+");");
            }
            resultSet.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void test_if_position_is_at_waypoint(LatLng pos)
    {
        DecimalFormat df1 = new DecimalFormat("#.#####");
        Log.i("Point user","lng: "+df1.format(pos.longitude) + "  lat: "+df1.format(pos.latitude));
        //We check every way point (expect the first one which is our position)
        //We could've check only the next one but we assume the user can use a shortcut
        // to go directly to the n+1
        for(int i=0; i<waypoints.size();i++)
        {
            if(i!=0)
            {
                Log.i("Marker google map","Number "+waypoints.get(i).waypointnb+" lng: "+df1.format(waypoints.get(i).longitude) + " lat: "+df1.format(waypoints.get(i).latitude));
                LatLng waypointlatlng = new LatLng(waypoints.get(i).latitude,waypoints.get(i).longitude);
                if(df1.format(waypointlatlng.latitude).equals(df1.format(pos.latitude))||df1.format(waypointlatlng.longitude).equals(df1.format(pos.longitude)))
                {
                    Toast.makeText(getContext(),"Arrived at step "+waypoints.get(i).waypointnb,Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void refresh_parcours(LatLng myposition) {
        //Clear the arraylist waypoints
        waypoints.clear();
        //Add the first point to be my position
        waypoints.add(new Waypoint(0, "", "COORD", myposition.latitude, myposition.longitude,"0",0,"0",0));
        //Load the ways points from the db
        Cursor resultSet;
        try {
            //Fetch the data from DB
            resultSet = randoTrackRDB.rawQuery("Select * from Waypoint", null);
            while (resultSet.moveToNext()) {
                String adresse = resultSet.getString(resultSet.getColumnIndex("Adresse"));
                String type = resultSet.getString(resultSet.getColumnIndex("Type"));
                Double lat = resultSet.getDouble(resultSet.getColumnIndex("Latitude"));
                Double lng = resultSet.getDouble(resultSet.getColumnIndex("Longitude"));
                int waypointnb = resultSet.getInt(resultSet.getColumnIndex("Waypointnb"));

                //Place markers on the map
                if(lat!=-1&&lng!=-1)
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat,lng))
                            .title("Etape "+waypointnb+" "+adresse)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                }
                //convert special char to ASCII char
                adresse = Normalizer.normalize(adresse, Normalizer.Form.NFD);
                //remove space at the end
                adresse = adresse.replaceAll("\\s+$", "");
                //convert space in string into + for the url and check if no ASCII char left
                adresse = adresse.replaceAll("\\s", "+").replaceAll("[^a-zA-Z0-9-+]+", "");
                waypoints.add(new Waypoint(waypointnb, adresse, type, lat, lng,"",0,"",0));
            }
            resultSet.close();
            if (waypoints.size()!=1) {
                String url = formatDirectionsUrl(waypoints);

                DownloadTask downloadTask = new DownloadTask();

                downloadTask.execute(url);

                try {
                    String result=downloadTask.get();
                    ParserTask parserTask = new ParserTask();
                    // Invokes the thread for parsing the JSON data
                    parserTask.execute(result);

                    try {
                        //Fetch the directiondata from the parser
                        List<List<HashMap<String, String>>> routes = parserTask.get();
                        //Fetch the duration from the parser
                        ArrayList<Value_total> duration = parserTask.getduration();
                        update_durations(duration);
                        //Fetch the distance from the parser
                        ArrayList<Value_total> distance = parserTask.getdistance();
                        update_distances(distance);
                        //Create list of differente points to pass to
                        ArrayList<LatLng> points = null;
                        //Line options
                        PolylineOptions lineOptions = null;
                        if(routes==null)
                        {
                            Toast.makeText(getContext(),"Pas de direction trouvée",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Traversing through all the routes
                        for(int i=0;i<routes.size();i++){
                            points = new ArrayList<LatLng>();
                            lineOptions = new PolylineOptions();

                            // Fetching i-th route
                            List<HashMap<String, String>> path = routes.get(i);

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
                        if(polylines !=null)
                            polylines.remove();
                        // Drawing polyline in the Google Map for the i-th route
                        polylines = googleMap.addPolyline(lineOptions);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    private void update_distances(ArrayList<Value_total> distances) {
        for(int i=0;i<distances.size();i++)
        {
            randoTrackRDB.execSQL("UPDATE Waypoint SET Distance_text = \'"+distances.get(i).getText()+"\' , Distance_value = "+distances.get(i).getValue()+" WHERE Waypointnb="+(i+1)+";");
        }
    }

    private void update_durations(ArrayList<Value_total> durations) {
        for(int i=0;i<durations.size();i++)
        {
            randoTrackRDB.execSQL("UPDATE Waypoint SET Duration_text = \'"+durations.get(i).getText()+"\' , Duration_value = "+durations.get(i).getValue()+" WHERE Waypointnb="+(i+1)+";");
        }
    }

    private String formatDirectionsUrl(ArrayList<Waypoint> mywaypoints){
        // Transport mode
        String mode = "mode=walking";
        //Language
        String language = "language=fr";
        // Origin of route
        String str_origin="";
        if(mywaypoints.get(0).type.equals("COORD"))
        {
            str_origin = "origin="+mywaypoints.get(0).latitude+","+mywaypoints.get(0).longitude;
        }else if(mywaypoints.get(0).type.equals("ADRESSE"))
        {
            str_origin = "origin="+mywaypoints.get(0).adresse;
        }
        // Destination of route (the last marker)
        String str_dest="";
        if(mywaypoints.get(mywaypoints.size()-1).type.equals("COORD"))
        {
            str_dest = "destination="+mywaypoints.get(mywaypoints.size()-1).latitude+","+mywaypoints.get(mywaypoints.size()-1).longitude;
        }else if(mywaypoints.get(mywaypoints.size()-1).type.equals("ADRESSE"))
        {
            str_dest = "destination="+mywaypoints.get(mywaypoints.size()-1).adresse;
        }
        // Waypoints
        List<String> betweenwaypoints = new ArrayList<String>();
        if(mywaypoints.size()>2)
        {
            for (int i=1;i<mywaypoints.size()-1;i++)
            {
                switch (mywaypoints.get(i).type)
                {
                    case "ADRESSE":
                        betweenwaypoints.add(""+mywaypoints.get(i).adresse);
                        break;
                    case "COORD":
                        betweenwaypoints.add(""+mywaypoints.get(i).latitude+","+mywaypoints.get(i).longitude);
                        break;
                    default:
                }
            }
        }

        String _waypoints="waypoints="+TextUtils.join("|", betweenwaypoints);

        //API key
        String APIKEY = getResources().getString(R.string.API_KEY);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+_waypoints+"&"+mode+"&"+language+"&"+APIKEY;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        Log.i("Url",url);
        return url;
    }
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.e("Exception while dl url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}