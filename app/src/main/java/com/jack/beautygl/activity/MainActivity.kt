package com.jack.beautygl.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jack.beautygl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var startBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startBtn = binding.startBtn

        startBtn.setOnClickListener {
            if (!hasPermissions(this)) {
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(), permissionRC)
                Log.d(TAG, "onCreate: " + "a")
            }
            else {
               Intent(this, CameraActivity::class.java)
                   .apply {
                       startActivity(this)
                   }
                Log.d(TAG, "onCreate: " + "b")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRC && hasPermissions(this)) {
            Intent(this, CameraActivity::class.java)
                .apply {
                    startActivity(this)
                }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG)
                .show()
        }
        else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG)
                .show()

            finish()
        }
    }

    private fun hasPermissions(context: Context) = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val permissionRC = 100
        private val permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}