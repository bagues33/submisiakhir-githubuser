package com.example.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.database.Favorite
import com.example.githubuser.database.FavoriteDao
import com.example.githubuser.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = favoriteDao.getAllFavorite()

    fun getUserFavoriteById(id: Int): LiveData<List<Favorite>> =
        favoriteDao.getUserFavoriteById(id)

    fun insert(favorite: Favorite) {
        executorService.execute { favoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { favoriteDao.delete(favorite) }
    }
}