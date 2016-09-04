package edu.asu.msse.smurthy3.assign9test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class MovieDB extends SQLiteOpenHelper {
    private static final boolean debugon = false;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "moviedb";
    private String dbPath;
    private SQLiteDatabase crsDB;
    private final Context context;

    public MovieDB(Context context) {
        super(context,dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath()+"/";
        context.deleteDatabase(dbName);
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }

    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    private boolean checkDB(){    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean ret = false;
        try{
            String path = dbPath + dbName + ".db";
            //debug("CourseDB --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    //debug("CourseDB --> checkDB","opened db at: "+checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='movie';", null);
                    boolean crsTabExists = false;
                    if(tabChk == null){
                        //debug("CourseDB --> checkDB","check for course table result set is null");
                    }else{
                        tabChk.moveToNext();
                        //debug("CourseDB --> checkDB","check for course table result set is: " +
                        //      ((tabChk.isAfterLast() ? "empty" : (String) tabChk.getString(0))));
                        crsTabExists = !tabChk.isAfterLast();
                    }
                    if(crsTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM movie", null);
                        c.moveToFirst();
                        while(! c.isAfterLast()) {
                            String name = c.getString(0);
                            String rated = c.getString(1);
                            String released = c. getString(2);
                            String genre = c.getString(3);
                            String plot = c.getString(4);
                            //debug("CourseDB --> checkDB","Course table has CourseName: "+
                            //      crsName+"\tCourseID: "+crsid);
                            c.moveToNext();
                        }
                        ret = true;
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("CourseDB->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return ret;
    }

    public void copyDB() throws IOException{
        try {
            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                //debug("CourseDB --> copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.moviedb);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("CourseDB --> copyDB", "IOException: "+e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        if(checkDB()) {
            crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            //debug("CourseDB --> openDB", "opened db at path: " + crsDB.getPath());
        }else{
            try {
                this.copyDB();
                crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
            }
        }
        return crsDB;
    }

    @Override
    public synchronized void close() {
        if(crsDB != null)
            crsDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String myPath = dbPath + dbName + ".db";
        try {
            this.copyDB();
            crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch(Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
        }

    }
}
