package com.hraps.pytorch.tec;

import org.pytorch.Tensor;

import java.util.HashMap;
import java.util.List;

public class Vocab {
    HashMap<Long, String> idToWord;
    HashMap<String, Long> wordToId;

    public Vocab(List<String> words) {
        idToWord = new HashMap<>();
        wordToId = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            idToWord.put((long)i,words.get(i));
            wordToId.put(words.get(i),(long)i);
        }
    }

    public String getWord(long id){
        return idToWord.get(id);
    }

    public long getId(String word){
        try {
            return wordToId.get(word);
        }catch (Exception e){
            return 0;
        }
    }

    public long[] getIds(String[] words,int length){
        long ids[] = new long[length];
        for (int i = 0; i < words.length; i++) {
            ids[i] = getId(words[i]);
        }
        return ids;
    }

    public long[] getIds(String[] words){
        return getIds(words,words.length);
    }

    public String[] getWords(long[] ids,int length){
        String words[] = new String[length];
        for (int i = 0; i < ids.length; i++) {
            words[i] = getWord(ids[i]);
        }
        return words;
    }

    public String[] getWords(long[] ids){
        return getWords(ids,ids.length);
    }

    public long size(){
        return idToWord.size();
    }
}
