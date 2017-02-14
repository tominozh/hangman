package haladektomas.mynewappdevcompany.ie.hangman;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HaladekT on 29/02/2016.
 * This class handles a highScoreTable listView rows, it creates a row as View, fill all details from
 * Player obj and then returns it.
 */
public class HighScoreAdapter extends ArrayAdapter<Player> {
    Context context;
    int layoutResourceId;
    ArrayList<Player> myList;


    public HighScoreAdapter(Context context, int layoutResourceId, ArrayList<Player> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.myList = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DetailsHolder detailsHolder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            detailsHolder = new DetailsHolder();
            detailsHolder.textView_id = (TextView) row.findViewById(R.id.textView_id);
            detailsHolder.textView_name = (TextView) row.findViewById(R.id.textView_name);
            detailsHolder.textView_score = (TextView) row.findViewById(R.id.textView_score);
            detailsHolder.imageView_gametype = (ImageView) row.findViewById(R.id.imageView_gametype);
            row.setTag(detailsHolder);
        } else {
            detailsHolder = (DetailsHolder) row.getTag();
        }
        Player player = myList.get(position);
        detailsHolder.textView_id.setText(String.valueOf(position + 1));
        detailsHolder.textView_name.setText(player.getName());
        detailsHolder.textView_score.setText(player.getScore());
        if (player.getType() == 1) {
            detailsHolder.imageView_gametype.setImageResource(R.drawable.icon_users);
        } else if (player.getType() == 0) {
            detailsHolder.imageView_gametype.setImageResource(R.drawable.usericon);
        }
        return row;
    }

    static class DetailsHolder {
        TextView textView_id;
        TextView textView_name;
        TextView textView_score;
        ImageView imageView_gametype;
    }
}
