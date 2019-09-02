package com.example.newsApp

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

import com.example.newsApp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var mAppBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        //reference to nav controller
        navController = Navigation.findNavController(this, R.id.MyNavHostFragment)
        //reference to toolBar
        val toolbar = binding.toolbar
        //set toolbar as default action bar
        setSupportActionBar(toolbar)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(navController.graph)
                .build()
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp()
    }
}
