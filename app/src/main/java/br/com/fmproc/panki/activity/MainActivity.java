package br.com.fmproc.panki.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.fmproc.panki.Card;
import br.com.fmproc.panki.Deck;
import br.com.fmproc.panki.DeckHistory;
import br.com.fmproc.panki.R;
import br.com.fmproc.panki.exception.PankiException;
import br.com.fmproc.panki.main.Main;

public class MainActivity extends Activity {
	private String currentDeck;
	private Deck deck;
    private DeckHistory history;
	private List<Card> currents;
	private Card current;
	private TextView textFront;
	private TextView textBack;
	
	private List<Card> done;
	private String temp;	
	private boolean next = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = getIntent();
        currentDeck = intent.getStringExtra("currentDeck");
        setTitle(currentDeck);

        try {
            deck = Main.loadDeck(Environment.getExternalStorageDirectory().getPath() + "/panki/" + currentDeck + ".txt");
            history = Main.loadHistory(Environment.getExternalStorageDirectory().getPath() + "/panki/" + currentDeck + ".history.txt");

            currents = Main.buildCurrent(deck);
            done = new ArrayList<Card>();

            current = currents.remove(0);
            textFront = (TextView) findViewById(R.id.textFront);
            textBack = (TextView) findViewById(R.id.textBack);

            Button btnRepetir = (Button) findViewById(R.id.buttonRepetir);
            Button btnProximo = (Button) findViewById(R.id.buttonProximo);
            Button btnFurigana = (Button) findViewById(R.id.buttonFurigana);

            btnRepetir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (next && current != null) {
                        Random r = new Random();
                        next = false;

                        int increment = 1;
                        if (deck.getCards().size() > deck.getCardsNumber()) {
                            increment = (deck.getCards().size() / (deck.getCardsNumber() * 2) + 1);
                        }
                        if (increment < 1) {
                            increment = 1;
                        }

                        current.setPriority(current.getPriority() + increment);

                        currents.add(currents.size() > 0 ? r.nextInt(currents.size()) + 1 : 0, current);
                        current = currents.remove(0);
                        setLabels(current.getFront(), "");
                        temp = null;
                    }
                }
            });

            btnProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (next) {
                        if (current != null) {
                            if (currents.size() > 0) {
                                next = false;
                                done.add(current);
                                current = currents.remove(0);
                                setLabels(current.getFront(), "");
                                temp = null;
                            } else {
                                setLabels("ACABOU!", "");

                                int increment = 1;
                                if (deck.getCards().size() > deck.getCardsNumber()) {
                                    increment = (deck.getCards().size() / (deck.getCardsNumber() * 2) + 1);
                                }
                                if (increment < 1) {
                                    increment = 1;
                                }

                                for (Card card : done) {
                                    if (card.getBaseCard() != null && card.getPriority() > card.getBaseCard().getPriority()) {
                                        card.getBaseCard().setPriority(card.getPriority());
                                    }
                                }
                                done.add(current);
                                current = null;
                                for (Card card : done) {
                                    String cardName = card.getFront() + "|" + card.getBack();
                                    history.addLastOccurrence(cardName, card.getPriority());

                                    if (history.isLastFiveZero(cardName)) {
                                        card.setPriority(-3 * increment);
                                    } else if (history.isLastFourZero(cardName)) {
                                        card.setPriority(-2 * increment);
                                    } else if (history.isLastThreeZero(cardName)) {
                                        card.setPriority(-1 * increment);
                                    } else {
                                        int mean = history.getLastThreeOccurrencesMean(cardName);
                                        card.setPriority(mean > card.getPriority() ? mean : card.getPriority());
                                    }
                                }
                                history.persist();
                                deck.persist();
                            }
                        }
                    }
                }
            });

            btnFurigana.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current != null && current.getRead() != null) {
                        if (temp == null) {
                            temp = (String) textFront.getText();
                            textFront.setText(current.getRead());
                        } else {
                            textFront.setText(temp);
                            temp = null;
                        }
                    }
                }
            });

            setLabels(current.getFront(), "");
        }catch (PankiException e){

        }
    }    
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = super.onTouchEvent(event);
		
		if (!next){
			next=true;
			setLabels(current.getFront(), current.getBack());
		}
		
		return ret;
	}

	private void setLabels(String front, String back){
		textFront.setText(front);
		textBack.setText(back);
		setTitle(currentDeck + " - " + "Faltam " + (currents.size()+1));
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
