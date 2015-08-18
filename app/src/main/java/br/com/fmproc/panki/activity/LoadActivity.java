package br.com.fmproc.panki.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.fmproc.panki.Card;
import br.com.fmproc.panki.Deck;
import br.com.fmproc.panki.DeckItemAdapter;
import br.com.fmproc.panki.R;
import br.com.fmproc.panki.R.id;
import br.com.fmproc.panki.R.layout;
import br.com.fmproc.panki.exception.PankiException;
import br.com.fmproc.panki.main.Main;

public class LoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_load);
        setTitle("Escolha o Deck");

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/panki");
        List<Deck> decks = new ArrayList<Deck>();
        for(String s: dir.list()){
            if (!s.contains(".history.txt")) {
                Deck deckResult = new Deck();
                try {
                    Deck deck = Main.loadDeck(Environment.getExternalStorageDirectory().getPath() + "/panki/" + s);
                    List<Card> cards = deck.getCards();
                    int negative = 0;
                    for (Card card : cards){
                        if (card.getPriority() <= 0){
                            negative++; 
                        }
                    }
                    deckResult.setName(s.replaceAll(".txt", ""));
                    deckResult.setUnread((int)((double)negative/cards.size()*100));

                }catch (PankiException e){
                    deckResult.setName("<ERROR line "+e.getMessage()+"> "+s.replaceAll(".txt", ""));
                }
                catch (Exception e){}
                decks.add(deckResult);
            }
        }

        DeckItemAdapter adapter = new DeckItemAdapter(this, layout.deck, decks);

        final ListView lv = (ListView)findViewById(id.listDecks);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Deck  deck    = (Deck) lv.getItemAtPosition(position);
                String itemValue = deck.getName();

                Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                intent.putExtra("currentDeck", itemValue);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.load, menu);
        return true;
    }

}
