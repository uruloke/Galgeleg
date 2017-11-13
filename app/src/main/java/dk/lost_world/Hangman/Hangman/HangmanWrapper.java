package dk.lost_world.Hangman.Hangman;

import java.util.ArrayList;

public class HangmanWrapper extends Hangman {

    private ArrayList<OnGameDoneListener> gameDoneListeners = new ArrayList<>();
    private ArrayList<OnGameStartListener> gameStartListeners = new ArrayList<>();
    private ArrayList<OnFetchedWordsDoneListener> fetchedWordsDoneListeners = new ArrayList<>();
    private ArrayList<OnFetchedWordsFailedListener> fetchedWordsFailedListeners = new ArrayList<>();
    private ArrayList<OnFetchedWordsStartListener> fetchedWordsStartListeners = new ArrayList<>();
    private boolean started = false;

    private static final HangmanWrapper instance = new HangmanWrapper();

    public static HangmanWrapper getInstance() {
        return instance;
    }

    private HangmanWrapper() {
        super();
    }

    public HangmanWrapper start() {
        started = true;
        gameStartListeners.forEach(gameStartListener -> gameStartListener.onGameStart(this));
        return this;
    }

    public HangmanWrapper removePossibleWords()
    {
        this.possibleWords.clear();
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

    public HangmanWrapper addFetchedWordsStartCallback(OnFetchedWordsStartListener fetchedWordsStartListener) {
        fetchedWordsStartListeners.add(fetchedWordsStartListener);
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
        this.fetchedWordsStartListeners.forEach(fetchedWordsStartListener -> fetchedWordsStartListener.onFetchedWordsStart(this));
        new FetchFromUrlTask(this).execute((RunnableException) super::fetchWordsFromDr);
    }

    public void addDefaultWords() {
        this.fetchedWordsStartListeners.forEach(fetchedWordsStartListener -> fetchedWordsStartListener.onFetchedWordsStart(this));
        possibleWords.add("bil");
        possibleWords.add("computer");
        possibleWords.add("programmering");
        possibleWords.add("motorvej");
        possibleWords.add("busrute");
        possibleWords.add("gangsti");
        possibleWords.add("skovsnegl");
        possibleWords.add("solsort");
        this.fetchedWordsDoneListeners.forEach(fetchedWordsDoneListener -> fetchedWordsDoneListener.onFetchedWordsDone(this));
    }

    public HangmanWrapper removeFetchedWordsFailedCallback(OnFetchedWordsFailedListener fetchedWordsFailedListener) {
        fetchedWordsFailedListeners.remove(fetchedWordsFailedListener);
        return this;
    }
}
