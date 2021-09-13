package com.example.bestroadinvestment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson

class FavouriteRecyclerViewAdapter(
        private var articles: List<Article>?, private val context: Context? = null
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Suppress("DEPRECATION")
    inner class NewsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var article: Article? = null
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
        val newsDate: TextView = itemView.findViewById(R.id.newsDate)
        val newsFavouriteButton: ImageButton = itemView.findViewById(R.id.favourites_button)

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, ItemPageActivity::class.java)
                intent.putExtra("Newstitle", article?.title)
                intent.putExtra("Newsdescription", article?.description)
                intent.putExtra("Newsdate", article?.publishedAt)
                intent.putExtra("picture", article?.urlToImage)
                context?.startActivity(intent)
            }
        }

        init {
            newsFavouriteButton.setOnClickListener {
                if (article == null) {
                    return@setOnClickListener
                }
                val MyPREFERENCES = "MyPrefs"
                var sharedpreferences: SharedPreferences? =
                        context?.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                val gson = Gson()

                val json: String? = sharedpreferences?.getString("MyObject", "")
                var obj: FavoriteNews
                if (json != null) {
                    obj = gson.fromJson(json!!, FavoriteNews::class.java)
                } else {
                    obj = FavoriteNews()
                    obj.articles = mutableListOf()
                }

                obj.articles?.add(article!!)

                val editor = sharedpreferences!!.edit()
                val json1: String = gson.toJson(obj)
                editor.putString("MyObject", json1)
                editor.commit()
            }
        }
    }

    internal fun refreshNewsItems(articles: List<Article>?) {
        val initialSize = this.articles?.size ?: 0

        this.articles = articles
        if (articles != null) {

            notifyItemRangeChanged(1, articles.size)
            if (articles.size < initialSize) {

                val sizeDifference = initialSize - articles.size
                notifyItemRangeRemoved(articles.size, sizeDifference)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {

            val v = parent.inflate(R.layout.news_item_layout)
            NewsItemViewHolder(v)
        } else {
            val v = parent.inflate(R.layout.news_item_layout)
            NewsItemViewHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return (1 + (articles?.size ?: 0))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position == 0) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
        if (position >= 1) {
            if (articles != null) {
                val idx = position - 1
                if (articles?.indices?.contains(idx) == true) {
                    val a = articles!![idx]
                    val newsItemViewHolder = (holder as NewsItemViewHolder)
                    newsItemViewHolder.newsTitle.text = a.title
                    newsItemViewHolder.newsDescription.text = a.description
                    newsItemViewHolder.newsDate.text = a.publishedAt
                    newsItemViewHolder.article = a
                    if (context != null) {
                        Glide.with(context).load(a.urlToImage)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .placeholder(R.drawable.image)
                                .error(R.drawable.image)
                                .fallback(R.drawable.image)
                                .into(newsItemViewHolder.newsImage)
                    }
                }
            }
        }
    }
}
