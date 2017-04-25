package io.github.arrase.onioncat.fragments;


import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.github.arrase.onioncat.R;
import io.github.arrase.onioncat.helpers.CheckDependenciesHelper;

public class SettingsFragment extends PreferenceFragment {
    private Context mContext;

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ocat_settings);

        // Orbot
        Preference installOrbot = (Preference) findPreference(getString(R.string.pref_orbot_installed));

        if (CheckDependenciesHelper.checkOrbot(mContext)) {
            installOrbot.setSummary(R.string.orbot_is_installed);
        } else {
            installOrbot.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(OrbotHelper.getOrbotInstallIntent(mContext));
                    return true;
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
