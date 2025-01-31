package com.example.superanticheat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
        }

        val loginButton: Button = findViewById(R.id.login_butt)
        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val emailView: EditText = findViewById(R.id.emailInput)
        val nameView: EditText = findViewById(R.id.nameInput)
        val passwordView: EditText = findViewById(R.id.passwordInput)
        val registerButton: Button = findViewById(R.id.registration)
        registerButton.setOnClickListener {
            val name: String = nameView.text.toString().trim()
            val email: String = emailView.text.toString().trim()
            val password: String = passwordView.text.toString().trim()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, email, password)
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                Log.d("RegistrationActivity", "Name: '$name', Email: '$email', Password: '$password'")
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                val user = User(name, email, password)
                val response = RetrofitClient.apiService.registerUser(user)

                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("RegisterActivity", "Error during registration", e)
            }

        }
    }
}