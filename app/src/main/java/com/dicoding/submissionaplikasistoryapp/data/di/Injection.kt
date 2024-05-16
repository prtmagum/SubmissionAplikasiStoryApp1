package com.dicoding.submissionaplikasistoryapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.submissionaplikasistoryapp.data.StoryRepository
import com.dicoding.submissionaplikasistoryapp.data.api.ApiConfig
import com.dicoding.submissionaplikasistoryapp.data.pref.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}