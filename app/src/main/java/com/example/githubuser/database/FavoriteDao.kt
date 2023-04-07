package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT  * from tb_favorite")
    fun getAllFavorite(): LiveData<List<Favorite>>

    @Query("SELECT  * from tb_favorite WHERE id = :id")
    fun getUserFavoriteById(id: Int): LiveData<List<Favorite>>
}