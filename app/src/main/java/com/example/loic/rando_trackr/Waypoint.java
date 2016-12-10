package com.example.loic.rando_trackr;

import static android.R.attr.type;

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
    //Constructor
    public Waypoint(int waypointnb, String adresse, String type, Double latitude, Double longitude)
    {
        this.waypointnb=waypointnb;
        this.adresse=adresse;
        this.type=type;
        this.latitude=latitude;
        this.longitude=longitude;
    }
}
