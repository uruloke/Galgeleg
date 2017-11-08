package dk.lost_world.Hangman.Hangman;

import android.os.AsyncTask;


public class FetchFromUrlTask extends AsyncTask<RunnableException, Void, Exception>{
    private HangmanWrapper hangman;

    public FetchFromUrlTask(HangmanWrapper hangman) {
        this.hangman = hangman;
    }


    @Override
    protected Exception doInBackground(RunnableException... runnableExceptions) {
        try {
            runnableExceptions[0].run();
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Exception e) {
        if(e == null) {
            hangman.getFetchedWordsDoneListeners().forEach(fetchedWordsDoneListener -> fetchedWordsDoneListener.onFetchedWordsDone(hangman));
            return;
        }
        hangman.getFetchedWordsFailedListeners().forEach(fetchedWordsFailedListener -> fetchedWordsFailedListener.onFetchedWordsFailed(hangman, e));
    }
}
