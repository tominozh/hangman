package haladektomas.mynewappdevcompany.ie.hangman;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Tomas & Aoibh on 11/05/2016.
 */
public class HighScorerFragment extends DialogFragment {
    private MediaPlayer aplause;
    private boolean isSoundOn;
    public interface HsFragListener{
       void hsOKClicked(String name);
    }
    private HsFragListener listener;
    public HighScorerFragment(){}

    public static HighScorerFragment newInstance(String num){
        Log.i("FRAG","instance "+num);
        HighScorerFragment frag = new HighScorerFragment();
        Bundle args = new Bundle();
        args.putString("KEY",num);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.high_scorer_frag_layout,container);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isSoundOn= sharedPreferences.getBoolean("soundEffects", true);
        if(isSoundOn){
            aplause = MediaPlayer.create(getContext(),R.raw.applause7);
            aplause.start();
        }

        this.setCancelable(false);
        TextView text = (TextView)rootView.findViewById(R.id.number_high_score);
        text.setText(getArguments().getString("KEY"));
        final EditText editText = (EditText)rootView.findViewById(R.id.enter_name_et);
        Button okButton = (Button)rootView.findViewById(R.id.ok_button_hsfrag);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editText.getText().toString();
                aplause.release();
                if(name.equalsIgnoreCase("")){
                    name="Player";
                }
                listener.hsOKClicked(name);
            }
        });
        return rootView;
    }

   public void onResume() {

        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();

        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        // Set the width of the dialog proportional to 95% of the screen width
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        // Call super onResume after sizing
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (HsFragListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragButtonListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (HsFragListener)activity;
    }
}
