package com.example.githubuser.ui.follow


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.ApiConfig
import com.example.githubuser.ItemsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followers = MutableLiveData<ArrayList<ItemsItem>?>(null)
    val followers: LiveData<ArrayList<ItemsItem>?> = _followers

    private val _following = MutableLiveData<ArrayList<ItemsItem>?>(null)
    val following: LiveData<ArrayList<ItemsItem>?> = _following

    init {
        Log.i(TAG, "FollFragment is Created")

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
                        }
                        _isLoading.value = false

                    }

                    override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                        Log.e(TAG, t.message.toString())

                        _following.value = arrayListOf()
                        _isLoading.value = false
                    }

                })
            }
    }


    companion object {
        private const val TAG = "FollowViewModel"
        private const val TOKEN = "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"
    }
}