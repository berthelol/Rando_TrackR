package com.example.loic.rando_trackr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loic.rando_trackr.MeteoObject.ImageConnection_Handler;
import com.example.loic.rando_trackr.MeteoObject.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static android.support.v7.widget.AppCompatDrawableManager.get;
import static com.example.loic.rando_trackr.R.id.imageView;
import static com.example.loic.rando_trackr.R.id.layoutA;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Loïc on 18/09/16.
 */

public class Meteo extends Fragment {

    private EditText cityName;
    private TextView txt_temp_h1, txt_temp_h2, txt_temp_h3, txt_temp_h4,
                    txt_temp_j1, txt_temp_j2, txt_temp_j3, txt_temp_j1A, txt_temp_j2A, txt_temp_j3A, txt_city, txt_cityA,
                    txt_h1, txt_h2, txt_h3, txt_h4, txt_j1, txt_j2, txt_j3, txt_j1A, txt_j2A, txt_j3A;

    private ImageView img_h1, img_h2, img_h3, img_h4,
                    img_j1, img_j2, img_j3, img_j1A, img_j2A, img_j3A;
    private ImageButton btn;

    private float latD, latA, lonD, lonA;
    private Weather weatherh1, weatherh2, weatherh3, weatherh4, weatherj1, weatherj2, weatherj3, weatherj1A, weatherj2A, weatherj3A;
    private LinearLayout layoutA, layoutIF;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_meteo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Meteo");

        // We initialize to 0 each value to know that there is no arrival point
        latA = 0;
        lonA = 0;

        ///////////        LOIC        --------------------------------->
        // Then we get the current position and arrival position from the app
        latD = Float.parseFloat(""+48.85);
        lonD = Float.parseFloat(""+2.35);


        // TextView where we print the temperatures
        // with ...h for nexts hours
        txt_temp_h1 = (TextView) getView().findViewById(R.id.txt_temp_h1);
        txt_temp_h2 = (TextView) getView().findViewById(R.id.txt_temp_h2);
        txt_temp_h3 = (TextView) getView().findViewById(R.id.txt_temp_h3);
        txt_temp_h4 = (TextView) getView().findViewById(R.id.txt_temp_h4);

        // with ...j for next days
        txt_temp_j1 = (TextView) getView().findViewById(R.id.txt_temp_j1);
        txt_temp_j2 = (TextView) getView().findViewById(R.id.txt_temp_j2);
        txt_temp_j3 = (TextView) getView().findViewById(R.id.txt_temp_j3);

        txt_temp_j1A = (TextView) getView().findViewById(R.id.txt_temp_j1A);
        txt_temp_j2A = (TextView) getView().findViewById(R.id.txt_temp_j2A);
        txt_temp_j3A = (TextView) getView().findViewById(R.id.txt_temp_j3A);

        txt_h1 = (TextView) getView().findViewById(R.id.txt_h1);
        txt_h2 = (TextView) getView().findViewById(R.id.txt_h2);
        txt_h3 = (TextView) getView().findViewById(R.id.txt_h3);
        txt_h4 = (TextView) getView().findViewById(R.id.txt_h4);

        txt_j1 = (TextView) getView().findViewById(R.id.txt_j1);
        txt_j2 = (TextView) getView().findViewById(R.id.txt_j2);
        txt_j3 = (TextView) getView().findViewById(R.id.txt_j3);

        txt_j1A = (TextView) getView().findViewById(R.id.txt_j1A);
        txt_j2A = (TextView) getView().findViewById(R.id.txt_J2A);
        txt_j3A = (TextView) getView().findViewById(R.id.txt_J3A);

        txt_city = (TextView) getView().findViewById(R.id.txt_city);
        txt_cityA = (TextView) getView().findViewById(R.id.txt_cityA);

        // ImageView for the meteo

        img_h1 = (ImageView) getView().findViewById(R.id.image_h1);
        img_h2 = (ImageView) getView().findViewById(R.id.image_h2);
        img_h3 = (ImageView) getView().findViewById(R.id.image_h3);
        img_h4 = (ImageView) getView().findViewById(R.id.image_h4);

