package haladektomas.mynewappdevcompany.ie.hangman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by HaladekT on 01/03/2016.
 * All the game logic is done in this class, it handles both, single player and multiplayer games
 * if single player game, user gets to choose topic for words and then a word is picked based on
 * random number, user then guests the word, if there is 7 wrong letters guested the game
 * player dies, if user guess the word, the gets a new one from the same topic. when player dies the
 * score is counted from hit - miss. If player score is higher then the 10th player in the High Score
 * Table player object is created and player saves his name,score,the last winning word and game type
 * (single player or multi player).
 */

public class SinglePlayerActivity extends AppCompatActivity implements View.OnClickListener,
        AlertDFragment.DialogListener,
        HighScorerFragment.HsFragListener,
        NoMoreWordsFragment.ScoreIncrease{
    private static int miss, hit, score;
    private String oneWord;
    private Button bA, bB, bC, bD, bE, bF, bG, bH, bI, bJ, bK, bL, bM, bN, bO, bP, bQ, bR, bS, bT, bU, bV, bW, bX, bY, bZ;
    private TextView tVWordLenght, tVUnderScores, tVHit, tVMiss;
    private ImageView iVHang;
    private StringBuilder sbNew;
 //   MediaPlayer mClick;
    private boolean vib;
   private boolean isSoundOn;
    private int topic;
    private String multiPlayerWord;
    private boolean isMultiPlayer;
    private int lowestScore;
    private ArrayList<Integer> scoreValues;
    private int holdMiss, holdHit;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        Intent intent = getIntent();
        topic = intent.getIntExtra("topic", 1);
        multiPlayerWord = intent.getStringExtra("MultiWord");
        isMultiPlayer = intent.getBooleanExtra("isMultiPlayer", false);
        holdHit = intent.getIntExtra("hit_value", 0);
        holdMiss = intent.getIntExtra("miss_value", 0);
        updateDB();
        Words.cleanUsedWords();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (isMultiPlayer) {
            toolbar.setTitle(R.string.multi_player);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      //  mClick = MediaPlayer.create(this, R.raw.click);
        tVUnderScores = (TextView) findViewById(R.id.tVUnderscores);
        tVWordLenght = (TextView) findViewById(R.id.tVWordLength);
        iVHang = (ImageView) findViewById(R.id.iVhang);
        tVHit = (TextView) findViewById(R.id.tVHit);
        tVMiss = (TextView) findViewById(R.id.tVMiss);

        bA = (Button) findViewById(R.id.bA);
        bB = (Button) findViewById(R.id.bB);
        bC = (Button) findViewById(R.id.bC);
        bD = (Button) findViewById(R.id.bD);
        bE = (Button) findViewById(R.id.bE);
        bF = (Button) findViewById(R.id.bF);
        bG = (Button) findViewById(R.id.bG);
        bH = (Button) findViewById(R.id.bH);
        bI = (Button) findViewById(R.id.bI);
        bJ = (Button) findViewById(R.id.bJ);
        bK = (Button) findViewById(R.id.bK);
        bL = (Button) findViewById(R.id.bL);
        bM = (Button) findViewById(R.id.bM);
        bN = (Button) findViewById(R.id.bN);
        bO = (Button) findViewById(R.id.bO);
        bP = (Button) findViewById(R.id.bP);
        bQ = (Button) findViewById(R.id.bQ);
        bR = (Button) findViewById(R.id.bR);
        bS = (Button) findViewById(R.id.bS);
        bT = (Button) findViewById(R.id.bT);
        bU = (Button) findViewById(R.id.bU);
        bV = (Button) findViewById(R.id.bV);
        bW = (Button) findViewById(R.id.bW);
        bX = (Button) findViewById(R.id.bX);
        bY = (Button) findViewById(R.id.bY);
        bZ = (Button) findViewById(R.id.bZ);
        bA.setOnClickListener(this);
        bB.setOnClickListener(this);
        bC.setOnClickListener(this);
        bD.setOnClickListener(this);
        bE.setOnClickListener(this);
        bF.setOnClickListener(this);
        bG.setOnClickListener(this);
        bH.setOnClickListener(this);
        bI.setOnClickListener(this);
        bJ.setOnClickListener(this);
        bK.setOnClickListener(this);
        bL.setOnClickListener(this);
        bM.setOnClickListener(this);
        bN.setOnClickListener(this);
        bO.setOnClickListener(this);
        bP.setOnClickListener(this);
        bQ.setOnClickListener(this);
        bR.setOnClickListener(this);
        bS.setOnClickListener(this);
        bT.setOnClickListener(this);
        bU.setOnClickListener(this);
        bV.setOnClickListener(this);
        bW.setOnClickListener(this);
        bX.setOnClickListener(this);
        bY.setOnClickListener(this);
        bZ.setOnClickListener(this);
        nullScore();
    }

    //called just after onCreate() to check preferences, set all buttons visible and get a word to be guessed
    private void init() {
        updateDB();
        //check shared preferences for settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        vib = sharedPrefs.getBoolean("Vibration", true);
        isSoundOn = sharedPrefs.getBoolean("soundEffects", true);
        difficulty = sharedPrefs.getString("DIFFICULTY", "EASY");
        //make all the button visible
        bA.setVisibility(View.VISIBLE);
        bB.setVisibility(View.VISIBLE);
        bC.setVisibility(View.VISIBLE);
        bD.setVisibility(View.VISIBLE);
        bE.setVisibility(View.VISIBLE);
        bF.setVisibility(View.VISIBLE);
        bG.setVisibility(View.VISIBLE);
        bH.setVisibility(View.VISIBLE);
        bI.setVisibility(View.VISIBLE);
        bJ.setVisibility(View.VISIBLE);
        bJ.setVisibility(View.VISIBLE);
        bK.setVisibility(View.VISIBLE);
        bL.setVisibility(View.VISIBLE);
        bM.setVisibility(View.VISIBLE);
        bN.setVisibility(View.VISIBLE);
        bO.setVisibility(View.VISIBLE);
        bP.setVisibility(View.VISIBLE);
        bQ.setVisibility(View.VISIBLE);
        bR.setVisibility(View.VISIBLE);
        bS.setVisibility(View.VISIBLE);
        bT.setVisibility(View.VISIBLE);
        bU.setVisibility(View.VISIBLE);
        bV.setVisibility(View.VISIBLE);
        bW.setVisibility(View.VISIBLE);
        bX.setVisibility(View.VISIBLE);
        bY.setVisibility(View.VISIBLE);
        bZ.setVisibility(View.VISIBLE);
        sbNew = new StringBuilder();

        tVUnderScores.setText(null);
        if (isMultiPlayer) {
            oneWord = multiPlayerWord.trim();
            miss = holdMiss;
            hit = holdHit;

        } else {
            boolean isSlovak = false;
            String myLang = Locale.getDefault().getISO3Language().toString();
            if (myLang.equals("slk")) {
                isSlovak = true;
                Words wordsClass = new Words(this);
                oneWord = wordsClass.getMeWord(topic, isSlovak);
            } else {
                Words wordsClass = new Words(this);
                oneWord = wordsClass.getMeWord(topic, isSlovak);
            }
        }
        oneWord = oneWord.trim();
        if(oneWord.equals("NO_MORE")){
            NoMoreWordsFragment fragment = NoMoreWordsFragment.newInstance(hit);
            fragment.show(getSupportFragmentManager(),"FINAL");
        }
        oneWord = oneWord.toUpperCase();
        System.out.println("Word is " + oneWord);
        wordLengthToText(oneWord);
        ArrayList<Integer> myIndexes = new ArrayList();
        if (oneWord.contains(" ")) {
            char space = ' ';
            for (int i = 0; i < oneWord.length(); i++) {
                if (space == (oneWord.charAt(i))) {
                    myIndexes.add(i);
                }
            }
        }
        for (int i = 0; i < oneWord.length(); i++) {
            sbNew.append("*");
        }
        for (int x = 0; x < myIndexes.size(); x++) {
            int index = myIndexes.get(x);
            sbNew.setCharAt(index, ' ');
        }
        if (!isMultiPlayer) {
            updateWord();
        }
        tVUnderScores.setText(sbNew.toString());
        tVMiss.setText(getString(R.string.miss) + " " + miss);
        tVHit.setText(getString(R.string.hit) + " " + hit);
    }

    //method to update the guessed word, based on difficulty from shared preferences
    private void updateWord() {

        int wordLength = oneWord.length();
        Random rg = new Random();
        int numOne = 0;
        int numTwo = 0;
        int numThree = 0;
        String one, two, three;
        System.out.println("dif is " + difficulty);
        if (wordLength < 6) {
            switch (difficulty) {
                case "MEDIUM":
                    numOne = rg.nextInt(wordLength);
                    one = String.valueOf(oneWord.charAt(numOne));
                    onClick(one);
                    giveMeHelp(one);
                    break;
                case "EASY":
                    while (numOne == numTwo) {
                        numOne = rg.nextInt(wordLength);
                        numTwo = rg.nextInt(wordLength);
                    }
                    one = String.valueOf(oneWord.charAt(numOne));
                    two = String.valueOf(oneWord.charAt(numTwo));
                    onClick(one);
                    int num = giveMeHelp(one);
                    if (num == 1) {
                        giveMeHelp(two);
                        onClick(two);
                    }
                    break;
                case "HARD":
                    break;
            }
        } else {
            switch (difficulty) {
                case "MEDIUM":
                    while (numOne == numTwo) {
                        numOne = rg.nextInt(wordLength);
                        numTwo = rg.nextInt(wordLength);
                    }
                    one = String.valueOf(oneWord.charAt(numOne));
                    two = String.valueOf(oneWord.charAt(numTwo));
                    onClick(two);
                    onClick(one);
                    giveMeHelp(one);
                    giveMeHelp(two);
                    break;


                case "EASY":
                    while (numOne == numTwo | numOne == numThree) {
                        numOne = rg.nextInt(wordLength);
                        numTwo = rg.nextInt(wordLength);
                        numThree = rg.nextInt(wordLength);
                    }
                    one = String.valueOf(oneWord.charAt(numOne));
                    two = String.valueOf(oneWord.charAt(numTwo));
                    three = String.valueOf(oneWord.charAt(numThree));
                    onClick(one);


                    int num = giveMeHelp(one);
                    if (num == 1) {
                        int num2 = giveMeHelp(two);
                        onClick(two);

                        if (num2 == 1) {
                            giveMeHelp(three);
                            onClick(three);
                        }
                    } else {
                        giveMeHelp(three);
                        onClick(three);
                    }
                    break;
                case "HARD":
                    break;
            }
        }
    }

    //method called from within updateWord to update guessed word
    private int giveMeHelp(String oneLetter) {
        int count = 0;
        for (int i = 0; i < oneWord.length(); i++) {
            String check = String.valueOf(oneWord.charAt(i));
            if (check.equalsIgnoreCase(oneLetter)) {
                sbNew.setCharAt(i, oneLetter.charAt(0));
                count++;
            }
        }
        return count;
    }

    //method to null score
    public void nullScore() {
        hit = 0;
        miss = 0;
        score = 0;
        iVHang.setVisibility(View.INVISIBLE);
        init();
    }

    //creates a player object, initialize all fields and save player to database
    private void savePlayer(String playerName) {
        Player player = new Player();
        if (isMultiPlayer) {
            player.setType(1);
            player.setGameDif("EASY");

        } else {
            player.setType(0);
            player.setGameDif(difficulty);
        }
        player.setName(playerName);
        player.setScore(String.valueOf(score));
        if (!isMultiPlayer) {
            player.setWinningWord(String.valueOf(topic));
        }
        if (isMultiPlayer) {
            player.setWinningWord(oneWord);
        }
        DataBaseHandler db = new DataBaseHandler(this);
        db.addDBEntry(player);
        db.close();
        Intent intent = new Intent(SinglePlayerActivity.this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //method called if player looses the game(i.e. gets 7 misses), it uses AlertDFragment
    private void gameLost() {
        AlertDFragment myDialog = AlertDFragment.newInstance(R.drawable.thumb_down_circle, R.string.you_lost, R.string.play_again, true, true, R.string.yes, R.string.no);
        myDialog.show(getSupportFragmentManager(), "DIALOG_TWO");
    }


    //method to check if guessing word contains any space and print how many letters the guessed word has
    public void wordLengthToText(String s) {
        int myLength;
        if (oneWord.contains(" ")) {
            char space = ' ';
            int counter = 0;
            for (int i = 0; i < oneWord.length(); i++) {
                if (space == (oneWord.charAt(i))) {
                    counter++;
                }
            }
            myLength = oneWord.length() - counter;
            tVWordLenght.setText(getString(R.string.this_word_has) + " " + myLength + " " + getString(R.string.letters));
        } else {
            tVWordLenght.setText(getString(R.string.this_word_has) + " " + s.length() + " " + getString(R.string.letters));
        }
    }

    //method that does all the logic, after player presses a button, buttonOnClick listener pass a letter to this method to check
    //if user guess was right or wrong, if player guess all letters and miss<7 player gets new word, if players miss==7 game ends.
    //I then compare players score with the lowest score on the High Score Table, if its bigger player gets prompt to put in his name
    //and player gets saved to the Database
    private void check(String letter) {


        final Vibrator vibratore = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        int index = -1;
        for (int i = 0; i < oneWord.length(); i++) {
            String check = String.valueOf(oneWord.charAt(i));
            if (check.equalsIgnoreCase(letter)) {
                index = i;
                sbNew.setCharAt(i, letter.charAt(0));
            }
        }
        if (index == -1) {
            if (vib) {
                vibratore.vibrate(100);
            }
            miss++;

        } else {

            hit++;
            tVUnderScores.setText(sbNew.toString());
        }
        if (sbNew.toString().equalsIgnoreCase(oneWord)) {
            DialogFragment newFragment = AlertDFragment.newInstance(R.drawable.hangman_circle, giveFeedback(hit), R.string.empty_text, true, false, R.string.continue_text, R.string.no);
            newFragment.show(getSupportFragmentManager(), "DIALOG_ONE");
        }
        if (miss == 7) {
            tVUnderScores.setText(oneWord);
            if (!isMultiPlayer) {
                if (difficulty.equalsIgnoreCase("MEDIUM")) {
                    hit = hit + 2;
                }
                if (difficulty.equalsIgnoreCase("HARD")) {
                    hit = hit + 5;
                }
            }
            score = hit - miss;

            if (score > lowestScore) {
                String positionInTable = giveMePosition(score);
                HighScorerFragment frag = HighScorerFragment.newInstance(positionInTable);
                frag.show(getSupportFragmentManager(), "HS");
            } else {
                gameLost();
            }
        }

        hangmanPicture(miss);
        tVHit.setText(getString(R.string.hit) + String.valueOf(hit));
        tVMiss.setText(getString(R.string.miss) + String.valueOf(miss));
    }

    private String giveMePosition(int score) {
        int position = 0;
        for (int i = 0; i < scoreValues.size(); i++) {
            int num = scoreValues.get(i);
            if (score >= num) {
                position = i;

            }
        }
        Log.i("Switch", "position" + position);
        switch (position) {

            case 0:
                return getString(R.string._10th);
            case 1:
                return getString(R.string._9th);
            case 2:
                return getString(R.string._8th);
            case 3:
                return getString(R.string._7th);
            case 4:
                return getString(R.string._6th);
            case 5:
                return getString(R.string._5th);
            case 6:
                return getString(R.string._4th);
            case 7:
                return getString(R.string._3th);
            case 8:
                return getString(R.string._2nd);
            case 9:
                return getString(R.string._1st);
            default:
                return getString(R.string.xxx);
        }

    }

    //when player guess all letters I show AlertDialog, message the dialog set its based on score. the message is created here
    private int giveFeedback(int num) {

        if (num <= 5) {
            return R.string.just_starting;
        } else if (num > 5 && num <= 10) {
            return R.string.keep_going;
        } else if (num > 10 && num <= 15) {
            return R.string.well_done;
        } else if (num > 15 && num <= 20) {
            return R.string.getting_really_high;
        } else {
            return R.string.yes_master_hangman;
        }

    }

    //everytime a user press button with letter I check witch button was pressed, button then gets
    //dissabled,and check(letter) method is called
    @Override
    public void onClick(View v) {
        String letter = "";
        switch (v.getId()) {
            case R.id.bA:
                letter = "A";
                bA.setVisibility(View.GONE);
                break;
            case R.id.bB:
                letter = "B";
                bB.setVisibility(View.GONE);
                break;
            case R.id.bC:
                letter = "C";
                bC.setVisibility(View.GONE);
                break;
            case R.id.bD:
                letter = "D";
                bD.setVisibility(View.GONE);
                break;
            case R.id.bE:
                letter = "E";
                bE.setVisibility(View.GONE);
                break;
            case R.id.bF:
                letter = "F";
                bF.setVisibility(View.GONE);
                break;
            case R.id.bG:
                letter = "G";
                bG.setVisibility(View.GONE);
                break;
            case R.id.bH:
                letter = "H";
                bH.setVisibility(View.GONE);
                break;
            case R.id.bI:
                letter = "I";
                bI.setVisibility(View.GONE);
                break;
            case R.id.bJ:
                letter = "J";
                bJ.setVisibility(View.GONE);
                break;
            case R.id.bK:
                letter = "K";
                bK.setVisibility(View.GONE);
                break;
            case R.id.bL:
                letter = "L";
                bL.setVisibility(View.GONE);
                break;
            case R.id.bM:
                letter = "M";
                bM.setVisibility(View.GONE);
                break;
            case R.id.bN:
                letter = "N";
                bN.setVisibility(View.GONE);
                break;
            case R.id.bO:
                letter = "O";
                bO.setVisibility(View.GONE);
                break;
            case R.id.bP:
                letter = "P";
                bP.setVisibility(View.GONE);
                break;
            case R.id.bQ:
                letter = "Q";
                bQ.setVisibility(View.GONE);
                break;
            case R.id.bR:
                letter = "R";
                bR.setVisibility(View.GONE);
                break;
            case R.id.bS:
                letter = "S";
                bS.setVisibility(View.GONE);
                break;
            case R.id.bT:
                letter = "T";
                bT.setVisibility(View.GONE);
                break;
            case R.id.bU:
                letter = "U";
                bU.setVisibility(View.GONE);
                break;
            case R.id.bV:
                letter = "V";
                bV.setVisibility(View.GONE);
                break;
            case R.id.bW:
                letter = "W";
                bW.setVisibility(View.GONE);
                break;
            case R.id.bX:
                letter = "X";
                bX.setVisibility(View.GONE);
                break;
            case R.id.bY:
                letter = "Y";
                bY.setVisibility(View.GONE);
                break;
            case R.id.bZ:
                letter = "Z";
                bZ.setVisibility(View.GONE);
                break;
        }
        check(letter);

    }

    //this method is called only when difficulty is set to easy or medium, it just
    //dissables buttons that word contains but they are shown because low level difficulty
    public void onClick(String letter) {

        switch (letter) {
            case "A":
                bA.setVisibility(View.GONE);
                break;
            case "B":
                bB.setVisibility(View.GONE);
                break;
            case "C":
                bC.setVisibility(View.GONE);
                break;
            case "D":
                bD.setVisibility(View.GONE);
                break;
            case "E":
                bE.setVisibility(View.GONE);
                break;
            case "F":
                bF.setVisibility(View.GONE);
                break;
            case "G":
                bG.setVisibility(View.GONE);
                break;
            case "H":
                bH.setVisibility(View.GONE);
                break;
            case "I":
                bI.setVisibility(View.GONE);
                break;
            case "J":
                bJ.setVisibility(View.GONE);
                break;
            case "K":
                bK.setVisibility(View.GONE);
                break;
            case "L":
                bL.setVisibility(View.GONE);
                break;
            case "M":
                bM.setVisibility(View.GONE);
                break;
            case "N":
                bN.setVisibility(View.GONE);
                break;
            case "O":
                bO.setVisibility(View.GONE);
                break;
            case "P":
                bP.setVisibility(View.GONE);
                break;
            case "Q":
                bQ.setVisibility(View.GONE);
                break;
            case "R":
                bR.setVisibility(View.GONE);
                break;
            case "S":
                bS.setVisibility(View.GONE);
                break;
            case "T":
                bT.setVisibility(View.GONE);
                break;
            case "U":
                bU.setVisibility(View.GONE);
                break;
            case "V":
                bV.setVisibility(View.GONE);
                break;
            case "W":
                bW.setVisibility(View.GONE);
                break;
            case "X":
                bX.setVisibility(View.GONE);
                break;
            case "Y":
                bY.setVisibility(View.GONE);
                break;
            case "Z":
                bZ.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.single_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.new_word:
                init();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(SinglePlayerActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.exit:
                AlertDFragment myDialog = AlertDFragment.newInstance(R.drawable.login_out_icon, R.string.exit, R.string.are_you_sure, true, true, R.string.yes, R.string.no);
                myDialog.show(getSupportFragmentManager(), "DIALOG_EXIT_SINGLE_PLAYER");
               /* Intent intent1 = getIntent();
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                onDestroy();*/
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        vib = sharedPrefs.getBoolean("Vibration", true);
        isSoundOn = sharedPrefs.getBoolean("soundEffects", true);
        difficulty = sharedPrefs.getString("DIFFICULTY", "EASY");
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        vib = sharedPrefs.getBoolean("Vibration", true);
        isSoundOn = sharedPrefs.getBoolean("soundEffects", true);
        difficulty = sharedPrefs.getString("DIFFICULTY", "EASY");
    }

    @Override
    protected void onDestroy() {
      //  mClick.release();
        super.onDestroy();
    }

    //method to draw hangman picture based on int miss
    public void hangmanPicture(int num) {

        switch (num) {
            case 1:
                iVHang.setVisibility(View.VISIBLE);
                iVHang.setImageResource(R.drawable.hang0);

                break;
            case 2:
                iVHang.setImageResource(R.drawable.hang1);
                break;
            case 3:
                iVHang.setImageResource(R.drawable.hang2);
                break;
            case 4:
                iVHang.setImageResource(R.drawable.hang4);
                break;
            case 5:
                iVHang.setImageResource(R.drawable.hang6);
                break;
            case 6:
                iVHang.setImageResource(R.drawable.hang8);
                break;
            case 7:
                iVHang.setImageResource(R.drawable.hang10);
                break;
            default:
        }
    }

    //this is the implementation of AlertDFragment.DialogListener to deal with negative buttons on alert dialog
    @Override
    public void doNegativeClick(int title) {
        Fragment dialog_two = getSupportFragmentManager().findFragmentByTag("DIALOG_TWO");
        Fragment dialog_exit = getSupportFragmentManager().findFragmentByTag("DIALOG_EXIT_SINGLE_PLAYER");
      /*  if (isSoundOn) {
            mClick.start();
        }*/
        if (dialog_two != null && title == dialog_two.getArguments().getInt("TITLE")) {
            Intent intent = new Intent(SinglePlayerActivity.this, MainMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (dialog_exit != null && title == dialog_exit.getArguments().getInt("TITLE")) {
            onResume();
        }
    }

    //this is the implementation of AlertDFragment.DialogListener to deal with positive buttons on alert dialog
    @Override
    public void doPositiveClick(int title) {

        Fragment dialog_one = getSupportFragmentManager().findFragmentByTag("DIALOG_ONE");
        Fragment dialog_two = getSupportFragmentManager().findFragmentByTag("DIALOG_TWO");
        Fragment dialog_exit = getSupportFragmentManager().findFragmentByTag("DIALOG_EXIT_SINGLE_PLAYER");

      /*  if (isSoundOn) {
            mClick.start();
        }
*/
        if (dialog_one != null && title == dialog_one.getArguments().getInt("TITLE")) {
            if (isMultiPlayer) {
                Intent intent = new Intent(SinglePlayerActivity.this, MultiPlayerActivity.class);
                intent.putExtra("miss_value", miss);
                intent.putExtra("hit_value", hit);
                startActivity(intent);
            } else {
                if (difficulty.equalsIgnoreCase("MEDIUM")) {
                    hit = hit + 2;
                }
                if (difficulty.equalsIgnoreCase("HARD")) {
                    hit = hit + 5;
                }
                init();
            }
        } else if (dialog_two != null && title == dialog_two.getArguments().getInt("TITLE")) {
            if (isMultiPlayer) {
                nullScore();
                Intent intent = new Intent(SinglePlayerActivity.this, MultiPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                nullScore();
            }
        } else if (dialog_exit != null && title == dialog_exit.getArguments().getInt("TITLE")) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }
    }


    @Override
    public void hsOKClicked(String name) {
        savePlayer(name);
    }

    public void updateDB() {
        DataBaseHandler db = new DataBaseHandler(this);
        List<Player> myList = null;
        scoreValues = new ArrayList<>();
        try {
            myList = db.getAllPlayers();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (myList != null && myList.size() > 0) {
            Collections.sort(myList);
            for (int i = 0; i < myList.size(); i++) {
                Player player = myList.get(i);
                String score = player.getScore();

                scoreValues.add(Integer.valueOf(score));
            }

            if (scoreValues.size() < 10) {
                lowestScore = 0;
            }
            if (scoreValues.size() >= 10) {
                lowestScore = scoreValues.get(0);
            }

        }
    }

    @Override
    public void noMoreButton(int newScore) {
        score=newScore;
        String positionInTable = giveMePosition(score);
        HighScorerFragment frag = HighScorerFragment.newInstance(positionInTable);
        frag.show(getSupportFragmentManager(), "HS");
    }
}
