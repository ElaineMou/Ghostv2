package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix.isEmpty()) {
            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        }

        return binarySearch(prefix, 0, words.size());
    }

    private String binarySearch(String prefix, int begin, int end) {
        if (begin > end) {
            return null;
        }
        int index = (begin + end)/2;
        String word = words.get(index);
        if (word.startsWith(prefix) && !word.equals(prefix)) {
            return word;
        }
        if (word.compareTo(prefix) < 0) {
            return binarySearch(prefix,index+1, end);
        }
        if (word.compareTo(prefix) > 0) {
            return binarySearch(prefix, begin, index-1);
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
