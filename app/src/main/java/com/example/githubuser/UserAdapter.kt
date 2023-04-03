package com.example.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.githubuser.databinding.ItemUserBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class UserAdapter(private val listUser: ArrayList<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    /**
     * Set an item click callback
     *
     * @param onItemClickCallback   object that implements onItemClickCallback
     * @return Unit
     */
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.apply {
            listName.text = user.login
            listThumbnail.setImageGlide(holder.itemView.context, user.avatarUrl)
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(user: ItemsItem)
    }
}

private fun CircleImageView.setImageGlide(context: Context, url: String) {
    Glide
        .with(context)
        .load(url)
        .placeholder(R.drawable.messi)
        .into(this)
}


