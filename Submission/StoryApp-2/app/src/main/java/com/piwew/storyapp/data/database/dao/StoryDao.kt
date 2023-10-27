package com.piwew.storyapp.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.piwew.storyapp.data.database.entities.StoryEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(story: List<StoryEntity>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAllStories()
}