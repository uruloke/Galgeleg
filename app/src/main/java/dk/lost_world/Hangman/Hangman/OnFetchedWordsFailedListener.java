package dk.lost_world.Hangman.Hangman;

import android.support.annotation.NonNull;

public interface OnFetchedWordsFailedListener {
    void onFetchedWordsFailed(@NonNull HangmanWrapper hangman, Exception exception);
}
