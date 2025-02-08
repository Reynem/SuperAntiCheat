package com.example.superanticheat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activesettings)

        val clearAccount: Button = findViewById(R.id.clearAcc)
        clearAccount.setOnClickListener{
            AuthManager.clear()
            Toast.makeText(this, "Вы успешно вышли с аккаунта", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val changeName: Button = findViewById(R.id.ChangeName)

    }
    fun updateUserName(newName: String) {
        val token = "Bearer ${AuthManager.accessToken}"
        val request = UpdateNameRequest(newName)

        val call = RetrofitClient.apiService.updateName(token, request)

        call.enqueue(object : Callback<UpdateNameResponse> {
            override fun onResponse(call: Call<UpdateNameResponse>, response: Response<UpdateNameResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        AuthManager.nickname = it.new_name
                        Toast.makeText(this@SettingsActivity, "Имя изменено!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SettingsActivity, "Ошибка изменения имени", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateNameResponse>, t: Throwable) {
                Toast.makeText(this@SettingsActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}