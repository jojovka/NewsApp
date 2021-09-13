package com.example.bestroadinvestment

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class ItemPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_page_activity)

        var newsPageImage: ImageView = findViewById(R.id.item_page_image_view)
        val newsPageTitle: TextView = findViewById(R.id.item_page_title_view)
        val newsPageDescription: TextView = findViewById(R.id.item_page_description_view)
        val newsPageDate: TextView = findViewById(R.id.item_page_date_view)

        val newsPageTitleString = intent.getStringExtra("Newstitle")
        val newsPageDescriptionString = intent.getStringExtra("Newsdescription")
        val newsPageDateString = intent.getStringExtra("Newsdate")
        val imageUrl = intent.getStringExtra("picture")

        newsPageTitle.text = newsPageTitleString.toString()
        newsPageDescription.text = newsPageDescriptionString.toString()
        newsPageDate.text = newsPageDateString.toString()
        Glide.with(this).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.image)
                .error(R.drawable.image)
                .fallback(R.drawable.image)
                .into(newsPageImage)

    }
}