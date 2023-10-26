package com.piwew.storyapp.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.piwew.storyapp.R
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.api.response.Story
import com.piwew.storyapp.databinding.ActivityStoryDetailBinding
import com.piwew.storyapp.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    private val viewModel by viewModels<StoryDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivActionBack.setOnClickListener {
            onSupportNavigateUp()
        }

        storyId = intent.getStringExtra(STORY_ID).toString()
        viewModel.detailStory(storyId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showStoryDetail(result.data.story)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            } else {
                showToast(getString(R.string.empty_detail_story))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showStoryDetail(story: Story) {
        with(binding) {
            ivDetailPhoto.loadImage(story.photoUrl)
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            tvDetailDate.text = convertToIndonesiaTime(story.createdAt)
        }
    }

    private fun convertToIndonesiaTime(iso8601DateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputDate = SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale.getDefault())

        val timeZoneUTC = TimeZone.getTimeZone("UTC")
        val timeZoneIndonesia = TimeZone.getTimeZone("Asia/Jakarta")

        inputFormat.timeZone = timeZoneUTC

        return try {
            val dateCreated = inputFormat.parse(iso8601DateTime)
            dateCreated?.let {
                val calendar = Calendar.getInstance()
                calendar.time = dateCreated
                calendar.timeZone = timeZoneIndonesia
                return outputDate.format(calendar.time)
            } ?: "Invalid Date"
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