        img_j1 = (ImageView) getView().findViewById(R.id.image_j1);
        img_j2 = (ImageView) getView().findViewById(R.id.image_j2);
        img_j3 = (ImageView) getView().findViewById(R.id.image_j3);

        img_j1A = (ImageView) getView().findViewById(R.id.image_j1A);
        img_j2A = (ImageView) getView().findViewById(R.id.image_j2A);
        img_j3A = (ImageView) getView().findViewById(R.id.image_j3A);


        layoutA = (LinearLayout) getView().findViewById(R.id.layoutA);
        layoutIF = (LinearLayout) getView().findViewById(R.id.layoutIF);

        // We initialize meteo datas
        weatherUpdate();
        if(lonA != 0 || latA != 0){
            // Get the meteo of arrival point
            weatherUpdateArrival();

            // We print the meteo of arrival point
            layoutA.setVisibility(View.VISIBLE);

            // We desable the message
            layoutIF.setVisibility(View.INVISIBLE);

        }
        else
        {
            // Desable view of the meteo of arrival point
            layoutA.setVisibility(View.INVISIBLE);
            // Active the information message
            layoutIF.setVisibility(View.VISIBLE);
        }
    }

    // http://www.survivingwithandroid.com/2013/05/build-weather-app-json-http-android.html
    // http://openweathermap.org/api
    // http://www.todroid.com/creating-a-weather-app-in-android-studio/
    //
    //
    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private String getDate(String s){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get((Calendar.DAY_OF_MONTH));
        int h = c.get((Calendar.HOUR_OF_DAY));
        int hCall, hCallh2, hCallh3, hCallh4, hCallj1, hCallj2, hCallj3;
        int dCallh1, dCallh2, dCallh3, dCallh4, dCallj1, dCallj2, dCallj3;

        Log.i("Date --> ", year+" "+month+" "+day+" "+h);

        DecimalFormat formatH = new DecimalFormat("00");

        // Check the current time to ask for the next meteo
        // The meteo is updated every 3 hours, so we compare to be able to set a good String to
        // compare it from the response's one

        if (h >= 3) {
            if (h >= 6) {
                if (h >= 9) {
                    if (h >= 12) {
                        if (h >= 15) {
                            if (h >= 18) {
                                if (h >= 21) {
                                    hCall = 24;
                                    hCallh2 = 27;
                                    hCallh3 = 30;
                                    hCallh4 = 33;
                                } else {
                                    hCall = 21;
                                    hCallh2 = 24;
                                    hCallh3 = 27;
                                    hCallh4 = 30;
                                }
                            } else {
                                hCall = 18;
                                hCallh2 = 21;
                                hCallh3 = 24;
                                hCallh4 = 27;
                            }
                        } else {
                            hCall = 15;
                            hCallh2 = 18;
                            hCallh3 = 21;
                            hCallh4 = 24;
                        }
                    } else {
                        hCall = 12;
                        hCallh2 = 15;
                        hCallh3 = 18;
                        hCallh4 = 21;
                    }
                } else {
                    hCall = 9;
                    hCallh2 = 12;
                    hCallh3 = 15;
                    hCallh4 = 18;
                }
            } else {
                hCall = 6;
                hCallh2 = 9;
                hCallh3 = 12;
                hCallh4 = 15;
            }
        } else {
            hCall = 3;
            hCallh2 = 6;
            hCallh3 = 9;
            hCallh4 = 12;
        }

        // We select witch case we are to add X hours to the current one to ask for that time meteo
        if (s.equals("h2"))
        {
            hCall += 3;
        }
        if (s.equals("h3"))
        {
            hCall += 6;
        }
        if (s.equals("h4"))
        {
            hCall += 9;
        }
        if (s.equals("j1"))
        {
            hCall += 24;
        }
        if (s.equals("j2"))
        {
            hCall += 48;
        }
        if (s.equals("j3"))
        {
            hCall += 72;
        }


        while (hCall >= 24) {
            // if next hour is 24, we need to change the date and check for the end of month and year
            // we exclude bisexctile years
            if (hCall >= 24) {
                day++;
                hCall -= 24;

                // check the end of month
                if (month == 2) { // February has 28 days
                    if (day == 29) {
                        month++;
                        day = 1;
                    }
                } else if (month == 4 || month == 6 || month == 9 || month == 11) // April, June, September, and november has 30 days
                {
                    if (day == 31) {
                        month++;
                        day = 1;
                    }
                } else {
                    if (day == 32) {
                        month++;
                        day = 1;
                        // If we are the 31st of december, we change the year in less than 3 hours
                        if (month == 13) {
                            year++;
                            month = 1;
                        }
                    }
                }
            }
        }

        // Now we are ready to create the string
        // Example from response : "2016-12-15 15:00:00"
        return year+"-"+month+"-"+day+" "+formatH.format(hCall)+":00:00";


    }

    private void weatherUpdate(){
        //Weather web site API
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/?";
        //Weather web site API KEY
        final String API_KEY = getResources().getString(R.string.weather_API_KEY);

        DecimalFormat df = new DecimalFormat("###.#");

        // Get the new weather
        Connection_Handler ask_for_weather = new Connection_Handler();
        //Execute the request
        ask_for_weather.execute(BASE_URL+"lat="+ latD +"&lon="+ lonD +"&APPID="+API_KEY);

        String response= null;
        try {
            //Wait for the data to be uploaded (asynchrone)
            response = ask_for_weather.get();

            Log.i("Weather Content",response);
            //Extract weather using the response of the API
            // Initializing all the weathers objects
            extractWeatherFromResponse(new JSONObject(response));

            //Update the view
            // Dates sous la forme : 2016-12-18 06:00:00
            // Extract hour from the date string
            // And the temperature
            if(weatherh1 != null)
            {
                txt_h1.setText(weatherh1.getDate().substring(11, 16));
                txt_temp_h1.setText(""+(df.format(weatherh1.getTemp()))+" °C");
            }
            if(weatherh2 != null)
            {
                txt_h2.setText(weatherh2.getDate().substring(11, 16));
                txt_temp_h2.setText(""+(df.format(weatherh2.getTemp()))+" °C");
            }
            if(weatherh3 != null)
            {
                txt_h3.setText(weatherh3.getDate().substring(11, 16));
                txt_temp_h3.setText(""+(df.format(weatherh3.getTemp()))+" °C");
            }
            if(weatherh4 != null)
            {
                txt_h4.setText(weatherh4.getDate().substring(11, 16));
                txt_temp_h4.setText(""+(df.format(weatherh4.getTemp()))+" °C");
            }
            // Extract day from the date string
            if(weatherj1 != null) {
                txt_j1.setText(weatherj1.getDate().substring(5, 10));
                txt_temp_j1.setText(""+(df.format(weatherj1.getTemp()))+" °C");
            }
            if(weatherj2 != null)
            {
                txt_j2.setText(weatherj2.getDate().substring(5, 10));
                txt_temp_j2.setText(""+(df.format(weatherj2.getTemp()))+" °C");
            }
            if(weatherj3 != null)
            {
                txt_j3.setText(weatherj3.getDate().substring(5, 10));
                txt_temp_j3.setText(""+(df.format(weatherj3.getTemp()))+" °C");
            }

            Log.i("Meteo info J1", df.format(weatherj1.getTemp()));
            Log.i("Meteo info J2", df.format(weatherj2.getTemp()));
            Log.i("Meteo info J3", df.format(weatherj3.getTemp()));

            txt_city.setText(weatherh1.getCity());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getImages();
    }

    private void weatherUpdateArrival(){
        //Weather web site API
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/?";
        //Weather web site API KEY
        final String API_KEY = getResources().getString(R.string.weather_API_KEY);
        // Get the new weather
        Connection_Handler ask_for_weather = new Connection_Handler();
        //Execute the request
        ask_for_weather.execute(BASE_URL+"lat="+ latA +"&lon="+ lonA +"&APPID="+API_KEY);


        String response= null;
        try {
            //Wait for the data to be uploaded (asynchrone)
            response = ask_for_weather.get();

            Log.i("Weather Content",response);
            //Extract weather using the response of the API
            // Initializing all the weathers objects
            extractWeatherFromResponseA(new JSONObject(response));

            //Update the view
            // Extract day from the date string
            if(weatherj1A != null) txt_j1A.setText(weatherj1A.getDate().substring(5, 10));
            if(weatherj2A != null) txt_j2A.setText(weatherj2A.getDate().substring(5, 10));
            if(weatherj3A != null) txt_j3A.setText(weatherj3A.getDate().substring(5, 10));

            // Temperatures
            DecimalFormat df = new DecimalFormat("###.#");
            if(weatherj1A != null) txt_temp_j1A.setText(""+(df.format(weatherj1A.getTemp()))+" °C");
            if(weatherj2A != null) txt_temp_j2A.setText(""+(df.format(weatherj2A.getTemp()))+" °C");
            if(weatherj3A != null) txt_temp_j3A.setText(""+(df.format(weatherj3A.getTemp()))+" °C");

            if(weatherj1A != null) txt_cityA.setText(weatherj1A.getCity());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getImagesA();
    }

    public byte[] imageDownload(String s)
    {
        String result = "";
        URL url = null;
        InputStream in = null;
        HttpURLConnection urlconnection = null;
        ByteArrayOutputStream baos = null;

        Log.i("URL Reiceved", s);

        try {
            url = new URL(s);
            urlconnection = (HttpURLConnection) url.openConnection();
            in = urlconnection.getInputStream();
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();

            while (in.read(buffer) != -1)
                baos.write(buffer);
            return baos.toByteArray();

        } catch (Throwable t) {
            t.printStackTrace();
        }finally {
            try { in.close(); } catch(Throwable t) {}
            try { urlconnection.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }

    public void getImagesFromUrl(){
        ImageConnection_Handler ch1 = new ImageConnection_Handler();
        ImageConnection_Handler ch2 = new ImageConnection_Handler();
        ImageConnection_Handler ch3 = new ImageConnection_Handler();
        ImageConnection_Handler ch4 = new ImageConnection_Handler();
        ImageConnection_Handler cj1 = new ImageConnection_Handler();
        ImageConnection_Handler cj2 = new ImageConnection_Handler();
        ImageConnection_Handler cj3 = new ImageConnection_Handler();

        String url = getResources().getString(R.string.weather_icon_url);

        // METHODE 1 POUR CHARGER LES IMAGES : BOF BOF

        byte[] img;

        if (weatherh1 != null) {
            img = imageDownload(url + weatherh1.getIcon() + ".png");
            if (img != null) img_h1.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            else Log.e("Erreur chargement image", "H1");
        }
        if (weatherh2 != null) {
            img = imageDownload(url + weatherh2.getIcon() + ".png");
            if (img != null) img_h2.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            else Log.e("Erreur chargement image", "H2");
        }
        if (weatherh3 != null) {
            img = imageDownload(url + weatherh3.getIcon() + ".png");
            if (img != null) img_h3.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            else Log.e("Erreur chargement image", "H3");
        }
        if (weatherh4 != null) {
            img = imageDownload(url + weatherh4.getIcon() + ".png");
            if (img != null) img_h4.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            else Log.e("Erreur chargement image", "H4");
        }
        if (weatherj1 != null) {
            img = imageDownload(url + weatherj1.getIcon() + ".png");
            if (img != null) img_j1.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            else Log.e("Erreur chargement image", "J1");
        }
        if (weatherj2 != null) {
            img = imageDownload(url + weatherj2.getIcon() + ".png");
            if (img != null) img_j2.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            else Log.e("Erreur chargement image", "J2");
        }
        if (weatherj3 != null) {
            img = imageDownload(url + weatherj3.getIcon() + ".png");
            if (img != null) img_j3.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            else Log.e("Erreur chargement image", "J3");
        }

        // METHOD 2 : En background, mais pas beaucoup mieux
/*
        try {
            // Image h1
            ch1.execute(url+weatherh1.getIcon()+".png");
            byte[] img =  ch1.get();
            if (img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                img_h1.setImageBitmap(bm);
            }else {
                Log.e("ImageConvert", "Error conversion image from byte[] to bitmap");
            }

           // while(ch1.getStatus() != ImageConnection_Handler.Status.FINISHED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            // Image h2
            ch2.execute(url+weatherh2.getIcon()+".png");
            byte[] img =  ch2.get();
            if(img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                img_h2.setImageBitmap(bm);
            }
            else {
                Log.e("ImageConvert", "Error conversion image from byte[] to bitmap");
            }
         //   while(ch2.getStatus() != ImageConnection_Handler.Status.FINISHED);
            Log.i("Image h1 chargement", "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            // Image h3
            ch3.execute(url+weatherh3.getIcon()+".png");
            byte[] img =  ch3.get();
            if (img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                img_h3.setImageBitmap(bm);
            }else {
                Log.e("ImageConvert", "Error conversion image from byte[] to bitmap");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            // Image h4
            ch4.execute(url+weatherh4.getIcon()+".png");
            byte[] img =  ch4.get();
            if (img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                img_h4.setImageBitmap(bm);
            }else {
                Log.e("ImageConvert", "Error conversion image from byte[] to bitmap");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            // Image j1
            cj1.execute(url+weatherj1.getIcon()+".png");
            byte[] img =  cj1.get();
            if (img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                img_j1.setImageBitmap(bm);
            }else {
                Log.e("ImageConvert", "Error conversion image from byte[] to bitmap");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            // Image j2
            cj2.execute(url+weatherj2.getIcon()+".png");
            byte[] img =  cj2.get();
            if (img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                img_j2.setImageBitmap(bm);
            }else {
                Log.e("ImageConvert", "Error conversion image from byte[] to bitmap");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {

            // Image j3
            cj3.execute(url+weatherj3.getIcon()+".png");
            byte[] img =  cj3.get();
            if (img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                img_j3.setImageBitmap(bm);
            }else {
                Log.e("ImageConvert", "Error conversion image from byte[] to bitmap");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        */
    }


    public void getImagesA() {
        String fnm;
        int imgId;
        if (weatherj1A != null) {
            fnm = "icon" + weatherj1A.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);

            img_j1A.setImageBitmap(BitmapFactory.decodeResource(getResources(), imgId));

        }
        if (weatherj2A != null) {
            fnm = "icon" + weatherj2A.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);

            img_j2A.setImageBitmap(BitmapFactory.decodeResource(getResources(), imgId));

        }
        if (weatherj3A != null) {
            fnm = "icon" + weatherj3A.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);

            img_j3A.setImageBitmap(BitmapFactory.decodeResource(getResources(), imgId));

        }
    }

    public void getImages(){
        String fnm;
        int imgId;

        if (weatherh1 != null) {
            fnm = "icon" + weatherh1.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);

            img_h1.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));
        }
        if (weatherh2 != null) {
            fnm = "icon" + weatherh2.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);

            img_h2.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        }
        if (weatherh3 != null) {
            fnm = "icon" + weatherh3.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);

            img_h3.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        }
        if (weatherh4 != null) {
            fnm = "icon" + weatherh4.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);

            img_h4.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        }
        if (weatherj1 != null) {
            fnm = "icon" + weatherj1.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);

            img_j1.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        }
        if (weatherj2 != null) {
            fnm = "icon" + weatherj2.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);

            img_j2.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        }
        if (weatherj3 != null) {
            fnm = "icon" + weatherj3.getIcon(); //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);

            img_j3.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        }

    }

    private void extractWeatherFromResponse(JSONObject jObj) {

        String datej1 = getDate("j1");
        String datej2 = getDate("j2");
        String datej3 = getDate("j3");
        String dateh1 = getDate("h1");
        String dateh2 = getDate("h2");
        String dateh3 = getDate("h3");
        String dateh4 = getDate("h4");

        Log.i("Date H1", dateh1);
        Log.i("Date H2", dateh2);
        Log.i("Date H3", dateh3);
        Log.i("Date H4", dateh4);
        Log.i("Date J1", datej1);
        Log.i("Date J2", datej2);
        Log.i("Date J3", datej3);


        try {
            // We extract the location (first data in JSON response)
            JSONObject cityObj = getObject("city", jObj);
            JSONObject coordObj = getObject("coord", cityObj);
            float lat = getFloat("lat", coordObj);
            float lon = getFloat("lon", coordObj);
            String city = getString("name", cityObj);
            String date = null;

            /*
            String response = task.get();
            JSONArray json = new JSONArray(response);
            Log.i("Length",String.valueOf(json.length()));
            for(int i=0;i<json.length();i++)
            {
                Log.i("data",String.valueOf(json.getJSONObject(i).getInt("mesure")));
                values.add(new Entry(i,json.getJSONObject(i).getInt("mesure")));
            }
             */

            // Go the get the date from the list to compare it with the one we are looking for
            // Save the date code for each to be able to send it to the function that will create the weather
            // or erase
            int n = 0;
            int close = 0;
            JSONObject childFromListObj, mainObj;
            JSONArray listObj = jObj.optJSONArray("list");
            //for(int i = 0; i<listObj.length(); i++)
            while(close != 8)
            {
                // Go through the list until we find the date we want
                childFromListObj = listObj.getJSONObject(n);
                date = childFromListObj.getString("dt_txt");

                Log.i("Date extraite", date);

                // check the dates
                if (date.equals(datej1))
                {
                    this.weatherj1 = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(datej2))
                {
                    this.weatherj2 = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(datej3))
                {
                    this.weatherj3 = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(dateh1))
                {
                    this.weatherh1 = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(dateh2))
                {
                    this.weatherh2 = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(dateh3))
                {
                    this.weatherh3 = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(dateh4))
                {
                    this.weatherh4 = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }

                n++;

                Log.i("Number of meteos found", ""+close);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
       On récupère un objet JSON extrait précédement d'un url, et on extrait les données de l'objet
       JSON pour les placer dans une classe Weather et ensuite les utiliser
    */
    private Weather createWeather(JSONObject jObj, float lat, float lon, String city) {

        // We start extracting the info
        Weather weather = new Weather();

        try {
            weather.setLat(lat);
            weather.setLon(lon);
            weather.setCity(city);

            // We get weather info (This is an array)
            JSONArray jArr = jObj.getJSONArray("weather");

            // We use only the first value
            JSONObject JSONWeather = jArr.getJSONObject(0);
            weather.setDescr(getString("description", JSONWeather));
            weather.setCondition(getString("main", JSONWeather));
            weather.setIcon(getString("icon", JSONWeather));

            JSONObject mainObj = getObject("main", jObj);
            weather.setTemp(getFloat("temp", mainObj));

            // Clouds
            JSONObject cObj = getObject("clouds", jObj);
            weather.setClouds(getInt("all", cObj));

            // Date
            weather.setDate(getString("dt_txt", jObj));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();

            return weather;
        }

    }

    private void extractWeatherFromResponseA(JSONObject jObj) {

        String datej1 = getDate("j1");
        String datej2 = getDate("j2");
        String datej3 = getDate("j3");

        try {
            // We extract the location (first data in JSON response)
            JSONObject cityObj = getObject("city", jObj);
            JSONObject coordObj = getObject("coord", cityObj);
            float lat = getFloat("lat", coordObj);
            float lon = getFloat("lon", coordObj);
            String city = getString("name", cityObj);
            String date = null;

            /*
            String response = task.get();
            JSONArray json = new JSONArray(response);
            Log.i("Length",String.valueOf(json.length()));
            for(int i=0;i<json.length();i++)
            {
                Log.i("data",String.valueOf(json.getJSONObject(i).getInt("mesure")));
                values.add(new Entry(i,json.getJSONObject(i).getInt("mesure")));
            }
             */

            // Go the get the date from the list to compare it with the one we are looking for
            // Save the date code for each to be able to send it to the function that will create the weather
            // or erase
            int n = 0;
            int close = 0;
            JSONObject childFromListObj, mainObj;
            JSONArray listObj = jObj.optJSONArray("list");
            //for(int i = 0; i<listObj.length(); i++)
            while(close != 8)
            {
                // Go through the list until we find the date we want
                childFromListObj = listObj.getJSONObject(n);
                date = childFromListObj.getString("dt_txt");

                Log.i("Date extraite", date);

                // check the dates
                if (date.equals(datej1))
                {
                    this.weatherj1A = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(datej2))
                {
                    this.weatherj2A = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }
                if (date.equals(datej3))
                {
                    this.weatherj3A = createWeather(childFromListObj, lat, lon, city);

                    close ++;
                }

                n++;

                Log.i("Number of meteos found", ""+close);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}