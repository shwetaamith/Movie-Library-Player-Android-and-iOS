package edu.asu.msse.smurthy3.assign9test;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
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
 * @version 2/29/16
 */

public class DisplayFromServer extends AppCompatActivity {
    Movie m = new Movie();
    public Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie);
        intent = getIntent();
        String movie = intent.getStringExtra("movie");
        Log.d(this.getClass().getSimpleName(), "In DisplayFromServer, got intent");
        m = new Movie(movie);
        System.out.println(m.name);
        final TextView t1 = (TextView) findViewById(R.id.textView1);
        final TextView t2 = (TextView) findViewById(R.id.textView2);
        final TextView t3 = (TextView) findViewById(R.id.textView3);
        final TextView t4 = (TextView) findViewById(R.id.textView4);
        final TextView t5 = (TextView) findViewById(R.id.textView5);
        final TextView t6 = (TextView) findViewById(R.id.textView6);
        final TextView t7 = (TextView) findViewById(R.id.textView7);
        Button b = (Button) findViewById(R.id.button);
        t1.setText(m.name);
        t2.setText(m.actors);
        t3.setText(m.plot);
        t4.setText(m.genre);
        t5.setText(m.released);
        t6.setText(m.rated);
        t7.setText(m.year);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void play(View view
    ){
        Intent intent = new Intent(DisplayFromServer.this,Play.class);
        intent.putExtra("Filename",m.filename);
        startActivity(intent);
    }

    public void add(View view){
        android.util.Log.d(this.getClass().getSimpleName(), "In add from DisplayFromServer name of movie to be added:" + m.name);
        //mainActivity.serverMovie = this.m;
        String[] s = new String[]{m.name,m.rated,m.released,m.genre,m.plot,m.actors,m.filename};
        Info i = new Info(m.name,m.rated,m.released,m.genre,m.plot,m.actors,m.filename,"add");

        try {
            MovieDB db = new MovieDB((Context) this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cursor = crsDB.rawQuery("select movieid from movie;", new String[]{});
            int count = cursor.getCount();
            System.out.println("Count is: " + count);
            ContentValues cv=new ContentValues();
            cv.put("name",i.movie);
            cv.put("rated",i.rated);
            cv.put("released",i.released);
            cv.put("genre",i.genre);
            cv.put("plot",i.plot);
            cv.put("movieid",count);
            cv.put("actorname",i.actors);
            cv.put("filename",i.filename);
            Log.d(this.getClass().getSimpleName(), "Done till here");
            System.out.println("Plot:"+i.plot);
            //String query = "insert into movie values ('"+i.movie+ "','" + i.rated + "','" + i.released + "','" + i.genre+"','"+i.plot+"',"+count+");";

            //Log.d(this.getClass().getSimpleName(), query);
            //crsDB.rawQuery("insert into movie values ('"+i.movie+"','"+i.rated+"','"+i.released+"','"+i.genre+"','"+i.plot+"',"+count+");",new String[]{});
            insert("movie",cv, count);
            //crsDB.execSQL(query);
            Log.d(this.getClass().getSimpleName(), "Back from insert");
            check(i.movie);
            Log.d(this.getClass().getSimpleName(), "Back from check");

            //crsDB.insert("movie", null, cv);
            //Cursor cursor1 = crsDB.rawQuery("select plot from movie where movie.name=?",new String[]{i.movie});
            //Log.d(this.getClass().getSimpleName(),cursor1.getString(0));
            cursor.close();
            //cursor1.close();
            crsDB.close();
            db.close();
            intent.putExtra("movie",m.name);
            setResult(RESULT_OK,intent);
            finish();
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    ex.getMessage());
        }


    }
    public void insert(String name,ContentValues values, int count){
        count = count+1;
        MovieDB db = new MovieDB((Context) this);
        try {
            Log.d(this.getClass().getSimpleName(), "In insert");
            SQLiteDatabase crsDB = db.openDB();
            String query = "insert into movie (name, rated, released, genre, plot,filename, movieid) values ('"+values.get("name")+ "','" + values.get("rated") + "','" + values.get("released")+ "','" + values.get("genre")+"','"+values.get("plot")+"','"+values.get("filename")+"',"+count+");";
            //Cursor cur = crsDB.rawQuery("insert into movie values ('"+i.movie+"','"+i.rated+"','"+i.released+"','"+i.genre+"','"+i.plot+"',"+count+");",new String[]{});
            //crsDB.insert(name, null, values);

            crsDB.execSQL(query);
            String query2 = "insert into actors (actorname, movieid) values ('"+values.get("actorname")+"',"+count+");";
            crsDB.execSQL(query2);
            Log.d(this.getClass().getSimpleName(),"Done inserting");
            crsDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void check(String movie) throws SQLException {
        MovieDB db = new MovieDB((Context) this);
        Log.d(this.getClass().getSimpleName(),"in check");
        SQLiteDatabase crsDB = db.openDB();
        Cursor cursor1 = crsDB.rawQuery("select name from movie;", new String[]{});
        ArrayList<String> al = new ArrayList<String>();
        while (cursor1.moveToNext()) {
            try {
                al.add(cursor1.getString(0));
            } catch (Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(), "exception stepping thru cursor" + ex.getMessage());
            }
        }
        String[] movies = (String[]) al.toArray(new String[al.size()]);
        Log.d(this.getClass().getSimpleName(), String.valueOf(movies.length));
        Log.d(this.getClass().getSimpleName(),"Here after select");
    }


}
