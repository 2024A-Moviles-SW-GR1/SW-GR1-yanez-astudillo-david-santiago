package com.example.deber03

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val storiesRecyclerView = findViewById<RecyclerView>(R.id.rv_stories)
        val postsRecyclerView = findViewById<RecyclerView>(R.id.rv_posts)

        val stories = listOf(
            Story("John", R.drawable.story_image),
            Story("Jane", R.drawable.story_image),
            // Agrega más historias aquí
        )

        val posts = listOf(
            Post("John", R.drawable.post_image, "This is my new post!"),
            Post("Jane", R.drawable.post_image, "Check out this cool picture!"),
            // Agrega más publicaciones aquí
        )

        storiesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        storiesRecyclerView.adapter = StoriesAdapter(stories)

        postsRecyclerView.layoutManager = LinearLayoutManager(this)
        postsRecyclerView.adapter = PostsAdapter(posts)
    }
}