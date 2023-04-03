package com.example.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.FollowFragment
import com.example.githubuser.FollowFragment.Companion.ARG_POSITION
import com.example.githubuser.FollowFragment.Companion.ARG_USERNAME

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

              return  FollowFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, position + 1)
                        putString(ARG_USERNAME, username)
                    }
                }
    }

}