package com.example.loic.rando_trackr;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by loic on 15/12/2016.
 */
//This class is used to perfoms http request from API's like weather,maps,directions,geocoder
public class Connection_Handler extends AsyncTask<String, Void, String> {
    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... inputs) {
        Log.i("Connection Handler URL",inputs[0]);
        String result="";
        URL url  = null;
        HttpURLConnection urlconnection =null;

        Log.i("URL Reiceved", inputs[0]);

        try {
            url = new URL(inputs[0]);
            urlconnection =(HttpURLConnection) url.openConnection();
            InputStream in = urlconnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;

                data = reader.read();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
