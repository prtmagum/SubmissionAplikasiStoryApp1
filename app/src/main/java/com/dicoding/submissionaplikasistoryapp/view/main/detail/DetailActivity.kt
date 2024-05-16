package com.dicoding.submissionaplikasistoryapp.view.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submissionaplikasistoryapp.R
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityDetailBinding
import com.dicoding.submissionaplikasistoryapp.response.ListStoryItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupData()
    }

    private fun setupData() {
        val data = intent.getParcelableExtra<ListStoryItem>(EXTRA_DATA) as ListStoryItem
        binding.apply {
            tvNameDetail.text = data.name
            tvDescDetail.text = data.description
            Glide.with(this@DetailActivity)
                .load(data.photo)
                .fitCenter()
                .apply(
                    RequestOptions
                        .placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                ).into(ivStoryDetail)
        }
    }

    private fun setupView() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.story_detail)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}