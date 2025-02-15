package com.example.superanticheat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.superanticheat.addition.CameraAnalyzer
import com.example.superanticheat.addition.DetectionService

class CameraActivity : AppCompatActivity() {
    private lateinit var viewFinder: PreviewView
    private lateinit var detectionService: DetectionService
    private val targetClassId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activcamera)

        viewFinder = findViewById(R.id.viewFinder)

        detectionService = RetrofitClient.getDetectionService()
        val backMain: Button = findViewById(R.id.button_open_main)
        backMain.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        startCamera()
    }

        private fun startCamera() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = viewFinder.surfaceProvider
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(640, 480))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build().also {
                        it.setAnalyzer(
                            ContextCompat.getMainExecutor(this),
                            CameraAnalyzer(detectionService, this, targetClassId)
                        )
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                } catch (exc: Exception) {
                    Log.e("CameraActivity", "Ошибка при привязке use case", exc)
                }
            }, ContextCompat.getMainExecutor(this))
        }
}