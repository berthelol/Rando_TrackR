package com.example.loic.rando_trackr.User;

/**
 * Created by loic on 01/12/2016.
 */

public enum Quick_info {

    Position("Position",0),
    Altitude("Altitude",1),
    Temps_prochaine_etape("Temps prochaine étape",2),
    Vitesse("Vitesse",3),
    Temps_restant("Temps restant",4),
    Kcal_depensees("Kcal dépensées",5),
    Distance_restante("Distance restante",6),
    Distance_parcouru("Distance parcouru",7),
    Distance_prochaine_etape("Distance prochaine étape",8),
    Not_defined("Not Defined",9);

    //the name that will be displayed in the spinner and in the home view
    private String label;
    //index in the spinner
    private int index;
    //constructor
    Quick_info(String label, int index){
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

