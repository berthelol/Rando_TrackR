package com.example.loic.rando_trackr;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Loïc on 18/09/16.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Pushing MapView Fragment
        Fragment fragment = Fragment.instantiate(this, Accueil.class.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

       try
        {
            SQLiteDatabase database =this.openOrCreateDatabase("RandoTrackR",MODE_PRIVATE,null);

            database.execSQL("CREATE TABLE IF NOT EXISTS user (firstname VARCHAR, lastname VARCHAR, nburgence VARCHAR)");
           // database.execSQL("INSERT INTO user (firstname,lastname,nburgence) VALUES ('Loic','Berthelot','0677823818')");

            /*Cursor c =database.rawQuery("SELECT * FROM user",null);

            int firstnameindex = c.getColumnIndex("firstname");
            int lastnameindex = c.getColumnIndex("lastname");
            int nburgenceindex = c.getColumnIndex("nburgence");

            c.moveToFirst();
            while (c!=null)
            {
                Log.i("firstname:",c.getString(firstnameindex));
                Log.i("lastname:",c.getString(lastnameindex));
                c.moveToNext();
            }*/
        } catch (Exception e)
        {
            e.printStackTrace();
        }
              //add this line to display menu1 when the activity is loaded
       // displaySelectedScreen(R.id.profile);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Accueil/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.deletedb) {
            //clearDB
            SQLiteDatabase randoTrackRDB = openOrCreateDatabase("RandoTrackR",MODE_PRIVATE,null);
            deleteDatabase("RandoTrackR");
            Toast toast = Toast.makeText(getApplicationContext(), "Db deleted", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.accueil:
                fragment = new Accueil();
                break;
            case R.id.meteo:
                fragment = new Meteo();
                break;
            case R.id.parcours:
                fragment = new Parcours();
                break;
            case R.id.historique:
                fragment = new Historique();
                break;
            case R.id.parametres:
                fragment = new Parametres();
                break;
            case R.id.hardcore_mode:
                fragment = new Aventurier();
                break;
            case 0:
                fragment = new Profil();
                break;
            default:
                fragment = new Aventurier();

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void profil_click(View e)
    {
        displaySelectedScreen(0);
    }

    public void onSave (View view)
    {
        Button savebtn = (Button) findViewById(R.id.savebutton);
        savebtn.setBackgroundResource(R.drawable.mysavebutton);
        Toast toast = Toast.makeText(getApplicationContext(), "Modifications saved", Toast.LENGTH_SHORT);
        toast.show();
    }
}
