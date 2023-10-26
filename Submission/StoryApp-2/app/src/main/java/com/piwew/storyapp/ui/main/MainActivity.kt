package com.piwew.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.piwew.storyapp.R
import com.piwew.storyapp.data.api.response.ListStoryItem
import com.piwew.storyapp.databinding.ActivityMainBinding
import com.piwew.storyapp.ui.ViewModelFactory
import com.piwew.storyapp.ui.WelcomeActivity
import com.piwew.storyapp.ui.detail.StoryDetailActivity
import com.piwew.storyapp.ui.maps.MapsActivity
import com.piwew.storyapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mAdapter = ListStoriesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.logout_title))
                        setMessage(getString(R.string.logout_message))
                        setPositiveButton(getString(R.string.yes_title)) { _, _ ->
                            viewModel.logout()
                            showToast(getString(R.string.logout_success))
                        }
                        setNegativeButton(getString(R.string.no_title)) { _, _ ->
                        }
                        create()
                        show()
                    }
                    true
                }

                R.id.action_setting -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                R.id.action_maps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }

                else -> false
            }
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.stories.observe(this) {
                    getData()
                }
            }
        }

        showRecyclerView()
    }

    private fun showRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
        binding.rvStories.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickCallback(object : ListStoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem?) {
                if (data != null) {
                    showSelectedStory(data)
                }
            }
        })
    }

    private fun showSelectedStory(stories: ListStoryItem) {
        val intentToDetail = Intent(this@MainActivity, StoryDetailActivity::class.java)
        intentToDetail.putExtra("STORY_ID", stories.id)
        startActivity(intentToDetail)
    }

    private fun getData() {
        viewModel.stories.observe(this) { data ->
            mAdapter.submitData(lifecycle, data)
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}