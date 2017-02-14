package haladektomas.mynewappdevcompany.ie.hangman;


/**
 * Created by HaladekT on 29/02/2016.
 * Constructor for Player object. This class also implements
 * Comparable interface that i use to compare Players score
 * when filling the High Score Table
 */
public class Player implements Comparable<Player> {
    private int id;
    private String name;
    private String score;
    private String winningWord;
    private int type;
    private String gameDif;

    public Player(){}  //empty contructor

  public Player(int id,int tp,String nm,String sc,String ww,String df){  //full, containig also a id field, its never used
       this.setId(id);
        this.setName(nm);
        this.setScore(sc);
        this.setWinningWord(ww);
        this.setType(tp);
      this.setGameDif(df);


    }

  public Player (int tp,String nm,String sc,String ww,String df){ //full contructor without id field
        this.setName(nm);
        this.setScore(sc);
        this.setWinningWord(ww);
        this.setType(tp);
      this.setGameDif(df);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWinningWord() {
        return winningWord;
    }

    public void setWinningWord(String winningWord) {
        this.winningWord = winningWord;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGameDif() {
        return gameDif;
    }

    public void setGameDif(String gameDif) {
        this.gameDif = gameDif;
    }

    /**
     * overrdiden compareTo method to sort players based on the score value
     */
    @Override
    public int compareTo( Player another) {
        String compareScore = another.getScore();
        return Integer.valueOf(this.getScore()).compareTo(Integer.valueOf(compareScore));

    }





}
