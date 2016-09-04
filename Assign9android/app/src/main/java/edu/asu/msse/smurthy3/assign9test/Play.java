package edu.asu.msse.smurthy3.assign9test;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Copyright (c) 2016 Shweta Murthy,
 * You may not use this file except for self-evaluation and practice
 * This file is allowed to be used for grading puroposes
 * through the spring semester 2016, ASU, by  the grader, TA and the instructor
 * Unless agreed to in writing, this material can is to be
 * distributed on an "AS IS" BASIS
 *
 * @author Shweta Murthy mailTo: smurthy3@asu.edu
 * @version 4/14/16
 */

public class Play extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private VideoView mVideoView;
    private MediaController mController;
    private MediaMetadataRetriever mMetadataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        final Intent intent = getIntent();
        String file = intent.getStringExtra("Filename");
        Log.d(this.getClass().getSimpleName(), "In Play, got intent");
        mVideoView = (VideoView) findViewById(R.id.avideoview);
        mVideoView.setVideoPath(getString(R.string.urlprefix) + file);
        //mVideoView.setVideoPath(getString(R.string.urlprefix) + "Crazy.mp3");
        MediaController mediaController = new MediaController((Context)this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnPreparedListener(this);
    }

    public void onPrepared(MediaPlayer mp){
        android.util.Log.d(this.getClass().getSimpleName(), "onPrepared called. Video Duration: "
                + mVideoView.getDuration());
        mVideoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        android.util.Log.d(this.getClass().getSimpleName(), "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
