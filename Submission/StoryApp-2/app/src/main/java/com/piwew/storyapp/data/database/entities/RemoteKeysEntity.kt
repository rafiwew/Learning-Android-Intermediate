package com.piwew.storyapp.data.database.entities

import androidx.room.*

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
