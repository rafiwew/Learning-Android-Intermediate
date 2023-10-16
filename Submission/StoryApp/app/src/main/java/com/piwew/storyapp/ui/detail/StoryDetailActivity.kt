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