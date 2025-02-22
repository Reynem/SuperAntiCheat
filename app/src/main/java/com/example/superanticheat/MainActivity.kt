package com.example.superanticheat

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest;
import android.widget.ImageView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val accounts: ImageButton = findViewById(R.id.account)
        accounts.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val cameraFunc: ImageButton = findViewById(R.id.Camera)
        cameraFunc.setOnClickListener {
            if (AuthManager.isLoggedIn) {
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Для использования функции требуется авторизация", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
        }

        val settings: ImageButton = findViewById(R.id.settings)
        settings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            val REQUEST_CAMERA_PERMISSION = 1
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }



    }
    override fun onResume() {
        super.onResume()
        val gifImageView: ImageView = findViewById(R.id.gifImageView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.animation)
            .into(gifImageView)
    }
}

