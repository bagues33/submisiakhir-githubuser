package com.example.githubuser

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.detail.DetailUserActivity
import com.example.githubuser.ui.main.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FollowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFollowBinding.inflate(layoutInflater, container, false)

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
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

            mainViewModel.followers.observe(viewLifecycleOwner) { followers ->
                if (followers == null) {
                    val username = arguments?.getString(ARG_USERNAME) ?: ""
                    mainViewModel.getUserFollowers(username)
                } else {
                    showFollow(followers)
                }
            }
        } else {

            mainViewModel.following.observe(viewLifecycleOwner) { following ->
                if (following == null) {
                    val username = arguments?.getString(ARG_USERNAME) ?: ""
                    mainViewModel.getUserFollowing(username)
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
//        const val  ARG_SECTION_NUMBER= "section_number"
    }
}