package com.example.superanticheat.addition

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.superanticheat.addition.DetectionService
import kotlinx.coroutines.*

class CameraAnalyzer(
    private val detectionService: DetectionService,
    private val context: Context,
    private val targetClassId: Int
) : ImageAnalysis.Analyzer {

    private var lastAnalyzedTime = 0L

    override fun analyze(imageProxy: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalyzedTime >= 1000) {
            lastAnalyzedTime = currentTime

            val bitmap = imageProxy.toBitmap()
            sendFrameToServer(bitmap, detectionService, targetClassId, context)
        }
        imageProxy.close()
    }
}