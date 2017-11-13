package dk.lost_world.Hangman;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameDoneDialog extends DialogFragment implements View.OnClickListener {

    public static GameDoneDialog newInstance(String message) {
        GameDoneDialog frag = new GameDoneDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_game_done, container, false);
        view.findViewById(R.id.closeDialog).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.messageView)).setText(getArguments().getString("message"));

        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        return view;
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
        getFragmentManager().popBackStack();
    }
}
