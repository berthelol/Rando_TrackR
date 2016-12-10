package com.example.loic.rando_trackr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;

import javax.net.ssl.HttpsURLConnection;

import static com.example.loic.rando_trackr.R.string.API_KEY;

/**
 * Created by loic on 10/12/2016.
 */
public class Geocoder extends AsyncTask<String, Void, LatLng> {
    @Override
    protected LatLng doInBackground(String... params) {
        String url_format_postal_adresse=(params[0]);
        Log.i("URL input",url_format_postal_adresse);
        //convert special char to ASCII char
        url_format_postal_adresse = Normalizer.normalize(url_format_postal_adresse, Normalizer.Form.NFD);
        //remove space at the end
        url_format_postal_adresse=url_format_postal_adresse.replaceAll("\\s+$", "");
        //convert space in string into + for the url and check if no ASCII char left
        url_format_postal_adresse=url_format_postal_adresse.replaceAll("\\s","+").replaceAll("[^a-zA-Z0-9-+]+","");
        String response;
        try {
            String url = "http://maps.google.com/maps/api/geocode/json?address="+url_format_postal_adresse;
            Log.i("URL geocoder",url);
            response = getLatLongByURL(url);
            try {
                JSONObject jsonObject = new JSONObject(response);

                double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");
               return new LatLng(lat,lng);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {

        }
       return null;
    }

    @Override
    protected void onPostExecute(LatLng result) {
        super.onPostExecute(result);
    }
    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}