package com.example.newsApp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.example.newsApp.R
import com.example.newsApp.databinding.NewsListLayoutBinding
import com.example.newsApp.model.NewsModel

import java.util.ArrayList

import android.view.ViewGroup.LayoutParams
import androidx.core.view.children
import com.example.newsApp.ui.NewsListAdapter.ViewHolder.Companion.from

/**
 * adapter class for presentation of data in recycler view
 */
class NewsListAdapter(private val list: List<NewsModel>?, private val listener: OnClickListener)
    : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    class ViewHolder(val binding: NewsListLayoutBinding, val context: Context)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsModel: NewsModel) {
            binding.newsModel = newsModel
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = NewsListLayoutBinding.inflate(inflater)
                return ViewHolder(binding, parent.context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list!![position])
        holder.itemView.setOnClickListener { listener.onCardClick(list[position].webUrl) }
        holder.binding.authorName.setOnClickListener { handleAuthorTextView(holder, list) }

    }


    override fun getItemCount(): Int {

        return list?.size ?: 0
    }

    //call back to be implemented when item in recycler view is clicked
    interface OnClickListener {
        fun onCardClick(webUrl: String)
    }


    private companion object {
        private fun createAuthorTextView(authors: ArrayList<String>, holder: ViewHolder) {
            for (author in authors) {
                val textView = TextView(holder.context)
                val params = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                textView.text = author
                textView.setTextAppearance(holder.context, R.style.TextAppearance_AppCompat_Light_SearchResult_Subtitle)
                textView.layoutParams = params
                textView.setTextColor(ContextCompat.getColor(holder.context, R.color.white))
                holder.binding.authorContainer.addView(textView)
            }
        }

        private fun setAuthorVisibility(authorContainer: LinearLayout) {
            when {
                authorContainer.getChildAt(1).visibility == View.VISIBLE -> authorContainer.children.forEach {
                    it.visibility = View.GONE

                }
                else -> authorContainer.children.forEach {
                    it.visibility = View.VISIBLE

                }
            }
        }
        //here we create at first time a new text view if the the object fetched from api is containing author names
        // the second time we we check the visibility of text view that was created if it was visible when user click on it then make it invisible
        //else make it visible and so  on
        fun handleAuthorTextView(holder: ViewHolder, list: List<NewsModel>?) {
            //this list of authors that is existed in the clicked object.
            val authors = list!![holder.adapterPosition].getAuthors()
            //check if there is authors or not the object
            if (authors.isNotEmpty()) {
                val authorContainer = holder.binding.authorContainer
                //this is going to be executed when thee is text vie already created before, if it was then we move on to set the visibility
                when {
                    authorContainer.childCount > 1 -> setAuthorVisibility(authorContainer)
                    else -> createAuthorTextView(authors, holder)
                }
            }
        }
    }
}
