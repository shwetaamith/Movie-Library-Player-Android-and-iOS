package edu.asu.msse.smurthy3.assign9test;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright (c) 2016 Shweta Murthy,
 * You may not use this file except for self-evaluation and practice
 * This file is allowed to be used for grading puroposes
 * through the spring semester 2016, ASU, by  the grader, TA and the instructor
 * Unless agreed to in writing, this material can is to be
 * distributed on an "AS IS" BASIS
 *
 * @author Shweta Murthy mailTo: smurthy3@asu.edu
 * @version 4/15/16
 */
public class AsyncJsonConnect extends AsyncTask<Info, Void, JSONObject> {

    Exception mException = null;
    private Search parent;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mException = null;
    }

    @Override
    protected JSONObject doInBackground(Info... params) {
        Info info = params[0];
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://www.omdbapi.com/?");
        urlString.append("t=").append(info.movie);
        urlString.append("&y=");
        urlString.append("&plot=").append("short");
        urlString.append("&r=").append("json");

        Log.d(this.getClass().getSimpleName(), "In do in background");
        Log.d(this.getClass().getSimpleName(),urlString.toString());

        parent = info.parent;
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;
        try {
            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            object = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e) {
            this.mException = e;
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return object;
    }

    @Override
    protected void onPostExecute(JSONObject result)
    {
        String mname;
        String rate;
        String rel;
        String pl;
        String ac;
        String ge;
        super.onPostExecute(result);
        Log.d(this.getClass().getSimpleName(),"IN onpostexecute");
        Log.d(this.getClass().getSimpleName(),result.toString());
        try {
            Log.d(this.getClass().getSimpleName(),result.optString("Title"));
            mname = result.getString("Title");
            rate = result.getString("Rated");
            rel = result.getString("Released");
            pl = result.getString("Plot");
            ac = result.getString("Actors");
            ge = result.getString("Genre");
            parent.n.setText(mname);
            parent.ra.setText(rate);
            parent.re.setText(rel);
            parent.p.setText(pl);
            parent.a.setText(ac);
            parent.g.setText(ge);
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                parent.error= result.getString("Error");
                parent.response = "False";
                parent.dialog();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return;
        }


    }
}
