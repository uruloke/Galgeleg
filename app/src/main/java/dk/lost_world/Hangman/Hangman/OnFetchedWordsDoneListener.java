package dk.lost_world.Hangman.Hangman;

import android.support.annotation.NonNull;

public interface OnFetchedWordsDoneListener {
    void onFetchedWordsDone(@NonNull HangmanWrapper hangman);
}
