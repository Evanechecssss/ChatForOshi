package com.evanechecssss.girlai.client.utile;

import java.util.ArrayList;
import java.util.Random;

/**
 * \* https://evanechecssss.github.io
 * \
 */
public class RandomNickGen
{
    private ArrayList<String> namesDictionary = new ArrayList<>();
    
    public RandomNickGen () {
        namesDictionary.add("Oshi");
        namesDictionary.add("Evan");
    }
    public String getRandom()
    {
        return  namesDictionary.get((int) Math.round(Math.random()));
    }
}