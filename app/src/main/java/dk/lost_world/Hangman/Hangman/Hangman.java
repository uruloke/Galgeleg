package dk.lost_world.Hangman.Hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

class Hangman {
    private ArrayList<String> possibleWords = new ArrayList<>();
    private String word;
    private ArrayList<String> usedLetters = new ArrayList<>();
    private String visibleWord;
    private int wrongGuesses;
    private boolean lastWordCorrect;
    private boolean wonGame;
    private boolean lostGame;


    public ArrayList<String> usedLetters() {
        return usedLetters;
    }

    public String currentVisibleWord() {
        return visibleWord;
    }

    public String word() {
        return word;
    }

    public int wrongGuesses() {
        return wrongGuesses;
    }

    public boolean lastWordCorrect() {
        return lastWordCorrect;
    }

    public boolean gameWon() {
        return wonGame;
    }

    public boolean gameLost() {
        return lostGame;
    }

    public boolean gameDone() {
        return lostGame || wonGame;
    }

    public Hangman() {
        possibleWords.add("bil");
        possibleWords.add("computer");
        possibleWords.add("programmering");
        possibleWords.add("motorvej");
        possibleWords.add("busrute");
        possibleWords.add("gangsti");
        possibleWords.add("skovsnegl");
        possibleWords.add("solsort");
        reset();
    }

    public void reset() {
        usedLetters.clear();
        wrongGuesses = 0;
        wonGame = false;
        lostGame = false;
        word = possibleWords.get(new Random().nextInt(possibleWords.size()));
        updateVisibleWord();
    }

    private void updateVisibleWord() {
        visibleWord = "";
        wonGame = true;
        for (Character letter: word.toCharArray()) {
            if(usedLetters.contains(letter.toString())) {
                visibleWord += letter;
            }
            else {
                visibleWord += "*";
                wonGame = false;
            }
        }
    }

    public void guess(String letter) {
        if (letter.length() != 1) return;
        System.out.println("Der gættes på bogstavet: " + letter);
        if (usedLetters.contains(letter)) return;
        if (wonGame || lostGame) return;

        usedLetters.add(letter);

        if (word.contains(letter)) {
            lastWordCorrect = true;
            System.out.println("Bogstavet var korrekt: " + letter);
        } else {
            // Vi gættede på et bogstav der ikke var i word.
            lastWordCorrect = false;
            System.out.println("Bogstavet var IKKE korrekt: " + letter);
            wrongGuesses++;
            if (wrongGuesses > 6) {
                lostGame = true;
            }
        }
        updateVisibleWord();
    }

    private static String fetchWordsFromURL(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line).append("\n");
            line = br.readLine();
        }
        return sb.toString();
    }

    public void fetchWordsFromDr() throws Exception {
        String data = fetchWordsFromURL("http://dr.dk");
        //System.out.println("data = " + data);

        data = data.substring(data.indexOf("<body")). // fjern headere
                replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
                replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
                replaceAll(" [a-zæøå] ", " "). // fjern 1-bogstavsord
                replaceAll(" [a-zæøå][a-zæøå] ", " "); // fjern 2-bogstavsord

        System.out.println("data = " + data);
        possibleWords.clear();
        possibleWords.addAll(new HashSet<>(Arrays.asList(data.split(" "))));

        System.out.println("possibleWords = " + possibleWords);
        reset();
    }
}
