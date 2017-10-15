package dk.lost_world.Hangman.Hangman;

import android.support.annotation.NonNull;

public interface OnGameDoneListener {
    void onGameDone(@NonNull HangmanWrapper hangman, boolean wonGame);
}
