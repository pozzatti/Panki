package br.com.fmproc.panki.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.fmproc.panki.Card;
import br.com.fmproc.panki.Deck;
import br.com.fmproc.panki.DeckHistory;
import br.com.fmproc.panki.Direction;

public class Main {
	public static Deck loadDeck(String name){
		try {
			BufferedReader br = new BufferedReader(new FileReader(name));
			Deck deck = new Deck();
			deck.setName(name);
			String s;
			s = br.readLine();
			
			deck.setCardsNumber(Integer.parseInt(s));
			s = br.readLine();
			deck.setDirection(Direction.valueOf(s));
			deck.setCards(new ArrayList<Card>());
			
			while((s = br.readLine())!=null){
				String ss[] = s.split("\\|");
				Card card = new Card();
				card.setFront(ss[0]);
				if (ss.length==4){
					card.setBack(ss[2]);
					card.setRead(ss[1]);
				} else {
					card.setBack(ss[1]);	
				}				
				if (ss.length>2){
					card.setPriority(Integer.parseInt(ss[ss.length-1]));
				}				
				
				deck.getCards().add(card);
			}
			br.close();
			
			return deck;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

    public static DeckHistory loadHistory(String name){
        DeckHistory history = new DeckHistory();
        history.setName(name);

        try {
            BufferedReader br = new BufferedReader(new FileReader(name));
            String s;

            while((s = br.readLine())!=null){
                String ss[] = s.split(";");
                history.addCard(ss[0], new int[]{Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), Integer.parseInt(ss[3]), Integer.parseInt(ss[4]), Integer.parseInt(ss[5])});
            }
            br.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return history;
    }
	
	public static List<Card> buildCurrent(Deck deck){
		int cardNumbers = deck.getCardsNumber();
		List<Card> currents = new ArrayList<Card>();
		Map<Integer, List<Card>> mapPriority = new HashMap<Integer, List<Card>>();
		
		for (Card card : deck.getCards()) {
			if (!mapPriority.containsKey(card.getPriority())){
				mapPriority.put(card.getPriority(), new ArrayList<Card>());
			}
			mapPriority.get(card.getPriority()).add(card);
		}
		
		List<Integer> listPriority = new ArrayList<Integer>();
		listPriority.addAll(mapPriority.keySet());
		Collections.sort(listPriority);
		
		for (int i = listPriority.size()-1; i >= 0; i--) {
			List<Card> l = mapPriority.get(listPriority.get(i));
			if (l.size()<=cardNumbers){
				currents.addAll(l);
				cardNumbers-=l.size();
			} else {
				Random r = new Random();
				while (l.size()!=0 && cardNumbers!=0){
					currents.add(l.remove(r.nextInt(l.size())));
					cardNumbers--;
				}
			}
		}

		for(Card card : deck.getCards()){
			if (!currents.contains(card)){
				card.setPriority(card.getPriority()+1);
			} else {
				card.setPriority(0);
			}
		}
		
		if (deck.getDirection().equals(Direction.BOTH) || deck.getDirection().equals(Direction.BACK_ONLY)){
			List<Card> backList = new ArrayList<Card>();
			
			for (Card card : currents) {
				Card backCard = new Card();
				backCard.setFront(card.getBack());
				backCard.setBack(card.getFront());
				backCard.setPriority(card.getPriority());
				backCard.setBaseCard(card);
				backCard.setRead(card.getRead());
				backList.add(backCard);
			}
			
			if (deck.getDirection().equals(Direction.BOTH)){
				currents.addAll(backList);
			} else {
				currents = backList;
			}
		}
		
		Collections.shuffle(currents);
		
		return currents;
	}
}
