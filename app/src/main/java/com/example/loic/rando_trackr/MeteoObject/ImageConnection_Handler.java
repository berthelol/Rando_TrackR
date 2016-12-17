package com.example.loic.rando_trackr.MeteoObject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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
public class ImageConnection_Handler extends AsyncTask<String, Void, byte[]> {
    // Downloading data in non-ui thread
    @Override
    protected byte[] doInBackground(String... inputs) {
        String result = "";
        URL url = null;
        InputStream in = null;
        HttpURLConnection urlconnection = null;
        ByteArrayOutputStream baos = null;

        Log.i("URL Reiceved", inputs[0]);

        try {
            url = new URL(inputs[0]);
            urlconnection = (HttpURLConnection) url.openConnection();
            in = urlconnection.getInputStream();
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();

            while (in.read(buffer) != -1)
                baos.write(buffer);


        } catch (Throwable t) {
            t.printStackTrace();
        }finally {
            try { in.close(); } catch(Throwable t) {}
            try { urlconnection.disconnect(); } catch(Throwable t) {}
        }

        return baos.toByteArray();
    }


    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(byte[] result) {
        super.onPostExecute(result);
    }
}
