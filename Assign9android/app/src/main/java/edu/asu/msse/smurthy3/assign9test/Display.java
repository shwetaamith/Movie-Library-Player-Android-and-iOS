package edu.asu.msse.smurthy3.assign9test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;

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
public class Display extends Activity {
    // public Button del = (Button) findViewById(R.id.delete);
    // public Button button = (Button) findViewById(R.id.button);
    public TextView t1;
    String name;
    public MainActivity parent;
    public ArrayAdapter adapter;
    public Intent intent;
    String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        intent = getIntent();
        Log.d(this.getClass().getSimpleName(), "intent received");
        name = intent.getStringExtra("name");
        String rated = intent.getStringExtra("rated");
        String released = intent.getStringExtra("released");
        String genre = intent.getStringExtra("genre");
        String plot = intent.getStringExtra("plot");
        String actors = intent.getStringExtra("actor");
        filename = intent.getStringExtra("filename");
        t1 = (TextView) findViewById(R.id.textView1);
        final TextView t2 = (TextView) findViewById(R.id.textView2);
        final TextView t3 = (TextView) findViewById(R.id.textView3);
        final TextView t4 = (TextView) findViewById(R.id.textView4);
        final TextView t5 = (TextView) findViewById(R.id.textView5);
        final TextView t6 = (TextView) findViewById(R.id.textView6);
        t1.setText(name);
        t2.setText(actors);
        t3.setText(plot);
        t4.setText(genre);
        t5.setText(released);
        t6.setText(rated);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putExtra("result","ok");setResult(RESULT_OK,intent);finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        intent.putExtra("result","back");
        setResult(RESULT_OK,intent);
        finish();
    }

    public void delete(View view) throws SQLException {
        MovieDB db = new MovieDB((Context) this);
        SQLiteDatabase crsDB = db.openDB();
        //int value = crsDB.delete("movie","name=?",new String[]{name});
        //parent.adapter.notifyDataSetChanged();
        //System.out.println("The result of delete: "+value);
        //Cursor cur = crsDB.rawQuery("DELETE FROM movie WHERE name=?;",
        //      new String[]{name});
        //parent.adapter.notifyDataSetChanged();
        /*Cursor cur1 = crsDB.rawQuery("Select name from movie;", new String[]{});
        ArrayList<String> al = new ArrayList<String>();
        while (cur1.moveToNext()) {
            try {
                al.add(cur1.getString(0));
            } catch (Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(), "exception stepping thru cursor" + ex.getMessage());
            }
        }
        Log.d(this.getClass().getSimpleName(),al.toString());
        */Log.d(this.getClass().getSimpleName(),"Back to main for delete");
        System.out.println(t1.getText().toString());
        intent.putExtra("result","delete");
        //intent.setData(Uri.parse(t1.getText().toString()));
        //parent.deleteMovie = t1.getText().toString();
        setResult(RESULT_OK,intent);
        finish();
    }
    public void playMe(View view){

            if(this.filename.contains(".")){
                Intent i = new Intent(this,Play.class);
                i.putExtra("Filename",this.filename);
                startActivity(i);
        }
        else{
                new AlertDialog.Builder(this)
                        .setTitle("Sorry!")
                        .setMessage("No video for this movie!")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
    }
}
