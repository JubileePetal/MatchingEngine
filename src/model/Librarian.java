package model;

import java.util.HashMap;

import models.Instrument;

public class Librarian {
	
	private HashMap<String,OrderBook> library;
	
	public Librarian() {
		
		library = new HashMap<String, OrderBook>();
	}
	
	
	public void addOrderBook(Instrument inst){
		
		OrderBook newOrderBook = new OrderBook();
		//TODO Setup orderbook stuff here
		library.put(inst.getAbbreviation(), newOrderBook);
		System.out.println("Size: " + library.size());
		
		
	}

}
