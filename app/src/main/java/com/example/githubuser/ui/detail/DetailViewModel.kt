package com.example.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.ApiConfig
import com.example.githubuser.database.Favorite
import com.example.githubuser.model.DetailUserResponse
import com.example.githubuser.repository.FavoriteRepository
import com.example.githubuser.ui.follow.FollowViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(app: Application) : ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(app)

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _detailUser = MutableLiveData<DetailUserResponse?>(null)
    val isUser: LiveData<DetailUserResponse?> = _detailUser

    private val _callCounter = MutableLiveData(0)
    val callCounter: LiveData<Int> = _callCounter

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        Log.i(TAG, "DetailViewModel is Created")
    }

    fun insert(favEntity: Favorite) {
        mFavoriteRepository.insert(favEntity)
    }

    fun delete(favEntity: Favorite) {
        mFavoriteRepository.delete(favEntity)
    }

    fun getFavoriteById(id: Int): LiveData<List<Favorite>> {
        return mFavoriteRepository.getUserFavoriteById(id)
    }
    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(TOKEN, username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _detailUser.value = response.body()
//                } else {
//                    Log.e(TAG, "onFailure: anjing")
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
                if (response.isSuccessful) _detailUser.value = response.body()
                else Log.e(TAG, "onFailure: ${response}")
                _isLoading.value = false
                _isError.value = false
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {
        private const val TAG = "DetailViewModel"
        private const val TOKEN = "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"
    }

}