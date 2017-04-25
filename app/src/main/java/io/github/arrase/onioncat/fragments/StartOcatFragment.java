package io.github.arrase.onioncat.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.arrase.onioncat.R;

public class StartOcatFragment extends Fragment {

    public StartOcatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.start_ocat_fragment, container, false);

        return v;
    }
}
