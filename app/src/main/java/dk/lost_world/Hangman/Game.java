package dk.lost_world.Hangman;


import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import dk.lost_world.Hangman.Hangman.HangmanWrapper;
import dk.lost_world.Hangman.Hangman.OnGameDoneListener;
import dk.lost_world.Hangman.Hangman.OnGameStartListener;

import static dk.lost_world.Hangman.MainActivity.mGoogleApiClient;

public class Game extends Fragment implements OnGameDoneListener, OnGameStartListener, View.OnClickListener {

    protected HangmanWrapper hangman;
    protected TextView word;
    protected ImageView hangmanView;
    private Chronometer mChronometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game, container, false);

        hangman = HangmanWrapper.getInstance();
        hangmanView = root.findViewById(R.id.hangmanView);
        mChronometer = root.findViewById(R.id.gameTime);
        word = root.findViewById(R.id.word);

        hangman.addGameDoneCallback(this).addGameStartCallback(this);

        word.setText(StringUtils.repeat("_ ", hangman.word().length()));

        ArrayList<View> allButtons;
        allButtons = root.findViewById(R.id.gridLayout).getTouchables();
        allButtons.forEach(view -> view.setOnClickListener(this));

        hangman.start();
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hangman.reset();
        hangman.removeGameStartCallback(this).removeGameDoneCallback(this);
    }

    @Override
    public void onGameDone(@NonNull HangmanWrapper hangman, boolean wonGame) {
        mChronometer.stop();
        String message = getString(R.string.game_lost, hangman.word());

        if(hangman.gameWon()) {
            long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
            long score =  time * (hangman.wrongGuesses()+1);
            message = getString(R.string.game_won, score, time / 1000, hangman.wrongGuesses());

            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_score), score);
        }

        GameDoneDialog dialog = GameDoneDialog.newInstance(message);
        dialog.show(getFragmentManager(), "gameDoneDialog");
    }

    @Override
    public void onGameStart(@NonNull HangmanWrapper hangman) {
        mChronometer.setOnChronometerTickListener(chronometer -> {
            long t = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.setText(DateFormat.format("mm:ss", t));
        });
        mChronometer.start();
    }

    @Override
    public void onClick(View v) {
        Button button = ((Button) v);
        hangman.guess(Character.toLowerCase(button.getText().charAt(0)));
        word.setText(StringUtils.replace(hangman.currentVisibleWord(), "*", "_ "));

        setHangmanImageByWrongGuesses();
        button.setEnabled(false);
    }

    private void setHangmanImageByWrongGuesses() {
        switch (hangman.wrongGuesses()) {
            case 0:
                hangmanView.setImageResource(R.drawable.wrong_guess_0);
                break;
            case 1:
                hangmanView.setImageResource(R.drawable.wrong_guess_1);
                break;
            case 2:
                hangmanView.setImageResource(R.drawable.wrong_guess_2);
                break;
            case 3:
                hangmanView.setImageResource(R.drawable.wrong_guess_3);
                break;
            case 4:
                hangmanView.setImageResource(R.drawable.wrong_guess_4);
                break;
            case 5:
                hangmanView.setImageResource(R.drawable.wrong_guess_5);
                break;
            case 6:
                hangmanView.setImageResource(R.drawable.wrong_guess_6);
                break;
            case 7:
                hangmanView.setImageResource(R.drawable.wrong_guess_7);
                break;
        }
    }
}
