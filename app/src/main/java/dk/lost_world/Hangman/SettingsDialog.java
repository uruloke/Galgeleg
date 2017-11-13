package dk.lost_world.Hangman;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import dk.lost_world.Hangman.Hangman.HangmanWrapper;
import dk.lost_world.Hangman.Hangman.OnFetchedWordsFailedListener;

public class SettingsDialog extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnFetchedWordsFailedListener{

    Button closeButton;
    SharedPreferences preferences;
    Switch drSwitch;

    public static SettingsDialog newInstance() {
        SettingsDialog frag = new SettingsDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_settings, container, false);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        closeButton = view.findViewById(R.id.closeDialog);
        closeButton.setOnClickListener(this);

        drSwitch = view.findViewById(R.id.useDRSwitch);
        drSwitch.setChecked(preferences.getBoolean(getString(R.string.useDr), true));
        drSwitch.setOnCheckedChangeListener(this);
        HangmanWrapper.getInstance().addFetchedWordsFailedCallback(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HangmanWrapper.getInstance().removeFetchedWordsFailedCallback(this);
    }

    @Override
    public void onClick(View view) {
        if(view == closeButton) {
            this.dismiss();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        preferences.edit().putBoolean(getString(R.string.useDr), checked).apply();
        Log.e("CHECKED", String.valueOf(checked));

        if(checked) {
            HangmanWrapper.getInstance().removePossibleWords().fetchWordsFromDr();
        }
        else {
            HangmanWrapper.getInstance().removePossibleWords().addDefaultWords();
        }
    }

    @Override
    public void onFetchedWordsFailed(@NonNull HangmanWrapper hangman, Exception exception) {
        drSwitch.setChecked(preferences.getBoolean(getString(R.string.useDr), true));
    }
}
