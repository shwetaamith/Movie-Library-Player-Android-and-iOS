package edu.asu.msse.smurthy3.assign9test;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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

public class getFromServer extends ListActivity {
    public String defaulturl;
    public ArrayAdapter adapter;
    public Intent intent;
    public String[] n;

    //public String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getfromserver);
        intent = getIntent();
        defaulturl = intent.getStringExtra("url");
        ArrayList<String> movies = new ArrayList<String>();
        movies.add("unknown");
        android.util.Log.d(this.getClass().getSimpleName(), defaulturl);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,movies);
        setListAdapter(adapter);

        try{
        MethodInformation mi = new MethodInformation(this,defaulturl,"resetFromJsonFile",
                new String[]{});
        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
    } catch (Exception ex){
        Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                ex.getMessage());
    }


    try{
        MethodInformation mi = new MethodInformation(this,defaulturl,"getTitles",
                new String[]{});
        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
    } catch (Exception ex){
        Log.w(this.getClass().getSimpleName(),"Exception creating adapter: "+
                ex.getMessage());
    }

}

    @Override
    protected void onListItemClick (ListView listView, View v,int position, long id){
        super.onListItemClick(listView, v, position, id);
        String movie = (String) listView.getItemAtPosition(position);
        Log.d(this.getClass().getSimpleName(), movie);
        try{
            MethodInformation mi = new MethodInformation(this,defaulturl,"get",
                    new String[]{movie});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex){
            Log.w(this.getClass().getSimpleName(),"Exception creating adapter: "+
                    ex.getMessage());
        }
    }

    public void back(View view){
        String s = intent.getStringExtra("movie");
        setResult(RESULT_OK, intent);
        finish();
    }

}
