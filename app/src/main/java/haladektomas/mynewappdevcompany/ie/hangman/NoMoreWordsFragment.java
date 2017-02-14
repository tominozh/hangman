package haladektomas.mynewappdevcompany.ie.hangman;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tomas & Aoibh on 13/05/2016.
 */
public class NoMoreWordsFragment extends DialogFragment {

    private ScoreIncrease scoreIncrease;
    public NoMoreWordsFragment(){}

    public static NoMoreWordsFragment newInstance(int hit){
        NoMoreWordsFragment fragment = new NoMoreWordsFragment();
        Bundle args = new Bundle();
        args.putInt("HIT",hit);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_more_words,container);
        TextView score = (TextView)rootView.findViewById(R.id.anim_score);
        int currentScore = getArguments().getInt("HIT");

        animateTextView(currentScore,500,score);
        Button noMoreButton = (Button)rootView.findViewById(R.id.no_more_button);
        noMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             scoreIncrease.noMoreButton(500);
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

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.5f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 10) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(finalCount + "");
                }
            }, time);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scoreIncrease = (ScoreIncrease) context;
    }

    public interface ScoreIncrease{
        void noMoreButton(int hit);
    }
}
