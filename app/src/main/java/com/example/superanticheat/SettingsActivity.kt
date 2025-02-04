package com.example.superanticheat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activesettings)

        val clearAccount: Button = findViewById(R.id.ClearAcc)
        clearAccount.setOnClickListener{
            AuthManager.clear()
            Toast.makeText(this, "Вы успешно вышли с аккаунта", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}