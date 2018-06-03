package com.old.time.views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.old.time.utils.DebugLog;

public class VideoGlSurfaceView extends GLSurfaceView {
    private Renderer mRenderer;
    private static final String TAG = VideoGlSurfaceView.class.getName();
    public VideoGlSurfaceView(Context context) {
        super(context);
    }

    public VideoGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if(mRenderer == null) {
            return;
        }
        super.onWindowVisibilityChanged(visibility);
        DebugLog.d(TAG, "onWindowVisibilityChanged");
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        mRenderer = renderer;
        DebugLog.d(TAG, "setRender");
    }
}
