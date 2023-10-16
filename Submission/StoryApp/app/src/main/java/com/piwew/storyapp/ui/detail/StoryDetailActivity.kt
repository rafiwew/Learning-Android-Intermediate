package com.piwew.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.api.response.Story
import com.piwew.storyapp.databinding.ActivityStoryDetailBinding
import com.piwew.storyapp.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    private val viewModel by viewModels<StoryDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyId = intent.getStringExtra(STORY_ID).toString()
        viewModel.detailStory(storyId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showViewModel(result.data.story)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showViewModel(story: Story) {
        with(binding) {
            ivPhotoDetail.loadImage(story.photoUrl)
            tvNameDetail.text = story.name
            tvDescDetail.text = story.description
            tvDateDetail.text = formatDateTime(story.createdAt)
        }
    }

    private fun formatDateTime(iso8601DateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputDate = SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale.getDefault())

        return try {
            val date = inputFormat.parse(iso8601DateTime)
            outputDate.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "There is an error: ${e.message}", Toast.LENGTH_SHORT).show()
            "Invalid Date"
        }
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .fitCenter()
            .skipMemoryCache(true)
            .into(this)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val STORY_ID = "STORY_ID"
        var storyId = String()
    }
}