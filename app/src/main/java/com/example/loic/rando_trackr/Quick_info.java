package com.example.loic.rando_trackr;

/**
 * Created by loic on 01/12/2016.
 */

public enum Quick_info {

    Position("Position",0),
    Altitude("Altitude",1),
    Tempstotal("Temps total",2),
    Temps_prochaine_etape("Temps prochaine étape",3),
    Vitesse("Vitesse",4),
    Temps_restant("Temps restant",5),
    Temperature_actuel("Temperature actuel",6),
    Temperature_destination("Temperature destination",7),
    Kcal_depensees("Kcal dépensées",8),
    Distance_restante("Distance restante",9),
    Distance_parcouru("Distance parcouru",10),
    Distance_prochaine_etape("Distance prochaine étape",11);

    //the name that will be displayed in the spinner and in the home view
    private String label;
    //index in the spinner
    private int index;
    //constructor
    private Quick_info(String label, int index){
        this.label = label;
        this.index =index;
    }
    //override the enum to return the label
    @Override public String toString(){
        return label;
    }
    //return the index of the enum
    public int getindex()
    {
        return this.index;
    }

}

