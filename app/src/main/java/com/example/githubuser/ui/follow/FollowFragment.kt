package com.example.githubuser.ui.follow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.ItemsItem
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.detail.DetailUserActivity

/**
 * A simple [Fragment] subclass.
 * Use the [FollowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFollowBinding.inflate(layoutInflater, container, false)

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION, 0)
//        val username = arguments?.getString(ARG_USERNAME) ?: ""
        if (position == 1) {

            followViewModel.followers.observe(viewLifecycleOwner) { followers ->
                if (followers == null) {
                    val username = arguments?.getString(ARG_USERNAME) ?: ""
                    followViewModel.getUserFollowers(username)
                } else {
                    showFollow(followers)
                }
            }
        } else {

            followViewModel.following.observe(viewLifecycleOwner) { following ->
                if (following == null) {
                    val username = arguments?.getString(ARG_USERNAME) ?: ""
                    followViewModel.getUserFollowing(username)
                } else {
                    showFollow(following)
                }
            }

        }
    }

    private fun showFollow(users: ArrayList<ItemsItem>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = UserAdapter(users)

            binding.rvUser.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object :
                UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: ItemsItem) {
                    goToDetailUser(user)
                }

            })
        } else binding.tvStatus.visibility = View.VISIBLE
    }

    private fun goToDetailUser(user: ItemsItem) {
        Intent(activity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_DETAIL, user.login)
            putExtra(DetailUserActivity.KEY_ID, user.id)
            putExtra(DetailUserActivity.KEY_AVATAR, user.avatarUrl)
        }.also {
            startActivity(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.dataLoading.visibility = View.VISIBLE
        else binding.dataLoading.visibility = View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}