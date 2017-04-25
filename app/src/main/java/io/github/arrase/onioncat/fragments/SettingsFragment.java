package io.github.arrase.onioncat.fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import io.github.arrase.onioncat.R;

public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ocat_settings);
    }
}
