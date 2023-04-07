package com.example.githubuser.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.database.Favorite
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.detail.DetailUserActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {
    private val userFavorites = ArrayList<Favorite>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userFavorites[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(userFavorites[position]) }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListFavorite(items: List<Favorite>) {
        userFavorites.clear()
        userFavorites.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = userFavorites.size
    class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favEntity: Favorite) {
            with(binding) {
                listName.text = favEntity.login
                Glide.with(root)
                    .load(favEntity.avatar_url)
                    .circleCrop()
                    .into(binding.listThumbnail)
                root.setOnClickListener {
                    val intent = Intent(itemView.context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_DETAIL, favEntity)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(favEntity: Favorite)
    }
}