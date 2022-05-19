package com.jack.beautygl.view

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import com.jack.beautygl.camera.CameraHelper
import com.jack.beautygl.filters.BaseFilter
import com.jack.beautygl.utils.FilterFactory
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLCameraView(context: Context?, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {
    private var c: Context?
    private var filterType: FilterFactory.FilterType

    private var renderer: GLCameraRender
    private lateinit var cameraHelper: CameraHelper
    private lateinit var currentFilter: BaseFilter

    private val mtx = FloatArray(16)
    private var textureId = 0
    private lateinit var cameraTexture: SurfaceTexture

    init {
        setEGLContextClientVersion(2)

        c = context
        filterType = FilterFactory.FilterType.Original

        renderer = GLCameraRender(this, filterType)
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    inner class GLCameraRender(
        private val surfaceView: GLSurfaceView,
        filterType: FilterFactory.FilterType
    ): Renderer {

        init {
            cameraHelper = CameraHelper(c!!)
            currentFilter = FilterFactory().createFilter(c!!, filterType)!!
        }

        override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
            Log.d(TAG, "onSurfaceCreated: ")
        }

        override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
            Log.d(TAG, "onSurfaceChanged: ")

            GLES20.glViewport(0, 0, width, height)

            currentFilter.createProgram()

            textureId = currentFilter.bindTexture()

            cameraTexture = SurfaceTexture(textureId)

            cameraHelper.openCamera()
        }

        override fun onDrawFrame(p0: GL10?) {
            Log.d(TAG, "onDrawFrame: ")
            cameraTexture.updateTexImage()
            cameraTexture.getTransformMatrix(mtx)
            currentFilter.draw(textureId, mtx)
        }
    }

    fun switchCamera() {
        cameraHelper.switchCamera()
    }

    companion object {
        private val TAG = GLCameraView::class.java.simpleName
    }
}