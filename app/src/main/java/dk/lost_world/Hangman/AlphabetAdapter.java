package dk.lost_world.Hangman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class AlphabetAdapter extends ArrayAdapter<String> {

    public AlphabetAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
    }

    public AlphabetAdapter(@NonNull Context context) {
        super(context, 0,
            Stream.concat(
                    IntStream.rangeClosed('A','Z').mapToObj(c -> String.valueOf((char) c)),
                    Stream.of("Æ", "Ø", "Å")
            ).collect(toList())
        );
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        String character = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alphabet_character, parent, false);
        }

        TextView button = convertView.findViewById(R.id.press_character);
        button.setText(character);

        return convertView;
    }

}
