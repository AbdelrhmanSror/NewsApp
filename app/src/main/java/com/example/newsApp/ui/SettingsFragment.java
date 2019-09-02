package com.example.newsApp.ui;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.newsApp.R;

/**
 * fragment represent the preferences settings in the app
 */
 public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefrences,rootKey);

    }

}
