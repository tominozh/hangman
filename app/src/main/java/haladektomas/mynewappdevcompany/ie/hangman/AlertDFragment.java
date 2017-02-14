package haladektomas.mynewappdevcompany.ie.hangman;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/** Created by HaladekT on 21/03/2016.
 * Constructor for AlertDialog fragment. It gets build when instantiated. I also created
 * an interface to get a handle to deal with AlertDialog button clicks.
 */
public class AlertDFragment extends DialogFragment {


    private DialogListener activityCommander;

    public interface DialogListener {

        void doNegativeClick(int title);

        void doPositiveClick(int title);
    }

    public AlertDFragment() {
    }

    public static AlertDFragment newInstance(int icon, int title, int message, boolean hasPosBtn, boolean hasNegBtn, int posBtnText, int negBtnTxt) {
        AlertDFragment frag = new AlertDFragment();
        Bundle args = new Bundle();
        args.putInt("ICON", icon);
        args.putInt("TITLE", title);
        args.putInt("MESSAGE", message);
        args.putBoolean("POSITIVE", hasPosBtn);
        args.putBoolean("NEGATIVE", hasNegBtn);
        args.putInt("POSITIVE_TEXT", posBtnText);
        args.putInt("NEGATIVE_TEXT", negBtnTxt);
        frag.setCancelable(false);
        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int icon = getArguments().getInt("ICON");
        final int title = getArguments().getInt("TITLE");
        int message = getArguments().getInt("MESSAGE");
        boolean hasPosBtn = getArguments().getBoolean("POSITIVE");
        boolean hasNegBtn = getArguments().getBoolean("NEGATIVE");
        int posBtnTxt = getArguments().getInt("POSITIVE_TEXT");
        int negBtnTxt = getArguments().getInt("NEGATIVE_TEXT");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setIcon(icon);
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (hasNegBtn) {
            dialog.setNegativeButton(negBtnTxt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activityCommander.doNegativeClick(title);
                }
            });
        }
        if (hasPosBtn) {
            dialog.setPositiveButton(posBtnTxt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activityCommander.doPositiveClick(title);
                }
            });
        }
        return dialog.create();
    }

    @Override
    public void onAttach(Context context) {
        activityCommander = (DialogListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

