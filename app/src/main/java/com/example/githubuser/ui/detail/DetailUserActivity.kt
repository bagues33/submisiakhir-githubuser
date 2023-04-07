package com.example.githubuser.ui.detail

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.ViewModelFactory
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.database.Favorite
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.model.DetailUserResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView


class DetailUserActivity : AppCompatActivity(), View.OnClickListener{

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val KEY_ID = "key_id"
        const val KEY_AVATAR = "key_avatar"
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.followers_fragment,
            R.string.following_fragment
        )
    }

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!

    private var username: String? = null
    private var profileUrl: String? = null

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        username = intent.extras?.get(EXTRA_DETAIL) as String

        setContentView(binding.root)
        setViewPager()
        setToolbar()

        val favorite = Favorite()
        favorite.login = username
        favorite.id = intent.getIntExtra(KEY_ID, 0)
        favorite.avatar_url = intent.getStringExtra(KEY_AVATAR)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username!!)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val detailViewModel: DetailViewModel by viewModels {
            ViewModelFactory(application)
        }

        detailViewModel.isUser.observe(this) { isUser ->
            if (isUser != null) {
                parseUserDetail(isUser)
                profileUrl = isUser.htmlUrl
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isError.observe(this) { error ->
            if (error) errorOccurred()
        }

        detailViewModel.callCounter.observe(this) { counter ->
            if (counter < 1) detailViewModel.getDetailUser(username!!)
        }

        binding.btnOpen.setOnClickListener(this)

        detailViewModel.getFavoriteById(favorite.id!!)
            .observe(this@DetailUserActivity, { listFavorite ->
                isFavorite = listFavorite.isNotEmpty()

                binding.detailFabFavorite.imageTintList = if (listFavorite.isEmpty()) {
                    ColorStateList.valueOf(Color.rgb(255, 255, 255))
                } else {
                    ColorStateList.valueOf(Color.rgb(247, 106, 123))
                }

            })

        binding.detailFabFavorite.apply {
            setOnClickListener {
                if (isFavorite) {
                    detailViewModel.delete(favorite)
                    Toast.makeText(
                        this@DetailUserActivity,
                        "${favorite.login} telah dihapus dari data User Favorite ",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    detailViewModel.insert(favorite)
                    Toast.makeText(
                        this@DetailUserActivity,
                        "${favorite.login} telah ditambahkan ke data User Favorite",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_open -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(profileUrl)
                }.also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        username = null
        profileUrl = null

        super.onDestroy()
    }

    fun TextView.setAndVisible(text: String?) {
        if (!text.isNullOrBlank()) {
            this.text = text
            this.visibility = View.VISIBLE
        }
    }

    private fun CircleImageView.setImageGlide(context: Context, url: String) {
        Glide
            .with(context)
            .load(url)
            .placeholder(R.drawable.messi)
            .into(this)
    }

    private fun errorOccurred() {
        binding.apply {
            userDetailContainer.visibility = View.INVISIBLE
            viewPager.visibility = View.INVISIBLE
        }
        Toast.makeText(this@DetailUserActivity, "An Error is Occurred", Toast.LENGTH_SHORT).show()
    }

    private fun setToolbar() {
        binding.collapsingToolbar.isTitleEnabled = false
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.profile)
        }
    }

    private fun setViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
    }

    private fun parseUserDetail(user: DetailUserResponse) {
        binding.apply {
            detailUsername.text = user.login
            detailRepositories.text = user.publicRepos.toString()
            detailFollowers.text = user.followers.toString()
            detailFollowing.text = user.following.toString()

            detailName.setAndVisible(user.name)
            detailBio.setAndVisible(user.bio)
            detailCompany.setAndVisible(user.company)
            detailLocation.setAndVisible(user.location)
            detailWeb.setAndVisible(user.blog)

            detailAvatar.setImageGlide(this@DetailUserActivity, user.avatarUrl)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                pbLoading.visibility = View.VISIBLE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                pbLoading.visibility = View.GONE
                appBarLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
            }
        }
    }

}

