package com.example.loic.rando_trackr.MeteoObject;

/**
 * Created by mathieuchebassier on 08/12/2016.
 */

public class Weather {
    private String date;
    private float lon;
    private float lat;
    private String city;
    private String descr;
    private String condition;
    private String icon;
    private double temp;
    private int clouds;

    public Weather()
    {

    }

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String d)
    {
        this.date =d;
    }

    public String getCondition()
    {
        return this.condition;
    }

    public String getIcon(){
        return this.icon;
    }

    public void setIcon(String s)
    {
        this.icon = s;
    }

    public void setCondition(String s)
    {
        this.condition = s;
    }

    public int getClouds()
    {
        return this.clouds;
    }

    public void setClouds(int c)
    {
        this.clouds = c;
    }

    public String getCity()
    {
        return this.city;
    }

    public float getLon()
    {
        return this.lon;
    }

    public float getLat()
    {
        return this.lat;
    }

    public void setCity(String c)
    {
        this.city = c;
    }

    public void setLon(float l){
        this.lon = l;
    }

    public void setLat(float l){
        this.lat = l;
    }

    public String getDescr()
    {
        return this.descr;
    }

    public void setDescr(String d)
    {
        this.descr = d;
    }

    public double getTemp()
    {
        return this.temp;
    }

    public void setTemp(double t)
    {
        this.temp = t;
    }

}
