package edu.asu.msse.smurthy3.assign9test;

import android.util.Log;

import org.json.JSONObject;

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
public class Movie {
    public String name;
    public String year;
    public String rated;
    public String released;
    public String genre;
    public String plot;
    public String actors;
    public String filename;


    public Movie(){

    }

    public Movie(String name, String year, String rated, String released, String genre, String plot, String actors, String filename){
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.rated = rated;
        this.released = released;
        this.actors = actors;
        this.plot = plot;
        this.filename = filename;
    }

    Movie(String jsonStr) {
        try {
            JSONObject jo = new JSONObject(jsonStr);
            name = jo.getString("Title");
            year = jo.getString("Year");
            rated = jo.getString("Rated");
            released = jo.getString("Released");
            genre = jo.getString("Genre");
            plot = jo.getString("Plot");
            actors = jo.getString("Actors");
            filename = jo.getString("Filename");
            Log.d(this.getClass().getSimpleName(),"Successfully created object");
        } catch (Exception ex) {
            Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }

    Movie(JSONObject jsonObj){
        try{
            System.out.println("constructor from json received: " +
                    jsonObj.toString());
            name = jsonObj.getString("Title");
            Log.d(this.getClass().getSimpleName(), jsonObj.getString("Title"));
            year = jsonObj.getString("Year");
            Log.d(this.getClass().getSimpleName(),jsonObj.getString("Year"));

            rated = jsonObj.getString("Rated");
            Log.d(this.getClass().getSimpleName(),jsonObj.getString("Rated"));
            released = jsonObj.getString("Released");
            Log.d(this.getClass().getSimpleName(),jsonObj.getString("Released"));

            genre = jsonObj.getString("Genre");
            Log.d(this.getClass().getSimpleName(),jsonObj.getString("Genre"));
            plot = jsonObj.getString("Plot");
            Log.d(this.getClass().getSimpleName(),jsonObj.getString("Plot"));
            actors = jsonObj.getString("Actors");
            Log.d(this.getClass().getSimpleName(),jsonObj.getString("Actors"));
            filename = jsonObj.getString("Filename");
            Log.d(this.getClass().getSimpleName(),jsonObj.getString("Actors"));


        }catch(Exception ex){
            System.out.println("error converting to/from json");
        }
    }

    public JSONObject toJson(){
        JSONObject jo = new JSONObject();
        try{
            jo.put("Title", name);
            jo.put("Year", year);
            jo.put("Rated", rated);
            jo.put("Released", released);
            jo.put("Genre", genre);
            jo.put("Plot", plot);
            jo.put("Actors", actors);
            jo.put("Filename",filename);
        }catch (Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting to json");
        }
        return jo;
    }


    public String toJsonString() {
        String ret = "";
        try {
            JSONObject jo = new JSONObject();
            jo.put("Title", name);
            jo.put("Year", year);
            jo.put("Rated", rated);
            jo.put("Released", released);
            jo.put("Genre", genre);
            jo.put("Plot", plot);
            jo.put("Actors", actors);
            jo.put("Filename",filename);
            ret = jo.toString();
        } catch (Exception ex) {
            Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return ret;
    }
}
