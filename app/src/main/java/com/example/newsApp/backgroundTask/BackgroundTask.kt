package com.example.newsApp.backgroundTask

import android.content.Context
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
/**
 * class for doing background work using asyncTaskLoader easily without bothering our self
 * with initialization of loader and implementation of call back
 * just provide an instance of loaderManger and context and observer which has two method
 *
 */

class BackgroundTask<T> internal constructor(context: Context, //call back to be implemented when data is ready
                                             private val observer: NetworkObserver<T>) : AsyncTaskLoader<T>(context) {
    private var mData: T? = null

     class Builder<T> {
        private lateinit var observer: NetworkObserver<T>
        private  var manager: LoaderManager?=null
        private lateinit var context: Context

        fun addObserver(observer: NetworkObserver<T>): Builder<*> {
            this.observer = observer
            return this
        }

        fun addLoader(manager: LoaderManager): Builder<*> {
            this.manager = manager
            return this
        }

        fun addContext(context: Context): Builder<*> {
            this.context = context
            return this
        }

        /**
         *
         *
         * this method will create a new loader,if there is already one before it will use the last one.
         *
         */
        fun initLoader() {
            // Get our Loader by calling getLoader and passing the ID we specified
            val loader = manager!!.getLoader<String>(0)
            if (loader == null) {
                manager!!.initLoader(0, null, BackgroundCallBack(context, observer))
            } else {
                manager!!.restartLoader(0, null, BackgroundCallBack(context, observer))
            }
        }

    }

    override fun loadInBackground(): T? {
        mData = observer.startBackgroundTask()
        return mData
    }

    override fun onStartLoading() {
        when {
            observer.onPreExecuteBackground()!! -> cancelLoad()
            mData != null -> // Use already saved data
                deliverResult(mData)
            else -> forceLoad()
        }
    }

    override fun deliverResult(data: T?) {
        // We’ll save the data for later
        mData = data
        super.deliverResult(data)
    }

    interface NetworkObserver<T> {
        /**
         * this method is called before starting any Background task
         *
         * @return true if you want to stop the background or false if you want to precede
         */
        fun onPreExecuteBackground(): Boolean?

        /**
         * doing whatever background task u want to do here
         *
         *
         * this works on background thread
         *
         *
         * @return the result of the background task
         */
        fun startBackgroundTask(): T

        /**
         * here you can get the result of background task and do whatever you want with it
         *
         *
         * this works on main thread
         *
         *
         * @param data received from background task
         */
        fun onDataReceived(data: T)
    }

}
