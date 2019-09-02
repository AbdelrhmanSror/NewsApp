package com.example.newsApp

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.newsApp.model.NewsModel
import com.example.newsApp.ui.NewsListAdapter

import java.util.ArrayList

    //binding adapter for the author because most of data does not have author name
    @BindingAdapter("textVisibility")
    fun textVisibility(textView: TextView, name: ArrayList<String>?) {
        when {
            name.isNullOrEmpty() -> textView.visibility = View.GONE
            else -> textView.visibility = View.VISIBLE
        }
    }

    @BindingAdapter("adapter")
    fun bindRecyclerView(recyclerView: RecyclerView, newsModelList: List<NewsModel>?) {
        val newsListAdapter = NewsListAdapter(newsModelList, object :NewsListAdapter.OnClickListener{
            override fun onCardClick(webUrl: String) {
                //sending intent to open web page using the url provided by api
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(webUrl)
                recyclerView.context.startActivity(i)
            }
        })
        recyclerView.adapter = newsListAdapter
    }

    @BindingAdapter("swipeEnabled")
    fun swipeEnabled(refreshLayout: SwipeRefreshLayout, enable: Boolean) {
        refreshLayout.isRefreshing = enable
    }

    @BindingAdapter("noDataRetrieved")
    fun noDataRetrieved(textView: TextView, enabled: Boolean) {
        when {
            enabled -> textView.visibility = View.VISIBLE
            else -> textView.visibility = View.GONE
        }

    }

