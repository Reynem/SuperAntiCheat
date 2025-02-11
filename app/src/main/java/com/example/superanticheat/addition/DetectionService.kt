package com.example.superanticheat.addition

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DetectionService {
    @Multipart
    @POST("/detect")
    suspend fun detectObject(
        @Part file: MultipartBody.Part
    ): Response<DetectionResponse>
}

data class DetectionResponse(
    val detections: List<DetectionResult>
)

data class DetectionResult(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
    val confidence: Float,
    val class_id: Int,
    val class_name: String?
)
