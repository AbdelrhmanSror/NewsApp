package com.example.newsApp.ui
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.example.newsApp.R
import com.example.newsApp.databinding.FragmentListBinding
import com.example.newsApp.viewModels.NewsViewModel
import com.example.newsApp.viewModels.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar

import java.util.Objects

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        setUpViewModel()
        setHasOptionsMenu(true)
        setUpSwipeRefreshLayout()
        return binding.root
    }

    //setup viewModel with all needed info
    private fun setUpViewModel() {
        preferences=androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val application = Objects.requireNonNull<FragmentActivity>(activity).application
        val manager = activity!!.supportLoaderManager
        val factory = NewsViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, factory).get(NewsViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.onBackgroundTaskStarted.observe(this, Observer {enabled->
            enabled?.let {
                viewModel.startFetching(preferences.getString(getString(R.string.orderBy), "newest")!!, manager)
            }
        })
        viewModel.snackBarEnabled.observe(this, Observer {enabled->
           enabled?.let {
               Snackbar.make(Objects.requireNonNull<FragmentActivity>(activity).findViewById(android.R.id.content), R.string.internetConnection,
                       Snackbar.LENGTH_LONG)
                       .show()
           }

        })
    }

    //set up swipe feature layout and its listener
    private fun setUpSwipeRefreshLayout() {
        //when user swipe then start getting data from preferences and start network call
        binding.swipeRefresh.setOnRefreshListener { viewModel.onBackgroundTaskStarted() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = Navigation.findNavController(Objects.requireNonNull<FragmentActivity>(activity), R.id.MyNavHostFragment)
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

}// Required empty public constructor
