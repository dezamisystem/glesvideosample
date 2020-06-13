/*
 * Copyright (c) 2020. 東亜プリン秘密研究所 All rights reserved.
 */

package com.dezamisystem.glesvideosample;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;

import androidx.annotation.Nullable;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoSurfaceView extends GLSurfaceView implements VideoRender.RendererListener, SurfaceTexture.OnFrameAvailableListener {

    public interface SurfaceListener {
        void onSurfaceCreated(Surface surface);
        void onGlError(String text);
    }

    private static final String TAG = "VideoSurfaceView";

    private VideoRender mRenderer;
    @Nullable
    private SurfaceTexture surfaceTexture;
    @Nullable
    private Surface videoSurface;
    private boolean isSurfaceUpdated = false;
    @Nullable
    private SurfaceListener listener = null;

    public VideoSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        mRenderer = new VideoRender(context, this);
        setRenderer(mRenderer);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);
        mRenderer = new VideoRender(context, this);
        setRenderer(mRenderer);
    }

    public void setSurfaceListener(SurfaceListener listener) {

        this.listener = listener;
    }

    public void releaseSurface() {

        if (videoSurface != null) {
            videoSurface.release();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig, int textureId) {

        surfaceTexture = new SurfaceTexture(textureId);
        surfaceTexture.setOnFrameAvailableListener(this);

        videoSurface = new Surface(surfaceTexture);

        if (listener != null) {
            listener.onSurfaceCreated(videoSurface);
        }

        synchronized(this) {
            isSurfaceUpdated = false;
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        synchronized (this) {
            if (isSurfaceUpdated && surfaceTexture != null) {
                surfaceTexture.updateTexImage();
                surfaceTexture.getTransformMatrix(mRenderer.getSTMatrix());
                isSurfaceUpdated = false;
            }
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

        synchronized (this) {
            isSurfaceUpdated = true;
        }
    }

    @Override
    public void onGlError(String text) {

        if (listener != null) {
            listener.onGlError(text);
        }
    }
}
