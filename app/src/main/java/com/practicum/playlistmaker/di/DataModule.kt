package com.practicum.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.favorite.data.db.FavoriteDataBase
import com.practicum.playlistmaker.playlist.data.db.PlaylistDataBase
import com.practicum.playlistmaker.search.data.local.LocalStorage
import com.practicum.playlistmaker.search.data.local.impl.LocalStorageImpl
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.impl.NetworkClientImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataModule {

    companion object{
        private val dbFavoriteTracks: String = "dbFavoriteTracks.db"
        private val dbPlaylists:String = "dbPlaylist.db"
        private val itunesBaseUrl = "https://itunes.apple.com"
    }

    val dataModule = module {

        //Dependency for NetworkClientImpl
        single<ItunesApi>{

            //Interceptor for take logs about request http
             val interceptorHttp = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

             val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptorHttp)
                .build()

                 Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(itunesBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesApi::class.java)
        }

        // Dependency for LocalStorageImpl
        single<SharedPreferences> {
            androidContext().getSharedPreferences(LocalStorageImpl.KEY_SAVED_SEARCH,Context.MODE_PRIVATE)
        }

        // Dependency for LocalStorageImpl
        single<Gson>{
            Gson()
        }

        single<NetworkClient> {
            NetworkClientImpl(get())
        }

        single<LocalStorage>{
            LocalStorageImpl(get(),get())
        }

        //Dependency for FavoriteRepositoryImpl
        single{
            Room.databaseBuilder(androidContext(),FavoriteDataBase::class.java, dbFavoriteTracks)
                .build()
        }

        //Dependency for PlaylistRepositoryImpl
        single{
            Room.databaseBuilder(androidContext(),PlaylistDataBase::class.java, dbPlaylists)
                .build()
        }
    }

}