package haladektomas.mynewappdevcompany.ie.hangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tjeannin.apprate.AppRate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by HaladekT on 21/02/2016.
 * This is main screen activity, its shown 3 seconds after app starts.It sets a listview as a High Score Table
 * it gets all player from DB sort them out from highest score to lowest, and keeps only best 10. DB then gets
 * destroyed and its created again just from best 10 players.
 */

public class MainMenuActivity extends AppCompatActivity implements AlertDFragment.DialogListener {

    private Toolbar toolbar;
    ArrayList<Integer> scoreValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //app rate code used from
        //https://github.com/TimotheeJeannin/AppRate
        new AppRate(this)
                .setShowIfAppHasCrashed(false)
                .setMinDaysUntilPrompt(0)
                .setMinLaunchesUntilPrompt(13)
                .init();

        Button singePlayerGame = (Button) findViewById(R.id.button);
        Button multiplayerGame = (Button) findViewById(R.id.button2);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_screen);
        setSupportActionBar(toolbar);
        final ListView listView = (ListView) findViewById(R.id.listView_table);
        DataBaseHandler db = new DataBaseHandler(this);
        final ArrayList<Player> myBestPlayers = new ArrayList<Player>();
        List<Player> myList = null;
        scoreValues = new ArrayList<>();


        try {
            myList = db.getAllPlayers();
            db.deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(myList.size()<10) {
            for (int i = 1; i < 11; i++) {
                String nameplayer = "Player " + i;
                Player player = new Player();
                player.setName("Player " + i);
                player.setScore("1");
                player.setGameDif("EASY");
                if (i % 2 == 0) {
                    player.setType(0);

                } else {
                    player.setType(1);
                }
                player.setWinningWord("HELLO");
                myList.add(player);
            }

        }

        if(myList!=null && myList.size()>0){
            Collections.sort(myList);
            for (int i=0;i<myList.size();i++) {
                Player player = myList.get(i);
                String score = player.getScore();

                scoreValues.add(Integer.valueOf(score));
            }


        }

        int counter = 0;
        for (int i = myList.size() - 1; i >= 0; i--) {
            Player player = new Player();
            player = myList.get(i);
            myBestPlayers.add(player);
            db.addDBEntry(player);
            counter++;
            if (counter == 10) {
                break;
            }

        }

        HighScoreAdapter adapter = new HighScoreAdapter(this, R.layout.screen_list, myBestPlayers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player winningPlayer = myBestPlayers.get(position);
                String msg = winningPlayer.getWinningWord();
                Log.i("MSG", "WW  " + msg);
                switch (msg) {
                    case "0":
                        Toast.makeText(getApplicationContext(), getString(R.string.topic).concat(getString(R.string.random)), Toast.LENGTH_SHORT).show();
                        break;
                    case "1":
                        Toast.makeText(getApplicationContext(), getString(R.string.topic).concat(getString(R.string.car_brands)), Toast.LENGTH_SHORT).show();
                        break;
                    case "2":
                        Toast.makeText(getApplicationContext(), getString(R.string.topic).concat(getString(R.string.europe_contries)), Toast.LENGTH_SHORT).show();
                        break;
                    case "3":
                        Toast.makeText(getApplicationContext(), getString(R.string.topic).concat(getString(R.string.europe_capitals)), Toast.LENGTH_SHORT).show();
                        break;
                    case "HELLO":
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), getString(R.string.loosing_word) + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Intent intent;
        switch (id) {
            case R.id.exit:
                AlertDFragment myDialog = AlertDFragment.newInstance(R.drawable.login_out_icon, R.string.exit, R.string.are_you_sure, true, true, R.string.yes, R.string.no);
                myDialog.show(getSupportFragmentManager(), "DIALOG_EXIT");
                break;
            case R.id.settings:
                intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //this method deals with single player button clicks, on click it builds new alertDialog options to choose topic
    //for words, it then lauches SinglePlayerActivity
    public void SinglePlayerGame(View v) {
        CharSequence[] topic = new CharSequence[]{getString(R.string.random), getString(R.string.car_brands),
                getString(R.string.europe_contries), getString(R.string.europe_capitals)};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.pick_a_topic);
        dialog.setItems(topic, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainMenuActivity.this, SinglePlayerActivity.class);
                intent.putExtra("topic", which);
                intent.putIntegerArrayListExtra("SCORE_VALUES",scoreValues);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    //this method deals with multi player button, it just launches MultiPlayerActivity
    public void MultiPlayerGame(View v) {
        Intent intent = new Intent(this, MultiPlayerActivity.class);
        intent.putExtra("miss_value", 0);
        intent.putExtra("hit_value", 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //AlertDFraglent.DialogListener implementation for negative button
    @Override
    public void doNegativeClick(int title) {


    }

    //AlertDFraglent.DialogListener implementation for positive button
    @Override
    public void doPositiveClick(int title) {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }
}


