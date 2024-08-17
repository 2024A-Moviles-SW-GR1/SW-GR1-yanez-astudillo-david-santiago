package com.example.deber03

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostsAdapter (private val postsList: List<Post>) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameView: TextView = view.findViewById(R.id.tv_post_username)
        val imageView: ImageView = view.findViewById(R.id.iv_post_image)
        val captionView: TextView = view.findViewById(R.id.tv_post_caption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList[position]
        holder.usernameView.text = post.username
        holder.imageView.setImageResource(post.imageResId)
        holder.captionView.text = post.caption
    }

    override fun getItemCount() = postsList.size
}