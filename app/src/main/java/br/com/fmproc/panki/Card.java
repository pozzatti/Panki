package br.com.fmproc.panki;

import java.io.Serializable;

public class Card implements Serializable{
	private static final long serialVersionUID = -4275111503483357972L;

	private String front;
	private String read;
	private String back;
	private int priority;	
	private Card baseCard;
	
	public Card(){}
	
	public String getFront(){
		return front;
	}
	
	public void setFront(String front){
		this.front = front;
	}
	
	public void setRead(String read){
		this.read = read;
	}
	
	public String getRead(){
		return read;
	}
	
	public String getBack(){
		return back;
	}
	
	public void setBack(String back){
		this.back = back;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public void setPriority(int priority){
		this.priority = priority;
	}
	
	public Card getBaseCard(){
		return baseCard;
	}
	
	public void setBaseCard(Card baseCard){
		this.baseCard = baseCard;
	}
	
	public boolean equals(Object obj){
		boolean ret = false;
		if (obj instanceof Card){
			Card vo = (Card)obj;
			ret = (front!=null?front.equals(vo.front):vo.front==null);
			ret &= (back!=null?back.equals(vo.back):vo.back==null);
		}
		
		return ret;
	}
	
	public int hashCode(){
		return ((front!=null?front:"")+(back!=null?back:"")).hashCode();
	}
}
