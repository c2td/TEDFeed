package com.application.pm.tedfeed;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

/* This activity is responsible for playing a video when a corresponding button is clicked
   in the Details fragment view.
 */
public class VideoActivity extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private VideoView mVideoView;
    private View mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Get the correspodig video url
        String videoUrl = getIntent().getStringExtra("videoUrl");

        // Loading indicator is shown until the video has finished loading
        mLoadingIndicator = findViewById(R.id.video_loading_indicator);

        // Get the view object and attach the listeners
        mVideoView = (VideoView) findViewById(R.id.video);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);

        // If the Url is corrupted, exit the activity
        if (!correctResource(videoUrl)) {
            mVideoView.stopPlayback();
            finish();
            return;
        }

        // Set up the media controller for buttons and progress bar
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);

        // Start playing the video
        mVideoView.start();

    }

    private boolean correctResource(String url) {
        if (url == null) {
            return false;
        } else {
            mVideoView.setVideoURI(Uri.parse(url));
            return true;
        }
    }

    /* Close the activity after video completes */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        finish();
    }

    /* Hide the loading indicator after the video has been loaded */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mLoadingIndicator.setVisibility(View.GONE);
    }
}
