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
import com.piwew.storyapp.data.database.entities.StoryEntity
import com.piwew.storyapp.databinding.ActivityMainBinding
import com.piwew.storyapp.ui.ViewModelFactory
import com.piwew.storyapp.ui.WelcomeActivity
import com.piwew.storyapp.ui.detail.StoryDetailActivity
import com.piwew.storyapp.ui.main.adapter.ListStoriesAdapter
import com.piwew.storyapp.ui.main.adapter.LoadingStateAdapter
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

        setUpRecyclerView()
        setMenuItemClickListener()
        onAddStoryButtonClick()
        checkUserLoginAndRedirect()
    }

    private fun checkUserLoginAndRedirect() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                getData()
            }
        }
    }

    private fun setMenuItemClickListener() {
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
    }

    private fun onAddStoryButtonClick() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setUpRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
        binding.rvStories.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickCallback(object : ListStoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryEntity?) {
                if (data != null) {
                    showSelectedStory(data)
                }
            }
        })
    }

    private fun showSelectedStory(stories: StoryEntity) {
        val intentToDetail = Intent(this@MainActivity, StoryDetailActivity::class.java)
        intentToDetail.putExtra("STORY_ID", stories.id)
        startActivity(intentToDetail)
    }

    private fun getData() {
        mAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { mAdapter.retry() }
        )
        viewModel.stories.observe(this) { data ->
            if (data != null) {
                mAdapter.submitData(lifecycle, data)
                showLoading(false)
            } else {
                showLoading(true)
                showToast(getString(R.string.empty_story))
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}