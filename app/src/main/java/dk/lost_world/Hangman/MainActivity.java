package dk.lost_world.Hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {

    protected Hangman hangman;
    protected TextView word;
    protected EditText guess;
    protected ImageView hangmanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hangman = new Hangman();
        setContentView(R.layout.activity_main);

        word = (TextView) findViewById(R.id.word);
        word.setText(StringUtils.repeat("_ ", hangman.word().length()));

        hangmanView = (ImageView) findViewById(R.id.hangmanView);


        guess = (EditText) findViewById(R.id.guessLetter);

        guess.addTextChangedListener(new guessWatcher(word, guess, hangmanView));
    }


    private class guessWatcher implements TextWatcher {

        protected TextView word;
        protected EditText guess;
        protected ImageView hangmanView;

        public guessWatcher(TextView word, EditText guess, ImageView hangmanView) {
            this.word = word;
            this.guess = guess;
            this.hangmanView = hangmanView;
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

            if (hangman.gameDone()) {
                if(hangman.gameWon()) {
                    word.setText("WON!");
                }
                else {
                    word.setText("LOST!");
                }
                return;
            }

            setHangmanImageByWrongGuesses();

            guess.setFocusable(true);
        }

        private void setHangmanImageByWrongGuesses() {
            switch (hangman.wrongGuesses()) {
                case 0:
                    hangmanView.setImageResource(R.drawable.gallow);
                    break;
                case 1:
                    hangmanView.setImageResource(R.drawable.wronGuess1);
                    break;
                case 2:
                    hangmanView.setImageResource(R.drawable.wrongGuess2);
                    break;
                case 3:
                    hangmanView.setImageResource(R.drawable.wrongGuess3);
                    break;
                case 4:
                    hangmanView.setImageResource(R.drawable.wrongGuess4);
                    break;
                case 5:
                    hangmanView.setImageResource(R.drawable.wrongGuess5);
                    break;
                case 6:
                    hangmanView.setImageResource(R.drawable.wrongGuess6);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
