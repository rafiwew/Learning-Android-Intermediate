package com.piwew.storyapp

import com.piwew.storyapp.data.database.entities.StoryEntity

object DataDummy {
    fun generateDummyStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()

        for (i in 0..100) {
            val story = StoryEntity(
                i.toString(),
                "name + $i",
                "desc + $i",
                "photoUrl  + $i",
                "createdAt + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}