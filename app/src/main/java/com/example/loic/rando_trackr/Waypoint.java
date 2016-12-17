package com.example.loic.rando_trackr;

import static android.R.attr.type;
import static com.google.android.gms.fitness.data.zzr.St;

/**
 * Created by loic on 09/12/2016.
 */

public class Waypoint {
    //Step number
    public int waypointnb;
    //Postal adresse
    public String adresse;
    //coordinates
    public Double latitude;
    public Double longitude;
    //The type of data ei: COORD | ADRESSE
    public String type;
    //Distance from previous point in text
    public String distance_from_text;
    //Distance from previous point in value
    public int distance_from_value;
    //Time from previous point in text
    public String duration_from_text;
    //Time from previous point in value
    public int duration_from_value;
    //Constructor
    public Waypoint(int waypointnb, String adresse, String type, Double latitude, Double longitude,String distance_text,int distance_value,String duration_text,int duration_value)
    {
        this.waypointnb=waypointnb;
        this.adresse=adresse;
        this.type=type;
        this.latitude=latitude;
        this.longitude=longitude;
        this.distance_from_text=distance_text;
        this.distance_from_value=distance_value;
        this.duration_from_text=duration_text;
        this.duration_from_value=duration_value;
    }
}
