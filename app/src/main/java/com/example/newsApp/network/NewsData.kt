package com.example.newsApp.network

import com.example.newsApp.model.NewsModel

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class NewsData private constructor() {
    private var json: String? = null


    /**
     * after fetching json string we extract the info what we need using Json Lib
     *
     * @return arrayList of NewsModel to use it to make list of news
     */
    val news: ArrayList<NewsModel>
        get() {
            var headLine: String
            var publication: String
            var date: String
            var sectionName: String
            var webUrl: String
            var authors: ArrayList<String>
            val data = ArrayList<NewsModel>()
            if (json != null) {
                try {
                    val root = JSONObject(json!!)
                    val response = root.getJSONObject("response")
                    val results = response.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val `object` = results.getJSONObject(i)
                        sectionName = `object`.getString("sectionName")
                        webUrl = `object`.getString("webUrl")
                        date = `object`.getString("webPublicationDate")
                        val fields = `object`.getJSONObject("fields")
                        headLine = fields.getString("headline")
                        publication = fields.getString("publication")
                        val references = `object`.getJSONArray("references")
                        authors = getAuthor(references)
                        data.add(NewsModel(headLine, date, publication, sectionName, authors, webUrl))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return data
        }

    class Builder {
        private val newsData = NewsData()

        fun startFetching(orderVal: String): NewsData {
            val httpLib = HttpLib.HttpBuilder()
                    .addUrl("https://content.guardianapis.com/books")
                    .addQuery(FORMAT, "json")
                    .addQuery(SHOW_FIELD, "publication,headline")
                    .addQuery(ORDER_BY, orderVal)
                    .addQuery(API_KEY, /*BuildConfig.MY_NEWS_API_KEY*/"73295c83-5863-4d8d-be28-41838de1f711")
                    .addQuery(SHOW_REFERENCES, "author").build()
            newsData.json = httpLib!!.startFetching()
            return newsData
        }
    }

    @Throws(JSONException::class)
    private fun getAuthor(references: JSONArray): ArrayList<String> {
        val authors = ArrayList<String>()
        //if no author return null
        for (i in 0 until references.length()) {
            val `object` = references.getJSONObject(i)
            authors.add(`object`.getString("id"))
        }
        return authors
    }

    companion object {
        private val FORMAT = "format"
        private val SHOW_FIELD = "show-fields"
        private val ORDER_BY = "order-by"
        private val API_KEY = "api-key"
        private val SHOW_REFERENCES = "show-references"
    }
}

