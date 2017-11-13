package dk.lost_world.Hangman;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import dk.lost_world.Hangman.Hangman.HangmanWrapper;
import dk.lost_world.Hangman.Hangman.OnFetchedWordsDoneListener;
import dk.lost_world.Hangman.Hangman.OnFetchedWordsFailedListener;
import dk.lost_world.Hangman.Hangman.OnFetchedWordsStartListener;

import static dk.lost_world.Hangman.MainActivity.mGoogleApiClient;

public class Menu extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, OnFetchedWordsDoneListener, OnFetchedWordsFailedListener, OnFetchedWordsStartListener {

    private Button playButton;
    private Button scoreButton;
    private ImageButton settingButton;
    private SharedPreferences preferences;
    private View coordinatorLayout;
    private Snackbar snackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        playButton = root.findViewById(R.id.start);
        playButton.setOnClickListener(this);

        scoreButton = root.findViewById(R.id.Scoreboard);
        scoreButton.setOnClickListener(this);

        settingButton = root.findViewById(R.id.settingButton);
        settingButton.setOnClickListener(this);

        mGoogleApiClient.registerConnectionCallbacks(this);

        snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), R.string.UnableToConnectMessage, Snackbar.LENGTH_LONG);

        if(preferences.getBoolean(getString(R.string.useDr), true )) {
            HangmanWrapper.getInstance().removePossibleWords().fetchWordsFromDr();
        }
        else {
            HangmanWrapper.getInstance().removePossibleWords().addDefaultWords();
        }

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == playButton) {
            startGame();
        }
        else if(v == scoreButton) {
            launchScoreBoard();
        }
        else if(v == settingButton) {
            launchSettings();
        }
    }

    private void launchSettings() {
        SettingsDialog dialog = SettingsDialog.newInstance();
        dialog.show(getFragmentManager(), "settingDialog");
    }

    public void startGame() {
        HangmanWrapper.getInstance().reset();
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentView, new Game())
                .addToBackStack("StartGame")
                .hide(this)
                .commit();
    }

    public void launchScoreBoard() {
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,getString(R.string.leaderboard_score)), 100);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        scoreButton.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onFetchedWordsDone(@NonNull HangmanWrapper hangman) {
        Log.d("FETCHING", "DONE");
        playButton.setEnabled(true);
    }

    @Override
    public void onFetchedWordsFailed(@NonNull HangmanWrapper hangman, Exception exception) {
        Log.d("FETCHING", "FAILED");
        playButton.setEnabled(false);
        preferences.edit().putBoolean(getString(R.string.useDr), false).apply();
        HangmanWrapper.getInstance().removePossibleWords().addDefaultWords();
        snackbar.show();
    }

    @Override
    public void onFetchedWordsStart(@NonNull HangmanWrapper hangman) {
        Log.d("FETCHING", "START");
        playButton.setEnabled(false);
    }
}
