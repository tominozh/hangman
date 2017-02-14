package haladektomas.mynewappdevcompany.ie.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**User1 enters a word, press button GO, than SingleplayerActivity is launched and user2 is guessing letters.
 * This activity only takes input (String word) and checks if it contains only letters and spaces.
 */

public class MultiPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    static int miss;
    static int hit;
    EditText enterMultiWord;
    ImageButton goButton;
    TextView textViewHit, textViewMiss;

    boolean isSoundOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enterMultiWord = (EditText) findViewById(R.id.editText_word);
        enterMultiWord.setFocusable(true);
        enterMultiWord.requestFocus();
        enterMultiWord.setImeActionLabel("DONE", KeyEvent.KEYCODE_ENTER);
        textViewHit = (TextView) findViewById(R.id.textView_hit);
        textViewMiss = (TextView) findViewById(R.id.textView_miss);
        goButton = (ImageButton) findViewById(R.id.go_button);
        goButton.setOnClickListener(this);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        isSoundOn=sharedPrefs.getBoolean("soundEffects", true);

        Intent intent = getIntent();
        miss = intent.getIntExtra("miss_value", 0);
        hit = intent.getIntExtra("hit_value", 0);

        textViewHit.setText(String.valueOf(hit));
        textViewMiss.setText(String.valueOf(miss));

        enterMultiWord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    System.out.println("pressed key "+enterMultiWord.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        String multiWord = enterMultiWord.getText().toString();
        Pattern pattern = Pattern.compile("^[ A-z]+$");
        Matcher matcher = pattern.matcher(multiWord);
        System.out.println(multiWord);
        if (multiWord.length()>0) {
            if (matcher.matches()) {
                Intent intent = new Intent(MultiPlayerActivity.this, SinglePlayerActivity.class);
                intent.putExtra("MultiWord", multiWord);
                intent.putExtra("miss_value", miss);
                intent.putExtra("hit_value", hit);
                intent.putExtra("isMultiPlayer", true);
                startActivity(intent);

            } else {
                Toast.makeText(this, R.string.only_letters_allowed, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.enter_word_first, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.multi_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent = new Intent(MultiPlayerActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.multi_player_exit:


            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isSoundOn=sharedPreferences.getBoolean("soundEffects",true);
    }

    @Override
    protected void onDestroy() {
         super.onDestroy();
    }
}
