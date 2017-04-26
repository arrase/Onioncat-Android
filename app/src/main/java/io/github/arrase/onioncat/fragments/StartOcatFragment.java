package io.github.arrase.onioncat.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import io.github.arrase.onioncat.R;
import io.github.arrase.onioncat.constants.OcatConstant;
import io.github.arrase.onioncat.helpers.DependenciesHelper;
import io.github.arrase.onioncat.helpers.ServicesHelper;
import io.github.arrase.onioncat.services.OcatService;
import io.github.arrase.onioncat.services.OrbotService;

public class StartOcatFragment extends Fragment {
    private ImageView runOcat;
    private Context mContext;
    private OpenSettingsCallback openSettingsCallback;

    public StartOcatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ocat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            openSettingsCallback.openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final boolean isRunning = ServicesHelper.isServiceRunning(OcatService.class, mContext);

        View v = inflater.inflate(R.layout.start_ocat_fragment, container, false);

        runOcat = (ImageView) v.findViewById(R.id.run_ocat);

        if (isRunning) {
            runOcat.setImageDrawable(getResources().getDrawable(R.drawable.power_on));
        }

        runOcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DependenciesHelper.checkAll(mContext)) {
                    Toast.makeText(mContext, R.string.invalid_settings, Toast.LENGTH_LONG).show();
                } else if (isRunning) {
                    Toast.makeText(mContext, R.string.already_running, Toast.LENGTH_LONG).show();
                } else {
                    runOcat.setImageDrawable(getResources().getDrawable(R.drawable.power_on));
                    Intent intent = new Intent(mContext, OrbotService.class);
                    intent.setAction(OcatConstant.START_OCAT);
                    mContext.startService(intent);
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (mContext instanceof OpenSettingsCallback) {
            openSettingsCallback = (OpenSettingsCallback) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement openSettingsCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        openSettingsCallback = null;
    }

    public interface OpenSettingsCallback {
        void openSettings();
    }
}
