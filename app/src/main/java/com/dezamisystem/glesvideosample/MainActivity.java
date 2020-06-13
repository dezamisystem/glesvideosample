/*
 * Copyright (c) 2020. 東亜プリン秘密研究所 All rights reserved.
 */

package com.dezamisystem.glesvideosample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String VIDEO_CONTENT_NAME = "blazingstar01.mp4";

    private VideoSurfaceView mVideoView;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.video_surface_view);
        mVideoView.setSurfaceListener(videoSurfaceListener);
        mMediaPlayer = new MediaPlayer();
        try( AssetFileDescriptor afd = getAssets().openFd(VIDEO_CONTENT_NAME)) {
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    VideoSurfaceView.SurfaceListener videoSurfaceListener = new VideoSurfaceView.SurfaceListener() {
        @Override
        public void onSurfaceCreated(Surface surface) {

            mMediaPlayer.setSurface(surface);
            mVideoView.releaseSurface();
            try {
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onGlError(String text) {

            mMediaPlayer.stop();
        }
    };

}
