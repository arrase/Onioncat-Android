package io.github.arrase.onioncat;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.github.arrase.onioncat.constants.OcatConstant;
import io.github.arrase.onioncat.fragments.SettingsFragment;
import io.github.arrase.onioncat.fragments.StartOcatFragment;
import io.github.arrase.onioncat.helpers.InstallHelper;

public class OcatActivity extends AppCompatActivity implements
        SettingsFragment.setOnionCallback,
        StartOcatFragment.OpenSettingsCallback {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();

        InstallHelper.copyAssets(this);

        // Do not overlapping fragments.
        if (savedInstanceState != null) return;

        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new StartOcatFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setOnion() {
        Intent intent = new Intent(OcatConstant.INTENT_ACTION_REQUEST_HIDDEN_SERVICE);
        intent.putExtra("hs_port", 8060);
        startActivityForResult(intent, OcatConstant.REQUEST_HIDDEN_SERVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OcatConstant.REQUEST_HIDDEN_SERVICE) {
            if (resultCode == RESULT_OK) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString(getString(R.string.pref_server_onion), data.getStringExtra("hs_host"));
                edit.apply();
            }
        }
    }

    @Override
    public void openSettings() {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }
}
