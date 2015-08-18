package br.com.fmproc.panki;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class Deck {
	private String name;
	private int cardsNumber;
	private Direction direction;
	private List<Card> cards;
    private int unread;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getCardsNumber(){
		return cardsNumber;
	}
	
	public void setCardsNumber(int cardsNumber){
		this.cardsNumber = cardsNumber;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void setDirection(Direction direction){
		this.direction = direction;
	}
	
	public List<Card> getCards(){
		return cards;
	}
	
	public void setCards(List<Card> cards){
		this.cards = cards;
	}

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public void persist(){
		try {
			PrintWriter pw = new PrintWriter(new File(name), "UTF-8");
			pw.write(""+cardsNumber+"\r\n");
			pw.write(""+direction+"\r\n");
			for (Card card : cards) {
				pw.write(card.getFront()+(card.getRead()!=null?"|"+card.getRead():"")+"|"+card.getBack()+"|"+card.getPriority()+"\r\n");
			}
			pw.flush();
			pw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
