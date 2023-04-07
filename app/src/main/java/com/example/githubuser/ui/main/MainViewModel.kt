package com.example.githubuser.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.ApiConfig
import com.example.githubuser.ItemsItem
import com.example.githubuser.SearchUserResponse
import com.example.githubuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(app: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(app)

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _itemsItem = MutableLiveData<ArrayList<ItemsItem>>()
    val itemsItem: LiveData<ArrayList<ItemsItem>> = _itemsItem

    init {
        findUSer("bagus")
        Log.i(TAG, "DetailViewModel is Created")
    }


    fun findUSer(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsername(TOKEN, query)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _itemsItem.value = response.body()?.items as ArrayList<ItemsItem>?
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                _isError.value = false
            }
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _itemsItem.value = arrayListOf()
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object{
        private const val TAG = "MainViewModel"
        private const val TOKEN = "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"
    }

}

