package com.jack.beautygl.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.jack.beautygl.camera.CameraHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLCameraView(context: Context?, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {
    private lateinit var cameraHelper: CameraHelper

    init {
        setEGLContextClientVersion(2)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    inner class GLCameraRender(): GLSurfaceView.Renderer {
        override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {

        }

        override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {

        }

        override fun onDrawFrame(p0: GL10?) {

        }
    }

    fun switchCamera() {

    }
}