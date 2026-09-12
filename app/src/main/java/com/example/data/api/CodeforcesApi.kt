package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class CfUser(
    val handle: String,
    val rating: Int? = 0,
    val maxRating: Int? = 0,
    val rank: String? = "newbie",
    val maxRank: String? = "newbie"
)

data class CfUserResponse(
    val status: String,
    val result: List<CfUser>?
)

interface CodeforcesApiService {
    @GET("api/user.info")
    suspend fun getUserInfo(@Query("handles") handles: String): CfUserResponse

    companion object {
        private const val BASE_URL = "https://codeforces.com/"

        fun create(): CodeforcesApiService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(CodeforcesApiService::class.java)
        }
    }
}
