package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.Favorite
import com.example.githubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository : FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorites() : LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()
}