package dk.lost_world.Hangman.Hangman;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import dk.lost_world.Hangman.MainActivity;

public class HangmanWrapper extends Hangman {

    private ArrayList<OnGameDoneListener> gameDoneListeners;
    private ArrayList<OnGameStartListener> gameStartListeners;
    private ArrayList<OnFetchedWordsDoneListener> fetchedWordsDoneListeners;
    private ArrayList<OnFetchedWordsFailedListener> fetchedWordsFailedListeners;
    private boolean started = false;

    private static final HangmanWrapper instance = new HangmanWrapper();

    public static HangmanWrapper getInstance() {
        return instance;
    }

    private HangmanWrapper() {
        super();
        gameDoneListeners = new ArrayList<>();
        gameStartListeners = new ArrayList<>();
        fetchedWordsDoneListeners = new ArrayList<>();
        fetchedWordsFailedListeners = new ArrayList<>();
    }

    public HangmanWrapper start() {
        started = true;
        gameStartListeners.forEach(gameStartListener -> gameStartListener.onGameStart(this));
        return this;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void guess(String letter) {
        if(!this.isStarted()) {
            throw new GameNotStartedException();
        }

        boolean gameDoneBeforeGuess = this.gameDone();
        super.guess(letter);

        if(this.gameDone() && !gameDoneBeforeGuess) {
            ((ArrayList<OnGameDoneListener>) gameDoneListeners.clone()).forEach(
                    gameDoneListener -> gameDoneListener.onGameDone(this, this.gameWon())
            );
            this.reset();
        }
    }

    public HangmanWrapper guess(char charToGuess) {
        this.guess(String.valueOf(charToGuess));
        return this;
    }

    public HangmanWrapper addGameDoneCallback(OnGameDoneListener gameDoneListener) {
        gameDoneListeners.add(gameDoneListener);
        return this;
    }

    public HangmanWrapper addGameStartCallback(OnGameStartListener gameStartListener) {
        gameStartListeners.add(gameStartListener);
        return this;
    }

    public HangmanWrapper addFetchedWordsDoneCallback(OnFetchedWordsDoneListener fetchedWordsDoneListener) {
        fetchedWordsDoneListeners.add(fetchedWordsDoneListener);
        return this;
    }

    public HangmanWrapper addFetchedWordsFailedCallback(OnFetchedWordsFailedListener fetchedWordsFailedListener) {
        fetchedWordsFailedListeners.add(fetchedWordsFailedListener);
        return this;
    }

    public ArrayList<OnFetchedWordsDoneListener> getFetchedWordsDoneListeners() {
        return fetchedWordsDoneListeners;
    }

    public ArrayList<OnFetchedWordsFailedListener> getFetchedWordsFailedListeners() {
        return fetchedWordsFailedListeners;
    }

    public HangmanWrapper removeGameDoneCallback(OnGameDoneListener gameDoneListener) {
        gameDoneListeners.remove(gameDoneListener);
        return this;
    }

    public HangmanWrapper removeGameStartCallback(OnGameStartListener gameStartListener) {
        gameStartListeners.remove(gameStartListener);
        return this;
    }

    public boolean isStarted() {
        return started;
    }

    @Override
    public void fetchWordsFromDr() {
        new FetchFromUrlTask(this).execute((RunnableException) super::fetchWordsFromDr);
    }

}
