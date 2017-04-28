package io.github.arrase.onioncat.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.github.arrase.onioncat.R;
import io.github.arrase.onioncat.constants.OcatConstant;
import io.github.arrase.onioncat.dialogs.OnionActionsDialog;
import io.github.arrase.onioncat.helpers.DependenciesHelper;
import io.github.arrase.onioncat.services.OrbotService;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Context mContext;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver startOrbotEnd;
    private setOnionCallback setOnionCallback;

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ocat_settings);

        // Orbot
        Preference installOrbot = (Preference) findPreference(getString(R.string.pref_orbot_installed));

        if (DependenciesHelper.checkOrbot(mContext)) {
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

        // Onion
        Preference onion = (Preference) findPreference(getString(R.string.pref_set_server_onion));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String domainName = preferences.getString(mContext.getString(R.string.pref_server_onion), null);

        if (domainName != null) {
            onion.setSummary(domainName);
            onion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Bundle arguments = new Bundle();
                    arguments.putString("onion", domainName);
                    OnionActionsDialog dialog = new OnionActionsDialog();
                    dialog.setArguments(arguments);
                    dialog.show(getFragmentManager(), "OnionActionsDialog");
                    return true;
                }
            });
        } else {
            localBroadcastManager = LocalBroadcastManager.getInstance(mContext);

            startOrbotEnd = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    localBroadcastManager.unregisterReceiver(startOrbotEnd);
                    setOnionCallback.setOnion();
                }
            };

            onion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(mContext, R.string.request_onion, Toast.LENGTH_LONG).show();

                    localBroadcastManager.registerReceiver(
                            startOrbotEnd, new IntentFilter(OcatConstant.START_ORBOT_END));

                    Intent intent = new Intent(mContext, OrbotService.class);
                    intent.setAction(OcatConstant.START_ORBOT);
                    mContext.startService(intent);
                    return true;
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if (mContext instanceof setOnionCallback) {
            setOnionCallback = (setOnionCallback) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OpenSettingsCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setOnionCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_server_onion))) {
            Preference serverOnionPref = findPreference(getString(R.string.pref_set_server_onion));
            serverOnionPref.setSummary(sharedPreferences.getString(key, getString(R.string.onion_is_not_set)));
        }
    }

    public interface setOnionCallback {
        void setOnion();
    }
}
