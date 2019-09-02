package com.example.newsApp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.loader.app.LoaderManager
import com.example.newsApp.backgroundTask.BackgroundTask.NetworkObserver
import com.example.newsApp.model.NewsModel
import com.example.newsApp.network.NewsData
import com.example.newsApp.repository.Repository

import java.util.ArrayList
import java.util.Objects

class NewsViewModel(application: Application, private val repository: Repository):AndroidViewModel(application){
    //to observer the changes that happen to the data
    private val _news = MutableLiveData<List<NewsModel>>()
    val news: LiveData<List<NewsModel>> = _news
    //to handle re fetching data when user swipe also to keep the swiping mark running
    private val _swipeEnabled = MutableLiveData<Boolean>()
    val swipeEnabled: LiveData<Boolean> = _swipeEnabled
    //to handle if the data retrieved from server is null then show no Data message.
    private val _noDataTextEnabled = MutableLiveData<Boolean>()
    val noDataTextEnabled: LiveData<Boolean> = _noDataTextEnabled

    //to handle showing snackBar if no internet connection
    private val _snackBarEnabled = MutableLiveData<Boolean>()
    val snackBarEnabled: LiveData<Boolean> = _snackBarEnabled

    //to handle when to start the background task
    private val _onBackgroundTaskStarted = MutableLiveData<Boolean>()
    val onBackgroundTaskStarted: LiveData<Boolean> = _onBackgroundTaskStarted

    //checking if network is available or not
    private val isNetworkAvailable: Boolean
        get() {
            val network = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = Objects.requireNonNull(network).activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    fun onBackgroundTaskStarted() {
        _onBackgroundTaskStarted.value = true
    }

    init {
        //to start fetching data from api at the first moment of created viewModel
        _onBackgroundTaskStarted.value = true

    }

    fun startFetching(orderValue: String, manager: LoaderManager) {
        repository.startBackground(manager, object : NetworkObserver<ArrayList<NewsModel>> {
            //called before starting any background task
            override fun onPreExecuteBackground(): Boolean? {
                //if no network then show snackBar and stop the swipe feature
                return if (!isNetworkAvailable) {
                    _snackBarEnabled.value = true
                    //method disabling the swipe feature
                    _swipeEnabled.value = false
                    _noDataTextEnabled.value = true
                    true
                }
                else false
                //else call startBackgroundTask to begin the background
            }

            //do whatever u want to do in the background here
            override fun startBackgroundTask(): ArrayList<NewsModel> {
                return NewsData.Builder().startFetching(orderValue).news
            }

            //u can use data after loading it from network here
            //this works on Ui thread be cautious about doing intensive work here
            //if no internet connection this call back will not be invoked
            override fun onDataReceived(data: ArrayList<NewsModel>) {
                //provide the newList to viewModel whose responsible to provide it to recyclerView Adapter
                _news.value = data
                //stop the swipe feature after getting all data ready in recyclerView
                _swipeEnabled.value = false
                //disable the no data text message it it was there
                _noDataTextEnabled.value = false


            }
        })
    }
}
