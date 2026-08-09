package com.miclima.app.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.miclima.app.data.local.AppDatabase
import com.miclima.app.data.remote.ClimaApi
import com.miclima.app.data.remote.GeocodingApi
import com.miclima.app.data.repository.ClimaRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {

    private lateinit var db: AppDatabase

    fun init(context: Context) {
        if (::db.isInitialized) return
        db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "miclima.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val gson = Gson()

    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }

    private fun retrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    private val geocodingApi: GeocodingApi by lazy {
        retrofit("https://geocoding-api.open-meteo.com/").create(GeocodingApi::class.java)
    }

    private val climaApi: ClimaApi by lazy {
        retrofit("https://api.open-meteo.com/").create(ClimaApi::class.java)
    }

    val repositorio: ClimaRepository by lazy {
        ClimaRepository(geocodingApi, climaApi, db.ciudadDao(), db.climaCacheDao(), gson)
    }
}
