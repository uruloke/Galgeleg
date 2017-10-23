package dk.lost_world.Hangman;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import static dk.lost_world.Hangman.MainActivity.mGoogleApiClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class Menu extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private Button playButton;
    private Button scoreButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        playButton = root.findViewById(R.id.start);
        playButton.setOnClickListener(this);

        scoreButton = root.findViewById(R.id.Scoreboard);
        scoreButton.setOnClickListener(this);

        mGoogleApiClient.registerConnectionCallbacks(this);

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
    }


    public void startGame() {
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentView, new Game())
                .addToBackStack("StartGame")
                .remove(this)
                .commit();
    }

    public void launchScoreBoard() {
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,getString(R.string.leaderboard_score)), 100);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getView().findViewById(R.id.Scoreboard).setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}