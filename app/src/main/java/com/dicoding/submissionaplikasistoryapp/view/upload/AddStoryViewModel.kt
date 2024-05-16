package com.dicoding.submissionaplikasistoryapp.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionaplikasistoryapp.data.StoryRepository
import com.dicoding.submissionaplikasistoryapp.data.di.Event
import com.dicoding.submissionaplikasistoryapp.data.pref.UserModel
import com.dicoding.submissionaplikasistoryapp.response.AddStoryResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    val uploadResponse: LiveData<AddStoryResponse> = repository.uploadResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            repository.uploadStory(token, file, description)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }
}