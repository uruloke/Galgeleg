package dk.lost_world.Hangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;

import org.apache.commons.lang3.StringUtils;

import dk.lost_world.Hangman.Hangman.HangmanWrapper;
import dk.lost_world.Hangman.Hangman.OnGameDoneListener;
import dk.lost_world.Hangman.Hangman.OnGameStartListener;

public class Game extends AppCompatActivity implements OnGameDoneListener, OnGameStartListener, ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    protected HangmanWrapper hangman;
    protected TextView word;
    protected EditText guess;
    protected ImageView hangmanView;
    private Chronometer mChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hangmanView = findViewById(R.id.hangmanView);
        mChronometer = findViewById(R.id.gameTime);
        guess = findViewById(R.id.guessLetter);
        word = findViewById(R.id.word);

        hangman = new HangmanWrapper();
        hangman.addGameDoneCallback(this);
        hangman.addGameStartCallback(this);

        word.setText(StringUtils.repeat("_ ", hangman.word().length()));
        guess.addTextChangedListener(new GuessWatcher(word, guess, hangmanView, hangman));

        // Create the Google Api Client with access to Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        hangman.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onGameDone(@NonNull HangmanWrapper hangman, boolean wonGame) {
        mChronometer.stop();
        String message = getString(R.string.game_lost);

        if(hangman.gameWon()) {
            long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
            long score =  time / (hangman.wrongGuesses()+1);
            message = getString(R.string.game_won, score, time / 1000, hangman.wrongGuesses());

            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_score), score);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, id) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
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
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Connection failed", connectionResult.toString());
    }
}
