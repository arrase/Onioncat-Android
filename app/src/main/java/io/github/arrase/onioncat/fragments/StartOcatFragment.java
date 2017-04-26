package io.github.arrase.onioncat.fragments;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import io.github.arrase.onioncat.R;
import io.github.arrase.onioncat.constants.OcatConstant;
import io.github.arrase.onioncat.helpers.CheckDependenciesHelper;
import io.github.arrase.onioncat.services.OcatService;
import io.github.arrase.onioncat.services.OrbotService;

public class StartOcatFragment extends Fragment {
    private ImageView runOcat;
    private Context mContext;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver startOrbotEnd;

    public StartOcatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);

        startOrbotEnd = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                localBroadcastManager.unregisterReceiver(startOrbotEnd);
                Intent ocat = new Intent(mContext, OcatService.class);
                ocat.setAction(OcatConstant.START_OCAT);
                mContext.startService(ocat);
            }
        };

        View v = inflater.inflate(R.layout.start_ocat_fragment, container, false);

        runOcat = (ImageView) v.findViewById(R.id.run_ocat);
        runOcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckDependenciesHelper.checkAll(mContext)) {
                    localBroadcastManager.registerReceiver(
                            startOrbotEnd, new IntentFilter(OcatConstant.START_ORBOT_END));

                    Intent intent = new Intent(mContext, OrbotService.class);
                    intent.setAction(OcatConstant.START_ORBOT);
                    mContext.startService(intent);
                } else {
                    Toast.makeText(mContext, R.string.invalid_settings, Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
