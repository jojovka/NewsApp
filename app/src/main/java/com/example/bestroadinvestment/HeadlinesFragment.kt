package com.example.bestroadinvestment

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class HeadlinesFragment : Fragment(), NewsFetchedListener, SearchViewOnChangeListener {

    private lateinit var recyclerView: RecyclerView
    var isLoading: Boolean = false
    var page = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) =
            inflater.inflate(R.layout.fragment_headlines, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeRecyclerView()
        fetchNewsItems(page)
    }

    private fun initializeRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recycler_view)
        val recyclerViewAdapter = RecyclerViewAdapter(null, context, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL, false
            )
            adapter = recyclerViewAdapter
        }
        setupOnScrollListener()
    }

    private fun setupOnScrollListener() {
        recyclerView!!.addOnScrollListener(object :
                PaginationScrollListener(recyclerView!!.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return false
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                fetchNewsItems(page)
            }
        })
    }

    private fun fetchNewsItems(page: Int = 0) {
        isLoading = true
        val task = NewsFetchingAsyncTask("", page, this)

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    override fun whenHeadlinesNewsFetchedSuccessfully(articles: List<Article>?) {
        val adapter = recyclerView.adapter as RecyclerViewAdapter
        adapter.refreshNewsItems(articles)
        isLoading = false
        page++
    }

    override fun whenNewsFetchedOnError(error: String?) {
        isLoading = false
    }

    override fun searchViewOnQueryTextSubmit(text: String?) {

    }
}