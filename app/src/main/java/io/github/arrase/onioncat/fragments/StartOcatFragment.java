package io.github.arrase.onioncat.fragments;


import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import io.github.arrase.onioncat.R;
import io.github.arrase.onioncat.constants.OcatConstant;
import io.github.arrase.onioncat.helpers.DependenciesHelper;
import io.github.arrase.onioncat.helpers.ServicesHelper;
import io.github.arrase.onioncat.services.OcatService;
import io.github.arrase.onioncat.services.OrbotService;

public class StartOcatFragment extends Fragment {
    private ImageButton runOcat;
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

        switch (id) {
            case R.id.action_settings:
                openSettingsCallback.openSettings();
                return true;
            case R.id.get_ipv6:
                if (ServicesHelper.isServiceRunning(OcatService.class, mContext)) {
                    String ip = ServicesHelper.getOcatIPv6(mContext);
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("ipv6", ip);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(mContext, R.string.ipv6_to_clipboard, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.ocat_is_not_running, Toast.LENGTH_LONG).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.start_ocat_fragment, container, false);

        runOcat = (ImageButton) v.findViewById(R.id.run_ocat);

        if (ServicesHelper.isServiceRunning(OcatService.class, mContext)) {
            runOcat.setImageDrawable(getResources().getDrawable(R.drawable.power_on));
        }

        runOcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DependenciesHelper.checkAll(mContext)) {
                    Toast.makeText(mContext, R.string.invalid_settings, Toast.LENGTH_LONG).show();
                } else if (ServicesHelper.isServiceRunning(OcatService.class, mContext)) {
                    Toast.makeText(mContext, R.string.stopping_ocat, Toast.LENGTH_LONG).show();
                    runOcat.setImageDrawable(getResources().getDrawable(R.drawable.power_off));
                    ServicesHelper.stopOcat(mContext);
                } else {
                    runOcat.setImageDrawable(getResources().getDrawable(R.drawable.power_on));
                    Intent start = new Intent(mContext, OrbotService.class);
                    start.setAction(OcatConstant.START_OCAT);
                    mContext.startService(start);
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
