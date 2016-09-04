package edu.asu.msse.smurthy3.assign9test;

import android.util.Log;

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
public class Info {
    public String movie;
    public Search parent;
    public String rated;
    public String released;
    public String genre;
    public String plot;
    public String actors;
    public String filename;
    public String method;
    String moviename[];
    Info(String name, Search root, String method){

        if(name.contains(" ")){
            moviename = name.split(" ");
            this.movie = moviename[0]+"+"+moviename[1];}
        else{ this.movie = name;}
        this.parent= root;
        this.method = method;
        Log.d(this.getClass().getSimpleName(), movie + " " + parent);
    }

    Info(String name, String rated, String rel, String ge, String pl, String actors,String Filename, String method){
        if(pl.contains("'")){
            String[] a = pl.split("'");
            String s = "";
            int c = a.length;
            for(int i=0;i<c;i++){
                s = s+a[i];

            }
            System.out.println(s);
            this.plot = s;
        }
        else{this.plot = pl;}
        //this.plot = pl;

        this.movie = name;
        this.rated = rated;
        this.released = rel;
        this.genre = ge;
        this.actors = actors;
        this.method = method;
        this.filename = Filename;
    }


}
