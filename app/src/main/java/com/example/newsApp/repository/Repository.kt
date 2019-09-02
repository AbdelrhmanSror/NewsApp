package com.example.newsApp.repository

import android.content.Context

import androidx.loader.app.LoaderManager

import com.example.newsApp.backgroundTask.BackgroundTask
import com.example.newsApp.backgroundTask.BackgroundTask.NetworkObserver
import com.example.newsApp.model.NewsModel

import java.util.ArrayList

class Repository(private val context: Context) {

    fun startBackground(manager: LoaderManager, observer: NetworkObserver<ArrayList<NewsModel>>) {
        BackgroundTask.Builder<ArrayList<NewsModel>>()
                .addObserver(observer)
                .addContext(context)
                .addLoader(manager).initLoader()
    }


}
