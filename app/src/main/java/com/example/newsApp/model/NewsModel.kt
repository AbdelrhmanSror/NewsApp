package com.example.newsApp.model

import java.util.ArrayList

/**
 * the model class that represent data coming from network
 */
class NewsModel(val headLine: String
                , val date: String
                , val publication: String
                , val sectionName: String
                , private val authors: ArrayList<String>
                , val webUrl: String) {

    fun getAuthors(): ArrayList<String> {
        return formatAuthorString()
    }

    //author name is received from api in a format ("author/name")so we just want to take the name and remove the rest
    private fun formatAuthorString(): ArrayList<String> {
        return if (authors.isNotEmpty()) {
            authors.map {
                it.substring(7)
            } as ArrayList<String>

        } else
            authors

    }
}
