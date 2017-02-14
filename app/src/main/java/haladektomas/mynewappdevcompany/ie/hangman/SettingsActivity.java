package haladektomas.mynewappdevcompany.ie.hangman;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**Created by Haladekt on 04/03/2016.
 * this java file deals with preferences.xml, especially with switch preferences, it commits a change
 * to shared preferences.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_SOUND = "soundEffects";
    public static final String KEY_VIBRATION = "Vibration";

    boolean isVib, isSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();


        if (key.equals(KEY_SOUND)) {
            isSound = sharedPreferences.getBoolean("soundEffects", true);
            if (isSound) {
                editor.putBoolean("soundEffects", false);
                editor.commit();
            }
        }
        if (key.equals(KEY_VIBRATION)) {
            isVib = sharedPreferences.getBoolean("Vibration", true);
            if (isVib) {
                editor.putBoolean("Vibration", false);
                editor.commit();
            }
        }
    }

}

