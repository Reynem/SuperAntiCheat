package com.example.superanticheat

import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"
    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${AuthManager.accessToken}")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

data class User(
    val name: String,
    val email: String,
    val password: String
)

data class UserLogin(
    val email: String,
    val password: String
)

data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val user_id: Long
)

data class RegisterResponse(
    val message: String,
    val user_id: Long
)

data class SomeDataModel(
    val id: Int,
    val content: String,
    @SerializedName("created_at")
    val createdAt: String
)

interface ApiService {
    @POST("/register")
    suspend fun registerUser(@Body user: User): Response<RegisterResponse>

    @POST("/login")
    suspend fun loginUser(@Body user: UserLogin): Response<LoginResponse>

    @GET("/protected")
    suspend fun getProtectedData(): Response<SomeDataModel>
}
