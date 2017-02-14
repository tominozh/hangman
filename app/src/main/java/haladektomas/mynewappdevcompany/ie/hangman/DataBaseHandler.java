package haladektomas.mynewappdevcompany.ie.hangman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Created by HaladekT on 29/02/2016.
 *
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myDB";
    public static final String TABLE_HCPLAYERS = "highScoreTable";

    public static final String KEY_ID="id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCORE = "score";
    public static final String KEY_WW = "winningWord";
    public static final String KEY_DIF = "gamediffic";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HSTABLE="CREATE TABLE IF NOT EXISTS "+TABLE_HCPLAYERS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+KEY_TYPE + " INTEGER , " + KEY_NAME + " TEXT ,NULL, " + KEY_SCORE + " TEXT ,NULL, " +
                KEY_WW + " TEXT ,NULL " + KEY_DIF + " TEXT,NULL " + ")" ;

        db.execSQL(CREATE_HSTABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_HCPLAYERS);
        onCreate(db);
    }

    //add entry to DataBase
    public void addDBEntry(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, player.getName());
        values.put(KEY_SCORE, player.getScore());
        values.put(KEY_TYPE,player.getType());
        values.put(KEY_WW,player.getWinningWord());
        values.put(KEY_DIF,player.getGameDif());
                db.insert(TABLE_HCPLAYERS, null, values);
        db.close();
    }

    public List<Player> getAllPlayers() throws SQLException{
        List<Player> myList = new ArrayList<>();
        String selectQuery = "SELECT * FROM highScoreTable";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Player myPlayer = new Player();
                myPlayer.setId(Integer.parseInt(cursor.getString(0)));
                myPlayer.setType(Integer.parseInt(cursor.getString(1)));
                myPlayer.setName(cursor.getString(2));
                myPlayer.setScore(cursor.getString(3));
                myPlayer.setWinningWord(cursor.getString(4));
                myPlayer.setGameDif(cursor.getString(5));
                myList.add(myPlayer);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return myList;
    }

    public void deleteAll() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_HCPLAYERS, null, null);
        db.close();
    }



}
