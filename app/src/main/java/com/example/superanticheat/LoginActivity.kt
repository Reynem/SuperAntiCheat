package com.example.superanticheat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val toRegister: TextView = findViewById(R.id.toRegister)
        toRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        val emailView: EditText = findViewById(R.id.emailInput)
        val passwordView: EditText = findViewById(R.id.passwordInput)
        val loginButton: Button = findViewById(R.id.login_butt)
        loginButton.setOnClickListener {
            val email: String = emailView.text.toString().trim()
            val password: String = passwordView.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Email: '$email', Password: '$password'")
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val user = UserLogin(email, password)
                val response = RetrofitClient.apiService.loginUser(user)

                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Авторизация успешна", Toast.LENGTH_SHORT).show()
                    response.body()?.let {
                        AuthManager.accessToken = it.access_token
                        AuthManager.userId = it.user_id
                    }
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("LoginActivity", "Error during authorisation", e)
            }

        }
    }


}