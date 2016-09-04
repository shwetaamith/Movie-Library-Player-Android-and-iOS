package edu.asu.msse.smurthy3.assign9test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * @version 4/15/16
 */
public class Search extends Activity {
    public TextView n;
    public TextView ra;
    public TextView re;
    public TextView p;
    public TextView a;
    public TextView g;
    public EditText s ;
    public Button b;
    public String name;
    public String response;
    public String error;
    public MainActivity parent;
    public ArrayAdapter adapter;
    public Intent intent;
    public Info i;
    String filename = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        intent = getIntent();
        n = (TextView) findViewById(R.id.name);
        ra = (TextView) findViewById(R.id.rated);
        re = (TextView) findViewById(R.id.released);
        g = (TextView) findViewById(R.id.genre);
        p = (TextView) findViewById(R.id.plot);
        a = (TextView) findViewById(R.id.actors);
        b = (Button) findViewById(R.id.ok);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        s= (EditText) findViewById(R.id.searchMe);
        s.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    go(v);

                    return true;
                }
                return false;
            }
        });

    }


    public void go(View view) {
        android.util.Log.d(this.getClass().getSimpleName(),"Go clicked");
        AsyncJsonConnect asyncJsonConnect = new AsyncJsonConnect();
        s= (EditText) findViewById(R.id.searchMe);
        name = s.getText().toString();
        Info i = new Info(name,this,"search");
        asyncJsonConnect.execute(i);
    }
    public void dialog(){
        if(response=="False"){
            new AlertDialog.Builder(this)
                    .setTitle("Sorry!")
                    .setMessage(error)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        }
    }

    public void add(View view){
        i = new Info((String)n.getText(),(String)ra.getText(),(String)re.getText(),(String)g.getText(),(String)p.getText(),(String)a.getText(),filename,"add");
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
            cv.put("filename",filename);
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
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    ex.getMessage());
        }
        setResult(RESULT_OK,intent);

    }

    public void insert(String name,ContentValues values, int count){
        count = count+1;
        MovieDB db = new MovieDB((Context) this);
        try {
            Log.d(this.getClass().getSimpleName(), "In insert");
            SQLiteDatabase crsDB = db.openDB();
            String query = "insert into movie (name, rated, released, genre, plot,filename, movieid) values ('"+i.movie+ "','" + i.rated + "','" + i.released + "','" + i.genre+"','"+i.plot+"','"+filename+"',"+count+");";
            //Cursor cur = crsDB.rawQuery("insert into movie values ('"+i.movie+"','"+i.rated+"','"+i.released+"','"+i.genre+"','"+i.plot+"',"+count+");",new String[]{});
            //crsDB.insert(name, null, values);

            crsDB.execSQL(query);
            String query2 = "insert into actors (actorname, movieid) values ('"+i.actors+"',"+count+");";
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
