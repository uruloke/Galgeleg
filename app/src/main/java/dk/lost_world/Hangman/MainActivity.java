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

        guess.addTextChangedListener(new GuessWatcher(word, guess, hangmanView, hangman));
    }

}
