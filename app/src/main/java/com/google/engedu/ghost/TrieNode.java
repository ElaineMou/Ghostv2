package com.google.engedu.ghost;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        if (s == null || s.length() == 0) {
            this.isWord = true;
            return;
        }

        String firstChar = s.substring(0,1);
        if (!children.containsKey(firstChar)) {
            children.put(firstChar, new TrieNode());
        }
        TrieNode nextNode = children.get(firstChar);
        nextNode.add(s.substring(1,s.length()));
    }

    public boolean isWord(String s) {
        if (s == null || s.length() == 0) {
            return this.isWord;
        }

        String firstChar = s.substring(0,1);
        if (children.containsKey(firstChar)) {
            TrieNode nextNode = children.get(firstChar);
            return nextNode.isWord(s.substring(1,s.length()));
        }
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        String prefix = s;
        TrieNode node = this;
        while(prefix.length() > 0 && node != null) {
            String firstChar = prefix.substring(0, 1);
            node = node.children.get(firstChar);
            prefix = prefix.substring(1, prefix.length());
        }
        if (node == null || node.children.isEmpty()) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(s);
        Random random = new Random();
        Object[] entries = node.children.entrySet().toArray();
        Map.Entry<String,TrieNode> entry = (Map.Entry<String, TrieNode>) entries[random.nextInt(entries.length)];
        node = entry.getValue();
        stringBuffer.append(entry.getKey());
        while (!node.isWord) {
            entries = node.children.entrySet().toArray();
            entry = (Map.Entry<String, TrieNode>) entries[random.nextInt(entries.length)];
            node = entry.getValue();
            stringBuffer.append(entry.getKey());
        }
        return stringBuffer.toString();
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
