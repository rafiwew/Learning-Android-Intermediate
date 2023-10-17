package com.piwew.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.piwew.storyapp.R
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.api.response.ListStoryItem
import com.piwew.storyapp.databinding.ActivityMainBinding
import com.piwew.storyapp.ui.ViewModelFactory
import com.piwew.storyapp.ui.WelcomeActivity
import com.piwew.storyapp.ui.detail.StoryDetailActivity
import com.piwew.storyapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mAdapter = ListStoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModel.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showRecyclerView()
                        showViewModel(result.data.listStory)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
            }
            R.id.action_add_story -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
        binding.rvStories.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickCallback(object : ListStoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(stories: ListStoryItem) {
        val intentToDetail = Intent(this@MainActivity, StoryDetailActivity::class.java)
        intentToDetail.putExtra("STORY_ID", stories.id)
        startActivity(intentToDetail)
    }

    private fun showViewModel(storiesItem: List<ListStoryItem>) {
        if (storiesItem.isNotEmpty()) {
            binding.rvStories.visibility = View.VISIBLE
            mAdapter.submitList(storiesItem)
        } else {
            binding.rvStories.visibility = View.INVISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}