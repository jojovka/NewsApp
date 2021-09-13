package com.example.bestroadinvestment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson


class FavouriteActivity : AppCompatActivity() {

    private lateinit var favouriteRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        initializeRecyclerView()
    }


    private fun initializeRecyclerView() {

        favouriteRecyclerView = findViewById(R.id.favourite_recycler_view)
        val favoriteRecyclerViewAdapter = FavouriteRecyclerViewAdapter(null, this)

        favouriteRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL, false
            )
            adapter = favoriteRecyclerViewAdapter
        }
        fetchFavItems()
    }

    private fun fetchFavItems() {
        val MyPREFERENCES = "MyPrefs"
        var sharedpreferences: SharedPreferences? =
                this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        val gson = Gson()

        val json: String? = sharedpreferences?.getString("MyObject", "")
        var obj: FavoriteNews
        if (json != null) {
            obj = gson.fromJson(json!!, FavoriteNews::class.java)
        } else {
            obj = FavoriteNews()
            obj.articles = mutableListOf()
        }

        val c = obj.articles?.count()

        val adapter = favouriteRecyclerView.adapter as FavouriteRecyclerViewAdapter
        adapter.refreshNewsItems(obj.articles)
    }
}