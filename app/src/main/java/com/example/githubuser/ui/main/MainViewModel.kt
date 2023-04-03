package com.example.githubuser.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.ApiConfig
import com.example.githubuser.ItemsItem
import com.example.githubuser.SearchUserResponse
import com.example.githubuser.database.Favorite
import com.example.githubuser.model.DetailUserResponse
import com.example.githubuser.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val _detailUser = MutableLiveData<DetailUserResponse?>(null)
    val isUser: LiveData<DetailUserResponse?> = _detailUser

    private val _callCounter = MutableLiveData(0)
    val callCounter: LiveData<Int> = _callCounter

    private val _followers = MutableLiveData<ArrayList<ItemsItem>?>(null)
    val followers: LiveData<ArrayList<ItemsItem>?> = _followers

    private val _following = MutableLiveData<ArrayList<ItemsItem>?>(null)
    val following: LiveData<ArrayList<ItemsItem>?> = _following

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        findUSer("bagus")
        Log.i(TAG, "DetailViewModel is Created")
//        viewModelScope.launch { findUSer("bagus") }
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

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(TOKEN, username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                _isError.value = false
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserFollowers(TOKEN, username)
            .apply {
                enqueue(object : Callback<ArrayList<ItemsItem>> {
                    override fun onResponse(
                        call: Call<ArrayList<ItemsItem>>,
                        response: Response<ArrayList<ItemsItem>>
                    ) {
                        if (response.isSuccessful) _followers.value = response.body()
                        else Log.e(TAG, response.message())
                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                        Log.e(TAG, t.message.toString())

                        _followers.value = arrayListOf()
                        _isLoading.value = false
                    }

                })
            }
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserFollowing(TOKEN, username)
            .apply {
                enqueue(object : Callback<ArrayList<ItemsItem>> {
                    override fun onResponse(
                        call: Call<ArrayList<ItemsItem>>,
                        response: Response<ArrayList<ItemsItem>>
                    ) {
                        if (response.isSuccessful)  {
                            _following.value = response.body()
                            Log.e(TAG, response.body().toString())
                        } else {
                            Log.e(TAG, response.message())
                            _isLoading.value = false
                        }

                    }

                    override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                        Log.e(TAG, t.message.toString())

                        _following.value = arrayListOf()
                        _isLoading.value = false
                    }

                })
            }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object{
        private const val TAG = "MainViewModel"
        private const val TOKEN = "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"
    }

}

