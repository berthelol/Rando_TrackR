package com.example.loic.rando_trackr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static android.support.v7.widget.AppCompatDrawableManager.get;


/**
 * Created by Loïc on 18/09/16.
 */

public class Meteo extends Fragment {

    private EditText cityName;
    private TextView txt_temp_h1, txt_temp_h2, txt_temp_h3, txt_temp_h4,
                    txt_temp_j1, txt_temp_j2, txt_temp_j3, txt_temp_j4, txt_city;
    private ImageView img_h1, img_h2, img_h3, img_h4,
                    img_j1, img_j2, img_j3, img_j4;
    private ImageButton btn;

    private Weather weatherh1, weatherh2, weatherh3, weatherh4, weatherj1, weatherj2, weatherj3, weatherj4;

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
        txt_temp_j4 = (TextView) getView().findViewById(R.id.txt_temp_j4);

        txt_city = (TextView) getView().findViewById(R.id.txt_city);

        // ImageView for the meteo

        img_h1 = (ImageView) getView().findViewById(R.id.image_h1);
        img_h2 = (ImageView) getView().findViewById(R.id.image_h2);
        img_h3 = (ImageView) getView().findViewById(R.id.image_h3);
        img_h4 = (ImageView) getView().findViewById(R.id.image_h4);

        img_j1 = (ImageView) getView().findViewById(R.id.image_j1);
        img_j2 = (ImageView) getView().findViewById(R.id.image_j2);
        img_j3 = (ImageView) getView().findViewById(R.id.image_j3);
        img_j4 = (ImageView) getView().findViewById(R.id.image_j4);

        // Image button to synchronize
        btn = (ImageButton) getView().findViewById(R.id.btn_sync);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherUpdate();
            }
        });

        // We initialize meteo datas
        weatherUpdate();
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

    private String getDate(String s)
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get((Calendar.DAY_OF_MONTH));
        int h = c.get((Calendar.HOUR));
        int hCall, hCallh2, hCallh3, hCallh4, hCallj1, hCallj2, hCallj3, hCallj4;
        int dCallh1, dCallh2, dCallh3, dCallh4, dCallj1, dCallj2, dCallj3, dCallj4;

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
        if (s.equals("j4"))
        {
            hCall += 96;
        }


        while (hCall >= 24) {
            // if next hour is 24, we need to change the date and check for the end of month and year
            // we exclude bisexctile years
            if (hCall > 24) {
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
        return year+"-"+month+"-"+day+" "+hCall+":00:00";


    }
    private void weatherUpdate()
    {
        //Weather web site API
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/?";
        //Weather web site API KEY
        final String API_KEY = getResources().getString(R.string.weather_API_KEY);
        // Get the new weather
        Connection_Handler ask_for_weather = new Connection_Handler();
        //Execute the request
        ask_for_weather.execute(BASE_URL+"lat=35&lon=139"+"&APPID="+API_KEY);

        String response= null;
        try {
            //Wait for the data to be uploaded (asynchrone)
            response = ask_for_weather.get();
            Log.i("Weather Content",response);
            //Extract weather using the response of the API
            extractWeatherFromResponse(new JSONObject(response));

            //Update the view
            txt_temp_h1.setText(Double.toString(weatherh1.getTemp()));
            txt_temp_h2.setText(Double.toString(weatherh2.getTemp()));
            txt_temp_h3.setText(Double.toString(weatherh3.getTemp()));
            txt_temp_h4.setText(Double.toString(weatherh4.getTemp()));
            txt_temp_j1.setText(Double.toString(weatherj1.getTemp()));
            txt_temp_j2.setText(Double.toString(weatherj2.getTemp()));
            txt_temp_j3.setText(Double.toString(weatherj3.getTemp()));
            txt_temp_j4.setText(Double.toString(weatherj4.getTemp()));

            txt_city.setText(weatherh1.getCity());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public class WeatherHttpClient { //http://api.openweathermap.org/data/2.5/forecast/?lat=35&lon=139&APPID=9b733320ec0639446758235978d2bdab

        private String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/?";
        private String IMG_URL = "http://openweathermap.org/img/w/";
        private String APPID = "&APPID=9b733320ec0639446758235978d2bdab"; // id to be allowed to get the meteo data from OpenWeatherMap

        public String getWeatherData(String location) {
            HttpURLConnection con = null ;
            InputStream is = null;

            try {
                URL url = new URL(BASE_URL + location + APPID);
                //con.setRequestMethod("GET");
               // con.setDoInput(true);
               // con.setDoOutput(true);
                con = (HttpURLConnection) url.openConnection();
                con.connect();


                // Reading data from url
                is = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                    sb.append(line);
                }

                br.close();
                return sb.toString();



                //MATT
                // Let's read the response
                StringBuffer buffer = new StringBuffer();
                is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ( (line = br.readLine()) != null )
                    buffer.append(line + "rn");

                is.close();
                con.disconnect();
                return buffer.toString();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            finally {
                try { is.close(); } catch(Throwable t) {}
                try { con.disconnect(); } catch(Throwable t) {}
            }

            return null;

        }

        public byte[] getImage(String code) {
            HttpURLConnection con = null ;
            InputStream is = null;
            try {
                con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                // Let's read the response
                is = con.getInputStream();
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(buffer) != -1)
                    baos.write(buffer);

                return baos.toByteArray();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            finally {
                try { is.close(); } catch(Throwable t) {}
                try { con.disconnect(); } catch(Throwable t) {}
            }

            return null;

        }
    }*/

    // EXAMPLE OF JSON RESPONSE FROM OPENWEATHERMAP
    /*
    {"cod":"200","message":0.0048,"city":
        {"id":1851632,"name":"Shuzenji","coord":
            {"lon":138.933334,"lat":34.966671}
        ,"country":"JP","population":0}
    ,"cnt":41,"list":[
        {"dt":1481814000
        ,"main":
            {"temp":284.14,"temp_min":284.135,"temp_max":284.14,"pressure":1014.61,"sea_level":1024.26,"grnd_level":1014.61,"humidity":100,"temp_kf":0}
        ,"weather":[
            {"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}
        ]
        ,"clouds":
            {"all":92}
        ,"wind":
            {"speed":0.84,"deg":54.5002}
        ,"sys":
            {"pod":"n"}
        ,"dt_txt":"2016-12-15 15:00:00"}
     */

    private void extractWeatherFromResponse(JSONObject jObj) {

        String datej1 = getDate("j1");
        String datej2 = getDate("j2");
        String datej3 = getDate("j3");
        String datej4 = getDate("j4");
        String dateh1 = getDate("h1");
        String dateh2 = getDate("h2");
        String dateh3 = getDate("h3");
        String dateh4 = getDate("h4");

        try {
            // We extract the location (first data in JSON response)
            JSONObject cityObj = getObject("city", jObj);
            JSONObject coordObj = getObject("coord", cityObj);
            float lat = getFloat("lat", coordObj);
            float lon = getFloat("lon", coordObj);
            String city = getString("name", cityObj);
            String date = null;

            // Go the get the date from the list to compare it with the one we are looking for
            // Save the date code for each to be able to send it to the function that will create the weather
            // or erase
            int n = 0;
            int close = 0;
            JSONObject childFromListObj, mainObj;
            JSONArray listObj = jObj.optJSONArray("list");
            while(close < 1) {
                // Go through the list until we find the date we want
                childFromListObj = listObj.getJSONObject(n);
                date = getString("dt_txt", childFromListObj);

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
                if (date.equals(datej4))
                {
                    this.weatherj4 = createWeather(childFromListObj, lat, lon, city);

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

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();

            return weather;
        }

    }
}