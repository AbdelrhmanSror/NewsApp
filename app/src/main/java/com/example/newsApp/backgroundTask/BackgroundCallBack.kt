package com.example.newsApp.backgroundTask

import android.content.Context
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader

import com.example.newsApp.backgroundTask.BackgroundTask.NetworkObserver

/**
 * class for callback of loader to be used
 * @param <T>
</T> */
internal class BackgroundCallBack<T>(private val context: Context, private val observer: NetworkObserver<T>)
    : LoaderManager.LoaderCallbacks<T> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<T> {
        return BackgroundTask(context, observer)
    }

    override fun onLoadFinished(loader: Loader<T>, data: T) {
        observer.onDataReceived(data)

    }


    override fun onLoaderReset(loader: Loader<T>) {}


}
