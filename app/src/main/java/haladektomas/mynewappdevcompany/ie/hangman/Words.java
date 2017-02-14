package haladektomas.mynewappdevcompany.ie.hangman;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by HaladekT on 01/03/2016.
 * this class handles file inport, when getMeWord method is called it gets a topic,
 * and boolean isSlovak as arguments, it the imports appropriate file from assets folder
 * the creates random number, and pick a word randomly, word is than guessed by user
 */
public class Words {
    Context context;
    private static ArrayList<Integer> usedWords;

    public Words(Activity activity) {
        this.context = activity;
    }

    public String getMeWord(int topic, boolean isSlovak) {
        AssetManager am = context.getAssets();
        InputStream fis = null;
        try {

            if (isSlovak) {
                if (topic == 0) {
                    fis = am.open("slovensky");
                } else if (topic == 1) {
                    fis = am.open("cars");
                } else if (topic == 2) {
                    fis = am.open("krajiny");
                } else if (topic == 3) {
                    fis = am.open("mesta");
                }
            } else {
                if (topic == 0) {
                    fis = am.open("random");
                } else if (topic == 1) {
                    fis = am.open("cars");
                } else if (topic == 2) {
                    fis = am.open("countries");
                } else if (topic == 3) {
                    fis = am.open("capitals");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String string = sb.toString();
        String[] allTheWords = string.split(";");
        Random rg = new Random();
        int num;
        if (usedWords == null) {
            num = rg.nextInt(allTheWords.length - 1);
            usedWords = new ArrayList<>();
            usedWords.add(num);
        } else {
            Log.i("No_MORE",":"+usedWords.size()+" vs. "+allTheWords.length);
            if(usedWords.size()==allTheWords.length){

                return "NO_MORE";
            }
            num = rg.nextInt(allTheWords.length - 1);


                for (int i = 0; i < usedWords.size(); i++) {
                    if (num == usedWords.get(i)) {
                        num = rg.nextInt(allTheWords.length - 1);
                        i=0;
                    }
                }
            usedWords.add(num);
        }
        return allTheWords[num];
    }



    public static void cleanUsedWords(){
        if(usedWords!=null){
            usedWords = new ArrayList<>();
        }
    }


}
