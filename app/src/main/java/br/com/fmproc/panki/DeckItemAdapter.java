package br.com.fmproc.panki;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Marie on 20/07/2015.
 */
public class DeckItemAdapter extends ArrayAdapter<Deck> {

    private Context context;
    private int resourceId;
    private List<Deck> decks;

    public DeckItemAdapter(Context context, int resourceId, List<Deck> decks) {
        super(context, resourceId, decks);
        this.resourceId = resourceId;
        this.context = context;
        this.decks = decks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);
        }

        Deck deck = decks.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewName = (TextView) convertView.findViewById(R.id.deckName);
        textViewName.setText(deck.getName()+" ["+deck.getUnread()+"%]");

        ImageView imageViewSetting = (ImageView) convertView.findViewById(R.id.setting);
        imageViewSetting.setImageResource(R.drawable.ic_action_settings);

        return convertView;


    }

}
