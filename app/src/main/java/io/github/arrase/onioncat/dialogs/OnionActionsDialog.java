package io.github.arrase.onioncat.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.github.arrase.onioncat.R;

public class OnionActionsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();

        final View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.onion_actions_dialog, null);
        final AlertDialog actionDialog = new AlertDialog.Builder(getActivity())
                .setView(dialog_view)
                .setTitle(R.string.onion_domain)
                .create();

        Button copy = (Button) dialog_view.findViewById(R.id.onion_to_clipboard);
        copy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context mContext = v.getContext();
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("onion", arguments.getString("onion"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, R.string.done, Toast.LENGTH_LONG).show();
                actionDialog.dismiss();
            }
        });

        Button delete = (Button) dialog_view.findViewById(R.id.delete_onion);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString(getString(R.string.pref_server_onion), null);
                edit.apply();
                actionDialog.dismiss();
            }
        });

        Button cancel = (Button) dialog_view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                actionDialog.dismiss();
            }
        });

        return actionDialog;
    }
}
