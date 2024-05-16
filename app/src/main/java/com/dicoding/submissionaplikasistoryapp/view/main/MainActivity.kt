package com.dicoding.submissionaplikasistoryapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionaplikasistoryapp.R
import com.dicoding.submissionaplikasistoryapp.adapter.ListStoryAdapter
import com.dicoding.submissionaplikasistoryapp.adapter.LoadingStateAdapter
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityMainBinding
import com.dicoding.submissionaplikasistoryapp.view.ViewModelFactory
import com.dicoding.submissionaplikasistoryapp.view.upload.AddStoryActivity
import com.dicoding.submissionaplikasistoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var storyAdapter: ListStoryAdapter
    private var token = ""
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupViewModel()
        setupAdapter()
        setupAction()
        setupUser()
    }

    private fun setupAction() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupUser() {
        showLoading(true)
        mainViewModel.getSession().observe(this@MainActivity) { session ->
            token = session.token
            if (!session.isLogin) {
                moveActivity()
            } else {
                val userName = session.name
                val helloUserText = getString(R.string.username, userName)
                supportActionBar?.title = helloUserText
                setupData()
                showLoading(false)
            }
        }
        showToast()
    }

    private fun setupData() {
        mainViewModel.getListStories.observe(this@MainActivity) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun setupAdapter() {
        storyAdapter = ListStoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private fun setupView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.apply {
                shimmerLoading.visibility = View.VISIBLE
                shimmerLoading.startShimmer()
                rvStories.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                rvStories.visibility = View.VISIBLE
                shimmerLoading.stopShimmer()
                shimmerLoading.visibility = View.INVISIBLE
            }
        }
    }

    private fun showToast() {
        mainViewModel.toastText.observe(this@MainActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@MainActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                mainViewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}