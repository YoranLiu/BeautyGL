package com.jack.beautygl.camera

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraHelper(private val context: Context) {
    private var lensFacing = CameraSelector.LENS_FACING_FRONT
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var preview: Preview
    private lateinit var cameraSelector: CameraSelector

    fun openCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder()
                .build().also {
                    //it.setSurfaceProvider(Camer) // setSurfaceProvider(render)
                }

            // select camera(front or back)
            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            // image analyzer
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    context as LifecycleOwner,
                    cameraSelector,
                    preview
                // image analyzer
                )
            } catch (e: Exception) {
                Log.e(TAG, "openCamera: " + "Case binding failed")
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun switchCamera() {
        if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            lensFacing = CameraSelector.LENS_FACING_BACK

            Toast.makeText(context, "Switch to rear camera", Toast.LENGTH_SHORT)
                .show()
        }
        else {
            lensFacing = CameraSelector.LENS_FACING_FRONT

            Toast.makeText(context, "Switch to front camera", Toast.LENGTH_SHORT)
                .show()
        }
        openCamera()
    }

    companion object {
        private val TAG = CameraHelper::class.java.simpleName
    }
}