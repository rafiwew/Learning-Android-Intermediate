package com.piwew.storyapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.piwew.storyapp.R
import com.piwew.storyapp.data.api.response.ListStoryItem
import com.piwew.storyapp.data.api.retrofit.ApiConfig
import com.piwew.storyapp.data.pref.UserPreference
import com.piwew.storyapp.data.pref.dataStore
import com.piwew.storyapp.widget.StoriesWidget.Companion.EXTRA_ITEM
import com.piwew.storyapp.widget.StoriesWidget.Companion.STORY_ID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StackRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    private val storiesBitmap = ArrayList<Bitmap>()
    private val stories = ArrayList<ListStoryItem>()

    override fun onDataSetChanged(): Unit = runBlocking {
        try {
            val user =
                runBlocking { UserPreference.getInstance(context.dataStore).getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val response = apiService.getStories()
            val storyList = response.listStory
            val storiesBitmapList = storyList.map {
                Glide.with(context)
                    .asBitmap()
                    .load(it.photoUrl)
                    .submit()
                    .get()
            }

            storiesBitmap.clear()
            stories.clear()
            storiesBitmap.addAll(storiesBitmapList)
            stories.addAll(storyList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.stories_widget_item)
        rv.setImageViewBitmap(R.id.imageView, storiesBitmap[position])

        val fillInIntent = Intent().apply {
            action = EXTRA_ITEM
            putExtra(STORY_ID, stories[position].id)
        }
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)

        return rv
    }

    override fun onCreate() {}
    override fun onDestroy() {}
    override fun getCount(): Int = stories.size
    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(i: Int): Long = 0
    override fun hasStableIds(): Boolean = false
}