package dk.lost_world.Hangman;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import dk.lost_world.Hangman.Hangman.Hangman;

public class GuessWatcher implements TextWatcher {

    protected TextView word;
    protected EditText guess;
    protected ImageView hangmanView;
    protected Hangman hangman;

    public GuessWatcher(TextView word, EditText guess, ImageView hangmanView, Hangman hangman) {
        this.word = word;
        this.guess = guess;
        this.hangmanView = hangmanView;
        this.hangman = hangman;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length() == 0) {
            return;
        }
        guess.setFocusable(false);
        hangman.guess(charSequence.toString());

        word.setText(StringUtils.replace(hangman.currentVisibleWord(), "*", "_ "));
        guess.setText("");

        setHangmanImageByWrongGuesses();

        guess.setFocusable(true);
    }

    private void setHangmanImageByWrongGuesses() {
        switch (hangman.wrongGuesses()) {
            case 0:
                hangmanView.setImageResource(R.drawable.gallow);
                break;
            case 1:
                hangmanView.setImageResource(R.drawable.wrong_guess1);
                break;
            case 2:
                hangmanView.setImageResource(R.drawable.wrong_guess2);
                break;
            case 3:
                hangmanView.setImageResource(R.drawable.wrong_guess3);
                break;
            case 4:
                hangmanView.setImageResource(R.drawable.wrong_guess4);
                break;
            case 5:
                hangmanView.setImageResource(R.drawable.wrong_guess5);
                break;
            case 6:
                hangmanView.setImageResource(R.drawable.wrong_guess6);
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}