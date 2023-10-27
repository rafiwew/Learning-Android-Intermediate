package com.piwew.storyapp.data.database

import android.content.Context
import androidx.room.*
import com.piwew.storyapp.data.database.dao.RemoteKeysDao
import com.piwew.storyapp.data.database.dao.StoryDao
import com.piwew.storyapp.data.database.entities.RemoteKeysEntity
import com.piwew.storyapp.data.database.entities.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}