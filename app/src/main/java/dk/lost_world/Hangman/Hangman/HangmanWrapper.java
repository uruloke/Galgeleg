package dk.lost_world.Hangman.Hangman;

import java.util.ArrayList;

public class HangmanWrapper extends Hangman {

    private ArrayList<OnGameDoneListener> gameDoneListeners;
    private ArrayList<OnGameStartListener> gameStartListeners;
    private boolean started = false;

    private static final HangmanWrapper instance = new HangmanWrapper();

    public static HangmanWrapper getInstance() {
        return instance;
    }

    private HangmanWrapper() {
        super();
        gameDoneListeners = new ArrayList<>();
        gameStartListeners = new ArrayList<>();
    }

    public HangmanWrapper start() {
        started = true;
        gameStartListeners.forEach(gameStartListener -> gameStartListener.onGameStart(this));
        return this;
    }

    @Override
    public void guess(String letter) {
        if(!this.isStarted()) {
            throw new GameNotStartedException();
        }

        boolean gameDoneBeforeGuess = this.gameDone();
        super.guess(letter);

        if(this.gameDone() && !gameDoneBeforeGuess) {
            gameDoneListeners.forEach(gameDoneListener -> gameDoneListener.onGameDone(this, this.gameWon()));
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

    public boolean isStarted() {
        return started;
    }
}
