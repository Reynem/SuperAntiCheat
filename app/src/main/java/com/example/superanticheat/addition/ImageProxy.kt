package com.example.superanticheat.addition
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import okhttp3.MediaType
import showNotification
import com.example.superanticheat.addition.DetectionService


fun ImageProxy.toBitmap(): Bitmap? {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val width = this.width
    val height = this.height
    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

fun sendFrameToServer(
    bitmap: Bitmap,
    detectionService: DetectionService,
    targetClassId: Int,
    context: Context
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val byteArray = stream.toByteArray()
            val mediaType = MediaType.parse("image/jpeg")
            if (mediaType != null){
                println("MediaType: $mediaType")
            } else{
                Log.e("invalid mediatype", "Ошибка неверный mediatype")
            }
            val requestFile = RequestBody.create(mediaType, byteArray)
            val body = MultipartBody.Part.createFormData("file", "frame.jpg", requestFile)

            val response = detectionService.detectObject(body)
            if (response.isSuccessful) {
                val detectionResponse = response.body()
                val detections = detectionResponse?.detections
                Log.d("Detection", "дошел до обработки нулл или нет")
                if (detections != null) {
                    Log.d("Detection", "не нулл")

                    for (detection in detections) {
                        withContext(Dispatchers.Main) {
                            showNotification(context, "Обнаружено", "Получен ответ: ${detection.class_name ?: detection.class_id}")

                        }
                        Log.d("Detection", "class_id: ${detection.class_id}, targetClassId: $targetClassId, class_name: ${detection.class_name}")
                        if (detection.class_id == targetClassId) {
                            withContext(Dispatchers.Main) {
                                try {
                                    showNotification(context, "Обнаружен объект", "Обнаружен объект с id: $targetClassId")
                                    showNotification(context, "Обнаружен объект", "Необходимо принять меры против списывания!")
                                } catch (e: Exception) {
                                    Log.e("Notification", "Error showing notification: ${e.message}")
                                }
                            }
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
