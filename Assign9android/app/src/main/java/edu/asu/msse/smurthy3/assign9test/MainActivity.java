package edu.asu.msse.smurthy3.assign9test;



/**
 * Copyright (c) 2016 Shweta Murthy,
 * You may not use this file except for self-evaluation and practice
 * This file is allowed to be used for grading puroposes
 * through the spring semester 2016, ASU, by  the grader, TA and the instructor
 * Unless agreed to in writing, this material can is to be
 * distributed on an "AS IS" BASIS
 *
 * @author Shweta Murthy mailTo: smurthy3@asu.edu
 * @version 2/29/16
 */
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    public String selectedMovie;
    //public Movie serverMovie = new Movie();
    public String movie = "";
    private String[] movies;
    ArrayAdapter adapter;
    String rated = "unknown";
    String released = "unknown";
    String genre = "unknown";
    String plot = "unknown";
    String actor = "unknown";
    String filename = "unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.selectedMovie = this.setupMovie();
        //reset();

    }

    public String setupMovie() {
        String ret = "unknown";
        try {
            Log.d(this.getClass().getSimpleName(), "in setUpMovie");
            this.deleteDatabase("moviedb");

            MovieDB db = new MovieDB((Context) this);
            db.close();
            this.deleteDatabase("moviedb");
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name from movie;", new String[]{});
            ArrayList<String> al = new ArrayList<String>();
            while (cur.moveToNext()) {
                try {
                    al.add(cur.getString(0));
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "exception stepping thru cursor" + ex.getMessage());
                }
            }
            movies = (String[]) al.toArray(new String[al.size()]);
            android.util.Log.d(this.getClass().getSimpleName(), String.valueOf(movies.length));
            //adapter.notifyDataSetChanged();
            ret = (movies.length > 0 ? movies[0] : "unknown");
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movies);

            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
            //studAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, studs);
            //studentSpinner.setAdapter(studAdapter);
            //studentSpinner.setOnItemSelectedListener(this);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "unable to setup");
        }
        return ret;
    }

    @Override
    protected void onListItemClick(ListView listView, View v, int position, long id) {
        super.onListItemClick(listView, v, position, id);

        movie = (String) listView.getItemAtPosition(position);
        Log.d(this.getClass().getSimpleName(), movie);
        try {
            MovieDB db = new MovieDB((Context) this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select rated,released,genre,plot,filename from movie where name=? ;",
                    new String[]{movie});
            Cursor cur2 = crsDB.rawQuery("select actors.actorname from actors,movie where actors.movieid=movie.movieid and movie.name=?;", new String[]{movie});
            while (cur.moveToNext()) {
                rated = cur.getString(0);
                released = cur.getString(1);
                genre = cur.getString(2);
                plot = cur.getString(3);
                filename = cur.getString(4);
                Log.d(this.getClass().getSimpleName(), rated + " " + released + " " + plot + " " + genre+" "+filename);
            }
            while (cur2.moveToNext()) {
                actor = cur2.getString(0);
                Log.d(this.getClass().getSimpleName(), actor);
            }
            sendToDisplay(movie);
            cur.close();
            cur2.close();
            crsDB.close();
            db.close();
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    ex.getMessage());
        }
    }

    public void sendToDisplay(String movie) {
        System.out.println("in sendtodisplay");
        Intent intent = new Intent(MainActivity.this, Display.class);
        intent.putExtra("name", movie);
        intent.putExtra("rated", this.rated);
        intent.putExtra("released", this.released);
        intent.putExtra("plot", this.plot);
        intent.putExtra("genre", this.genre);
        intent.putExtra("actor", this.actor);
        intent.putExtra("filename",this.filename);
        Display d = new Display();
        d.parent = this;
        d.adapter = adapter;
        Log.d(this.getClass().getSimpleName(), "Done till here, ready to send to display");
        startActivityForResult(intent, 2);
        setupMovie();
    }

    public void search(View view) {
        android.util.Log.d(this.getClass().getSimpleName(), "Search clicked");
        Display d = new Display();
        d.parent = this;
        d.adapter = adapter;
        Intent intent = new Intent(MainActivity.this, Search.class);
        startActivityForResult(intent, 1);
    }
    public void getFromServer(View view){
        Intent intent = new Intent(MainActivity.this,getFromServer.class);
        String url = this.getString(R.string.default_url);
        intent.putExtra("url",url);
        startActivityForResult(intent,3);
        adapter.notifyDataSetChanged();
        setupMovie();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            Log.d(this.getClass().getSimpleName(), "back to main from search");
            adapter.notifyDataSetChanged();
            setupMovie();
        }
        if(requestCode == 2) {
            Log.d(this.getClass().getSimpleName(), "back to mainactivity from display, either as OK or delete");
            //Uri ret = intent.getData();
            //String name = ret.toString();
            String n = new String("delete");
            if(intent.getStringExtra("result").equals(n)){
                MovieDB db = new MovieDB((Context) this);
                SQLiteDatabase crsDB = null;
                try {
                    crsDB = db.openDB();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Cursor c = crsDB.rawQuery("Select movieid from movie where name=?",new String[]{movie});
                //int id = c.getInt(0);
                crsDB.rawQuery("Delete from actors where actors.movieid=(select movieid from movie where name=?)",new String[]{movie});
                int value = crsDB.delete("movie", "name=?", new String[]{movie});
                System.out.println("The resultcode of deletion: "+value);
                adapter.notifyDataSetChanged();
                setupMovie();
                crsDB.close();}
        }
        if(requestCode == 3){
            //String s = intent.getStringExtra("movie");
            //movies[movies.length+1] = s;
            setupMovie();
            adapter.notifyDataSetChanged();
        }

    }



}
